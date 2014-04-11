/*
 * Copyright 2014 Mostafa Gazar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.meg7.emailer;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.meg7.emailer.util.Constants;
import com.meg7.emailer.util.MLog;
import com.meg7.emailer.util.ProgressPreferenceUtils;
import com.meg7.emailer.util.Scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Manage how to do each cycle example.
 *
 * @author Mostafa Gazar
 */
public class EmailerManager extends TaskerManager {

    private static final String TAG = EmailerManager.class.getSimpleName();

    public static final int MAX_TASKS_PER_DAY = 4;

    public static final int MAX_RECIPIENTS_PER_EMAIL = 5;
//    public static final int MAX_EMAILS_PER_CYCLE = 25;
//    public static final int MAX_EMAILS_PER_DAY = 100;

    public EmailerManager(Context context) {
        super(context);
    }

    @Override
    protected void processCycle(int cycle) {
        // Do your thing here.
        final String username = PreferenceUtils.getEmailUsername(mContext);
        final String password = PreferenceUtils.getEmailPassword(mContext);
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            // TODO :: Populate some sort of error to UI.
            return;
        }

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        MLog.v(TAG, "Session created for :: " + username);

        final String fromEmail = PreferenceUtils.getFromEmail(mContext);
        final String fromName = PreferenceUtils.getFromName(mContext);
        final String subject = PreferenceUtils.getEmailSubject(mContext);
        final String message = PreferenceUtils.getEmailMessage(mContext);

        List<String> all = readEmailsFromFile(cycle + ".txt");
        List<String> emails;
        List<Message> messages = new ArrayList<Message>();

        int size = all.size();
        int messagesCount = (int) Math.ceil(size / (MAX_RECIPIENTS_PER_EMAIL * 1d));
        MLog.v(TAG, "Emails :: " + size + ", messages count :: " + messagesCount);
        MLog.v(TAG, "Creating messages");
        for (int i = 0; i < messagesCount; i ++) {
            try {
                emails = all.subList(i * MAX_RECIPIENTS_PER_EMAIL, Math.min(i * MAX_RECIPIENTS_PER_EMAIL + MAX_RECIPIENTS_PER_EMAIL, size));

                messages.add(createMessage(emails, fromEmail, fromName, subject, message, session));
            } catch (Exception e) {
                MLog.e(TAG, e.toString());
            }

        }

        sendMessages(messages);

        // Reschedule the alarm
        if (getCyclesProcessedTodaySoFar() < EmailerManager.MAX_TASKS_PER_DAY) {
            Scheduler.scheduleNextWake(mContext);
        } else {
            Scheduler.scheduleNextWake(mContext, Constants.THRESHOLD_DAY);

            resetCyclesProcessedTodaySoFar();
        }
    }

    @Override
    public int getCyclesCount() {
        try {
            return mContext.getAssets().list("tasks").length;
        } catch (IOException ignore) {
            return 0;
        }
    }

    private List<String> readEmailsFromFile(String fileName) {
        List<String> emails = new ArrayList<String>();

        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = mContext.getAssets().open("tasks" + File.separator + fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                emails.add(line);
            }
        } catch (Exception e) {
            MLog.e(TAG, e.toString());
        } finally {
            if (inputStream != null) try { inputStream.close(); } catch (IOException e) { }
            if (reader != null) try { reader.close(); } catch (IOException e) { }
        }

        return emails;
    }

    private Message createMessage(List<String> emails,
                                  String fromEmail, String fromName,
                                  String subject, String text,
                                  Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail, fromName));
        for (String email : emails) {
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(email, email));
        }
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

    private void sendMessages(List<Message> messages) {
        resetProgress(mContext);

        int position = 0;
        int count = messages.size();
        for (Message message : messages) {
            position += 1;

            try {
                Transport.send(message);

                ProgressPreferenceUtils.incrementSentCount(mContext);
            } catch (Exception e) {
                ProgressPreferenceUtils.incrementFailedCount(mContext);

                MLog.e(TAG, e.toString());
            }

            ProgressPreferenceUtils.setProgressPercentage(mContext, position * 100 / count);

            if (!TaskerHelper.isTaskerRunning(mContext)) {
                resetProgress(mContext);
                return;
            }

            LocalBroadcastManager.getInstance(mContext).
                    sendBroadcast(new Intent(Constants.ACTION_UPDATE_PROGRESS_FRAGMENT));
        }
    }

    public static void resetProgress(Context context) {
        ProgressPreferenceUtils.setProgressPercentage(context, 0);
        ProgressPreferenceUtils.setSentCount(context, 0);
        ProgressPreferenceUtils.setFailedCount(context, 0);

        LocalBroadcastManager.getInstance(context).
                sendBroadcast(new Intent(Constants.ACTION_UPDATE_PROGRESS_FRAGMENT));
    }

    public static class PreferenceUtils {

        public static final String PREF_EMAIL_USERNAME = "emailUsername";
        public static final String PREF_EMAIL_PASSWORD = "emailPassword";

        public static final String PREF_FROM_EMAIL = "fromEmail";
        public static final String PREF_FROM_NAME = "fromName";
        public static final String PREF_EMAIL_SUBJECT  = "emailSubject";
        public static final String PREF_EMAIL_MESSAGE  = "emailMessage";


        private static String getPrefValue(Context context, String key, String defaultValue) {
            return PreferenceManager.getDefaultSharedPreferences(context).
                    getString(key, defaultValue);
        }
        private static void setPrefValue(Context context, String key, String value) {
            PreferenceManager.getDefaultSharedPreferences(context).
                    edit().
                    putString(key, value).
                    commit();
        }

        public static String getEmailUsername(Context context) {
            return getPrefValue(context, PREF_EMAIL_USERNAME, context.getString(R.string.default_email));
        }
        public static void setEmailUsername(Context context, String username) {
            setPrefValue(context, PREF_EMAIL_USERNAME, username);
        }

        public static String getEmailPassword(Context context) {
            return getPrefValue(context, PREF_EMAIL_PASSWORD, context.getString(R.string.default_password));
        }
        public static void setEmailPassword(Context context, String password) {
            setPrefValue(context, PREF_EMAIL_PASSWORD, password);
        }

        public static String getFromEmail(Context context) {
            return getPrefValue(context, PREF_FROM_EMAIL, context.getString(R.string.default_from_email));
        }
        public static void setFromEmail(Context context, String fromEmail) {
            setPrefValue(context, PREF_FROM_EMAIL, fromEmail);
        }

        public static String getFromName(Context context) {
            return getPrefValue(context, PREF_FROM_NAME, context.getString(R.string.default_from_name));
        }
        public static void setFromName(Context context, String fromName) {
            setPrefValue(context, PREF_FROM_NAME, fromName);
        }

        public static String getEmailSubject(Context context) {
            return getPrefValue(context, PREF_EMAIL_SUBJECT, context.getString(R.string.default_subject));
        }
        public static void setEmailSubject(Context context, String subject) {
            setPrefValue(context, PREF_EMAIL_SUBJECT, subject);
        }

        public static String getEmailMessage(Context context) {
            return getPrefValue(context, PREF_EMAIL_MESSAGE, context.getString(R.string.default_message));
        }
        public static void setEmailMessage(Context context, String message) {
            setPrefValue(context, PREF_EMAIL_MESSAGE, message);
        }

    }

}
