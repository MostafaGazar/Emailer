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

/**
 * App global constants.
 *
 * @author Mostafa Gazar
 */
public class Constants {

    // Config
    public static final int CONFIG_MAX_RECIPIENTS_PER_EMAIL = 5;
    public static final int CONFIG_MAX_EMAILS_PER_CYCLE = 25;
    public static final int CONFIG_MAX_EMAILS_PER_DAY = 100;

    // Intents
    public static final String INTENT_ACTION_WAKE_EMAILER_UP = "com.meg7.emailer.action.ACTION_WAKE_EMAILER_UP";

    // Thresholds
    public static final int THRESHOLD_MAX_SEND_ATTEMPTS = 3;
    public static final int THRESHOLD_MIN_PERIOD_BETWEEN_CYCLES = 60 * 60 * 1000;// An hour.

}