package com.social.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.social.MainActivity;

import social.com.sociallibrary.Util.ConnectionDetector;

/**
 * Created by mind on 29/3/16.
 */
public class BaseFragment extends Fragment {

    public MainActivity mActivity;
    public View rootView;

    ConnectionDetector connectionDetector;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        connectionDetector = new ConnectionDetector(getActivity());


    }


}
