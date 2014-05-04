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

package com.meg7.emailer.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.meg7.emailer.service.TaskerService;

import java.util.Calendar;

/**
 * Manage scheduled Emailer wakes.
 *
 * @author Mostafa Gazar
 */
public class Scheduler {

    private static final String TAG = Scheduler.class.getSimpleName();

    public static void scheduleNextWake(Context context) {
        scheduleNextWake(context, Constants.THRESHOLD_MIN_PERIOD_BETWEEN_CYCLES);
    }

    public static void scheduleNextWake(Context context, int threshold) {
        // Cancel all previously scheduled wakes.
        cancelScheduledWakes(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, threshold);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getEmailerWakeIntent(context));

        MLog.d(TAG, "Emailer scheduled in " + threshold / (60 * 1000) + " minutes");
    }

    public static void cancelScheduledWakes(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getEmailerWakeIntent(context));
    }

    private static PendingIntent getEmailerWakeIntent(Context context) {
        Intent intent = new Intent(context, TaskerService.class);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
