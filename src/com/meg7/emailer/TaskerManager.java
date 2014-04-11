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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.meg7.emailer.util.Constants;

/**
 * Manage what to do in each cycle.
 *
 * @author Mostafa Gazar
 */
public abstract class TaskerManager {

    protected Context mContext;

    public TaskerManager(Context context) {
        mContext = context;
    }

    public int processCycleAndScheduleNext() {
        int cycleToProcess = getLastProcessedCycle() + 1;

        if (cycleToProcess < getCyclesCount()) {
            processCycle(cycleToProcess);

            setLastProcessedCycle(cycleToProcess);
        }

        return incrementCyclesProcessedTodaySoFar();
    }

    protected abstract void processCycle(int cycle);

    public abstract int getCyclesCount();

    public int getCyclesProcessedTodaySoFar() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).
                getInt(Constants.PREF_CYCLES_TODAY_SO_FAR, 0);
    }

    public int incrementCyclesProcessedTodaySoFar() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        int cyclesTodaySoFar = sharedPreferences.getInt(Constants.PREF_CYCLES_TODAY_SO_FAR, 0);
        cyclesTodaySoFar = cyclesTodaySoFar + 1;

        sharedPreferences.
                edit().
                putInt(Constants.PREF_CYCLES_TODAY_SO_FAR, cyclesTodaySoFar).
                commit();

        return cyclesTodaySoFar;
    }

    public void resetCyclesProcessedTodaySoFar() {
        PreferenceManager.
                getDefaultSharedPreferences(mContext).
                edit().
                putInt(Constants.PREF_CYCLES_TODAY_SO_FAR, 0).
                commit();
    }

    public int getLastProcessedCycle() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).
                getInt(Constants.PREF_LAST_PROCESSED_CYCLE, 0);
    }

    public void setLastProcessedCycle(int cycle) {
        PreferenceManager.
                getDefaultSharedPreferences(mContext).
                edit().
                putInt(Constants.PREF_LAST_PROCESSED_CYCLE, cycle).
                commit();
    }

}
