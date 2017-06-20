package com.social.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.social.R;

/**
 * Created by mind on 29/3/16.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initcomponents();
    }

    private void initcomponents() {
        try {
            rootView.findViewById(R.id.rl_facebook).setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.rl_facebook:
                    mActivity.facebookloginclick(getActivity());
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
