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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.meg7.emailer.EmailerManager;
import com.meg7.emailer.R;

/**
 * Fragment for showing editing email template before execution.
 *
 * @author Mostafa Gazar
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private EditText mUsernameEdt;
    private EditText mPasswordEdt;
    private EditText mFromEmailEdt;
    private EditText mFromNameEdt;
    private EditText mSubjectEdt;
    private EditText mMessageEdt;

    public static ProgressFragment newInstance() {
        ProgressFragment fragment = new ProgressFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        if (rootView != null) {
            mUsernameEdt = (EditText) rootView.findViewById(R.id.usernameEdt);
            mPasswordEdt = (EditText) rootView.findViewById(R.id.passwordEdt);
            mFromEmailEdt = (EditText) rootView.findViewById(R.id.fromEmailEdt);
            mFromNameEdt = (EditText) rootView.findViewById(R.id.fromNameEdt);
            mSubjectEdt = (EditText) rootView.findViewById(R.id.subjectEdt);
            mMessageEdt = (EditText) rootView.findViewById(R.id.messageEdt);

            // Update views.
            Context context = getActivity();
            mUsernameEdt.setText(EmailerManager.PreferenceUtils.getEmailUsername(context));
            mPasswordEdt.setText(EmailerManager.PreferenceUtils.getEmailPassword(context));
            mFromEmailEdt.setText(EmailerManager.PreferenceUtils.getFromEmail(context));
            mFromNameEdt.setText(EmailerManager.PreferenceUtils.getFromName(context));
            mSubjectEdt.setText(EmailerManager.PreferenceUtils.getEmailSubject(context));
            mMessageEdt.setText(EmailerManager.PreferenceUtils.getEmailMessage(context));

            View restoreDefaultsBtn = rootView.findViewById(R.id.restoreDefaultsBtn);
            restoreDefaultsBtn.setOnClickListener(this);
            View updateBtn = rootView.findViewById(R.id.updateBtn);
            updateBtn.setOnClickListener(this);
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.restoreDefaultsBtn:
                restoreDefaultSettings();
                break;
            case R.id.updateBtn:
                updateSettings();
                break;
        }
    }

    private void updateSettings() {
        Context context = getActivity();
        EmailerManager.PreferenceUtils.setEmailUsername(context, mUsernameEdt.getText().toString());
        EmailerManager.PreferenceUtils.setEmailPassword(context, mPasswordEdt.getText().toString());
        EmailerManager.PreferenceUtils.setFromEmail(context, mFromEmailEdt.getText().toString());
        EmailerManager.PreferenceUtils.setFromName(context, mFromNameEdt.getText().toString());
        EmailerManager.PreferenceUtils.setEmailSubject(context, mSubjectEdt.getText().toString());
        EmailerManager.PreferenceUtils.setEmailMessage(context, mMessageEdt.getText().toString());
    }

    private void restoreDefaultSettings() {
        mUsernameEdt.setText(getString(R.string.default_email));
        mPasswordEdt.setText(getString(R.string.default_password));
        mFromEmailEdt.setText(getString(R.string.default_from_email));
        mFromNameEdt.setText(getString(R.string.default_from_name));
        mSubjectEdt.setText(getString(R.string.default_subject));
        mMessageEdt.setText(getString(R.string.default_message));
    }

}
