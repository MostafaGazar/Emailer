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

import com.meg7.emailer.util.Constants;
import com.meg7.emailer.util.Scheduler;

import java.io.IOException;

/**
 * Manage how to do each cycle example.
 *
 * @author Mostafa Gazar
 */
public class EmailerManager extends TaskerManager {

    public static final String FROM_EMAIL = "support@someemail.com";
    public static final String FROM_NAME = "Emailer";
    public static final int MAX_RECIPIENTS_PER_EMAIL = 5;
    public static final int MAX_EMAILS_PER_CYCLE = 25;
    public static final int MAX_EMAILS_PER_DAY = 100;


    public EmailerManager(Context context) {
        super(context);
    }

    @Override
    protected void processCycle(int cycle) {
        // TODO :: Do your thing here.

        // Reschedule the alarm
        if (getCyclesProcessedTodaySoFar() < EmailerManager.MAX_EMAILS_PER_DAY) {
            Scheduler.scheduleNextWake(mContext);
        } else {
            Scheduler.scheduleNextWake(mContext, Constants.THRESHOLD_DAY);
        }
    }

    @Override
    protected int getCyclesCount() {
        try {
            return mContext.getAssets().list("tasks").length;
        } catch (IOException ignore) {
            return 0;
        }
    }

}
