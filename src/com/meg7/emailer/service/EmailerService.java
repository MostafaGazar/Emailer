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

package com.meg7.emailer.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.meg7.emailer.util.Scheduler;
import com.meg7.emailer.util.ConnectionHelper;
import com.meg7.emailer.util.MLog;

/**
 * Emailer service for sending emails by rate matching user settings.
 *
 * @author Mostafa Gazar
 */
public class EmailerService extends IntentService {

    public static final String TAG = EmailerService.class.getSimpleName();

    public EmailerService() {
        super(EmailerService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MLog.d(TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MLog.d(TAG, "onHandleIntent");

        // De-register any connection listeners.
        ConnectionHelper.unregisterConnectivityListener(getApplicationContext());

        // Check connection status.
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            MLog.w(TAG, "No connection");

            MLog.d(TAG, "Registering a connection listener");
            // Register Connectivity listener.
            ConnectionHelper.registerConnectivityListener(getApplicationContext());
        } else {
            // TODO :: Do some magic.
            MLog.i(TAG, "Emails sent successfully");

            // Reschedule the alarm
            Scheduler.scheduleNextWake(this);
        }
    }

}
