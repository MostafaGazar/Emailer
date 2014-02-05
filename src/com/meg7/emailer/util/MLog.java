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

import android.util.Log;

/**
 * Normal log with ON/OFF flag.
 *
 * @author Mostafa Gazar
 */
public class MLog {

	static final boolean LOG = true;

    public static void i(String tag, String log) {
        if (LOG) { Log.i(tag, log); }
    }

	public static void d(String TAG, String log) {
		if (LOG) { Log.d(TAG, log); }
	}

	public static void v(String TAG, String log) {
		if (LOG) { Log.v(TAG, log); }
	}

    public static void w(String TAG, String log) {
        if (LOG) { Log.w(TAG, log); }
    }

	public static void e(String TAG, String log) {
		if (LOG) { Log.e(TAG, log); }
	}

	public static void e(String TAG, String log, Exception ex) {
		if (LOG) { Log.e(TAG, log, ex); }
	}

}
