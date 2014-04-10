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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.meg7.emailer.R;
import com.meg7.emailer.TaskerHelper;
import com.meg7.emailer.ui.fragment.HeadlessFragment;
import com.meg7.emailer.ui.fragment.ProgressFragment;
import com.meg7.emailer.ui.fragment.SettingsFragment;

/**
 * Main activity, mainly fragments container.
 *
 * @author Mostafa Gazar
 */
public class EmailerActivity extends FragmentActivity implements HeadlessFragment.Callback {

    private static final String TAG_FRAGMENT_HEADLESS = "headless";

    private HeadlessFragment mHeadlessFragment;

    private FragmentsPagerAdapter mFragmentsAdapter;

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

        mRunTglBtn = (ToggleButton) findViewById(R.id.runTglBtn);
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
