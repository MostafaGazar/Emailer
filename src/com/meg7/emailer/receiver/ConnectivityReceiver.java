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

package com.meg7.emailer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.meg7.emailer.service.TaskerService;
import com.meg7.emailer.util.ConnectionHelper;
import com.meg7.emailer.util.MLog;

/**
 * Listening for changes to internet connection and launches Emailer service when available.
 *
 * @author Mostafa Gazar
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    private static final String TAG = ConnectivityReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            MLog.w(TAG, "onReceived :: " + intent);
            return;
        }

        boolean isConnected = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        MLog.d(TAG, "Connectivity state changed, connected :: " +  isConnected);

        // Connected! Let us send those emails.
        if (isConnected) {
            ConnectionHelper.unregisterConnectivityListener(context.getApplicationContext());

            // Start Emailer service.
            final Intent updaterIntent = new Intent(context, TaskerService.class);
            context.startService(updaterIntent);
        }
    }
}
