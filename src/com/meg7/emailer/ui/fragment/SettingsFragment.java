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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

    private View mRestoreDefaultsBtn;
    private View mUpdateBtn;


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

            mRestoreDefaultsBtn = rootView.findViewById(R.id.restoreDefaultsBtn);
            mUpdateBtn = rootView.findViewById(R.id.updateBtn);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initWidgets();
    }

    private void initWidgets(){
        Bundle args = getArguments();
        if (args == null) {
            return;
        }

        // Update views.
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        // TODO :: Update views.
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.restoreDefaultsBtn:
                break;
            case R.id.updateBtn:
                break;
        }
    }


}
