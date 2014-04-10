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

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by mostafagazar on 2/18/14.
 */
public class ProgressPreferenceUtils {

    private static final String PREF_PROGRESS_PERCENTAGE = "progressPercentage";
    private static final String PREF_SENT_COUNT = "sentCount";
    private static final String PREF_FAILED_COUNT = "failedCount";

    public static float getProgressPercentage(Context context) {
        return getPrefValue(context, PREF_PROGRESS_PERCENTAGE, 0f);
    }
    public static void setProgressPercentage(Context context,  float progressPercentage) {
        setPrefValue(context, PREF_PROGRESS_PERCENTAGE, progressPercentage);
    }

    public static int getSentCount(Context context) {
        return getPrefValue(context, PREF_SENT_COUNT, 0);
    }
    public static void setSentCount(Context context,  int sentCount) {
        setPrefValue(context, PREF_SENT_COUNT, sentCount);
    }
    public static void incrementSentCount(Context context){
        setPrefValue(context, PREF_SENT_COUNT,
                getPrefValue(context, PREF_SENT_COUNT, 0) + 1);
    }

    public static int getFailedCount(Context context) {
        return getPrefValue(context, PREF_FAILED_COUNT, 0);
    }
    public static void setFailedCount(Context context,  int failedCount) {
        setPrefValue(context, PREF_FAILED_COUNT, failedCount);
    }
    public static void incrementFailedCount(Context context){
        setPrefValue(context, PREF_FAILED_COUNT,
                getPrefValue(context, PREF_FAILED_COUNT, 0) + 1);
    }

    private static int getPrefValue(Context context, String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defaultValue);
    }
    private static float getPrefValue(Context context, String key, float defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(key, defaultValue);
    }

    private static void setPrefValue(Context context, String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(key, value)
                .commit();
    }
    private static void setPrefValue(Context context, String key, float value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putFloat(key, value)
                .commit();
    }
}
