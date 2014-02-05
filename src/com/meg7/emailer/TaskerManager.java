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
        int cycleToProcess = getLastProcessedCycles();

        setLastProcessedCycles(cycleToProcess + 1);// TODO :: Only do if less than max.


        return incrementCyclesProcessedTodaySoFar();
    }

    protected abstract void processCycle(int cycle);

    public int getCyclesProcessedTodaySoFar() {
        return 0;
    }

    public int incrementCyclesProcessedTodaySoFar() {
        return 0;
    }

    public int getLastProcessedCycles() {
        return 0;
    }

    public void setLastProcessedCycles(int cycle) {

    }


}
