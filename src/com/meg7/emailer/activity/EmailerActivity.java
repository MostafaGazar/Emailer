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

package com.meg7.emailer.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.meg7.emailer.R;
import com.meg7.emailer.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Main activity, mainly fragments container.
 *
 * @author Mostafa Gazar
 */
public class EmailerActivity extends FragmentActivity {

    private static final String regex = " '[a-z,_,0-9,A-Z,.,-]+";
    private final Pattern pattern = Pattern.compile(regex);

    private Session session;

    private EditText usernameEdit;
    private EditText passwordEdit;
    private Button checkButton;

    private EditText subjectEdit;
    private EditText messageEdit;
    private Button sendButton;

    private TextView log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);
        checkButton = (Button) findViewById(R.id.check);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session = createSessionObject();

                log.setText("Session created for :: " + usernameEdit.getText());
                sendButton.setEnabled(true);
            }
        });

        subjectEdit = (EditText) findViewById(R.id.subject);
        messageEdit = (EditText) findViewById(R.id.message);
        sendButton = (Button) findViewById(R.id.send);
        sendButton.setEnabled(false);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendButton.setEnabled(false);
                checkButton.setEnabled(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final List<String> all = readEmails("4.txt");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final String subject = subjectEdit.getText().toString();
                                final String messageBody = messageEdit.getText().toString();

                                List<String> emails;
                                List<Message> messages = new ArrayList<Message>();

                                int size = all.size();
                                int messagesCount = (int) Math.ceil(size / 10.0d);
                                log.append("\n\nSize :: " + size + ", messages count :: " + messagesCount + "\n\n");
                                log.append("\nCreating messages");
                                for (int i = 10; i < 20; i ++) {// for (int i = 0; i < messagesCount; i ++) {
                                    try {
                                        emails = all.subList(i * 10, Math.min(i * 10 + 10, size));

                                        messages.add(createMessage(emails, subject, messageBody, session));
                                        log.append(".");
//                                        System.gc();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                new SendMailTask().execute(messages);
                            }
                        });
                    }
                }).start();
            }
        });

        log = (TextView) findViewById(R.id.log);
    }

    private List<String> readEmails(String fileName) {
        List<String> emails = new ArrayList<String>();

        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            Matcher matcher;
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        emails.add(matcher.group().replace(" '", "") + "@gmail.com");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) try { inputStream.close(); } catch (IOException e) { }
            if (reader != null) try { reader.close(); } catch (IOException e) { }
        }

        return emails;
    }

    private Message createMessage(List<String> emails, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Constants.CONFIG_EMAILER_EMAIL, Constants.CONFIG_EMAILER_NAME));
        for (String email : emails) {
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(email, email));
        }
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        usernameEdit.getText().toString(), passwordEdit.getText().toString());
            }
        });
    }

    private class SendMailTask extends AsyncTask<List<Message>, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            log.append("\n\nSending");
        }

        @Override
        protected Void doInBackground(List<Message>... messages) {
            for (Message message : messages[0]) {
                try {
                    Transport.send(message);
                    publishProgress();

                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            log.append(".");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            log.append("\n\nDone");

            checkButton.setEnabled(true);
            sendButton.setEnabled(false);
        }
    }
}
