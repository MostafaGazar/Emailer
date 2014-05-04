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

package com.meg7.emailer.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meg7.emailer.R;
import com.meg7.emailer.util.Constants;
import com.meg7.emailer.util.ProgressPreferenceUtils;

/**
 * Fragment for showing stats.
 *
 * @author Mostafa Gazar
 */
public class ProgressFragment extends Fragment {

    private static final String ARGS_PROGRESS_PERCENTAGE = "progressPercentage";
    private static final String ARGS_SENT_COUNT = "sentCount";
    private static final String ARGS_FAILED_COUNT = "failedCount";

    private TextView mProgressPercentageTxt;
    private TextView mSentCountTxt;
    private TextView mFailedCountTxt;

    // TODO :: Add live progress listener.

    public static ProgressFragment newInstance(float progressPercentage, int sentCount, int failedCount) {
        ProgressFragment fragment = new ProgressFragment();

        Bundle args = new Bundle();
        args.putFloat(ARGS_PROGRESS_PERCENTAGE, progressPercentage);
        args.putInt(ARGS_SENT_COUNT, sentCount);
        args.putInt(ARGS_FAILED_COUNT, failedCount);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_progress, container, false);

        if (rootView != null) {
            mProgressPercentageTxt = (TextView) rootView.findViewById(R.id.progressPercentageTxt);
            mSentCountTxt = (TextView) rootView.findViewById(R.id.sentCountTxt);
            mFailedCountTxt = (TextView) rootView.findViewById(R.id.failedCountTxt);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateProgress();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mUpdateProgressReceiver,
                new IntentFilter(Constants.ACTION_PROGRESS));
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mUpdateProgressReceiver);
    }

    private BroadcastReceiver mUpdateProgressReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Constants.ACTION_PROGRESS.equals(action)) {
                updateProgress();
            }
        }
    };

    private void updateProgress() {
        float progressPercentage;
        int sentCount;
        int failedCount;

        Bundle args = getArguments();
        if (args == null ||
                !args.containsKey(ARGS_PROGRESS_PERCENTAGE) ||
                !args.containsKey(ARGS_SENT_COUNT) ||
                !args.containsKey(ARGS_FAILED_COUNT)) {
            progressPercentage = ProgressPreferenceUtils.getProgressPercentage(getActivity());
            sentCount = ProgressPreferenceUtils.getSentCount(getActivity());
            failedCount = ProgressPreferenceUtils.getFailedCount(getActivity());
        } else {
            progressPercentage = args.getFloat(ARGS_PROGRESS_PERCENTAGE);
            sentCount = args.getInt(ARGS_SENT_COUNT);
            failedCount = args.getInt(ARGS_FAILED_COUNT);
        }

        // Update views.
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        mProgressPercentageTxt.setText(String.format("%.2f", progressPercentage) + "%");
        mSentCountTxt.setText(String.valueOf(sentCount));
        mFailedCountTxt.setText(String.valueOf(failedCount));
    }

}
