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

package com.meg7.emailer.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.meg7.emailer.EmailerManager;
import com.meg7.emailer.R;
import com.meg7.emailer.TaskerHelper;
import com.meg7.emailer.ui.fragment.HeadlessFragment;
import com.meg7.emailer.ui.fragment.ProgressFragment;
import com.meg7.emailer.ui.fragment.SettingsFragment;
import com.meg7.emailer.util.Constants;

/**
 * Main activity, mainly fragments container.
 *
 * @author Mostafa Gazar
 */
public class EmailerActivity extends FragmentActivity implements HeadlessFragment.Callback {

    private static final String TAG_FRAGMENT_HEADLESS = "headless";

    private HeadlessFragment mHeadlessFragment;

    private FragmentsPagerAdapter mFragmentsAdapter;

    private TextView mRunningCycleLbl;
    private ToggleButton mRunTglBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mHeadlessFragment = (HeadlessFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_HEADLESS);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mHeadlessFragment == null) {
            mHeadlessFragment = new HeadlessFragment();
            fragmentManager.beginTransaction().add(mHeadlessFragment, TAG_FRAGMENT_HEADLESS).commit();
        }

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        mFragmentsAdapter = new FragmentsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mFragmentsAdapter);

        mRunningCycleLbl = (TextView) findViewById(R.id.runningCycleLbl);

        mRunTglBtn = (ToggleButton) findViewById(R.id.runTglBtn);
        mRunTglBtn.setChecked(TaskerHelper.isTaskerRunning(this));
        mRunTglBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // Start task.
                    TaskerHelper.startTasker(getApplicationContext());
                } else {
                    // Stop and cancel all planed tasks.
                    TaskerHelper.cancelFutureTasks(getApplicationContext());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateProgress();

        LocalBroadcastManager.getInstance(this).registerReceiver(mProgressReceiver,
                new IntentFilter(Constants.ACTION_PROGRESS));
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mProgressReceiver);
    }

    private BroadcastReceiver mProgressReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Constants.ACTION_PROGRESS.equals(action)) {
                updateProgress();
            }
        }
    };

    private void updateProgress() {
        EmailerManager taskerManager = new EmailerManager(this);
        mRunningCycleLbl.setText(getString(R.string.runningCycle,
                taskerManager.getCyclesProcessedTodaySoFar() + "/" +
                        taskerManager.getLastProcessedCycle() + "/" +
                        taskerManager.getCyclesCount()));
    }

    private class FragmentsPagerAdapter extends FragmentStatePagerAdapter {

        private SettingsFragment mSettingsFragment;
        private ProgressFragment mProgressFragment;

        public FragmentsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

            mSettingsFragment = new SettingsFragment();
            mProgressFragment = new ProgressFragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return mProgressFragment;
                default:
                    return mSettingsFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

}
