package com.social.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.social.R;

/**
 * Created by mind on 29/3/16.
 */
public class CointainFragment extends BaseFragment implements View.OnClickListener {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_share_login, container, false);
        initcomponents(rootView);
        return rootView;

    }

    private void initcomponents(View rootView) {
        rootView.findViewById(R.id.btn_share).setOnClickListener(this);
        rootView.findViewById(R.id.btn_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_share:
                    //mActivity.pushfragment(true, new LoginFragment());
                    Toast.makeText(getActivity(), "Share Click", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_login:
                    mActivity.pushfragment(true, new LoginFragment());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
