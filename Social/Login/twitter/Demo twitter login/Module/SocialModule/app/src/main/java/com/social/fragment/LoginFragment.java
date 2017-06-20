package com.social.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.social.R;
import com.social.social.LoginWithTwitter;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;
import social.com.sociallibrary.Social.FaceBookLoginActivity;


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
            rootView.findViewById(R.id.btn_facebook).setOnClickListener(this);

            rootView.findViewById(R.id.rl_gplus).setOnClickListener(this);
            rootView.findViewById(R.id.btn_gplus).setOnClickListener(this);

            rootView.findViewById(R.id.rl_twitter).setOnClickListener(this);
            rootView.findViewById(R.id.btn_twitter).setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.rl_facebook:
                    try {
                        Intent intent = new Intent(getActivity(), FaceBookLoginActivity.class);
                        startActivityForResult(intent, 100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btn_facebook:
                    try {
                        Intent intent = new Intent(getActivity(), FaceBookLoginActivity.class);
                        startActivityForResult(intent, 100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.rl_twitter:
                    try {
                        twitterclick();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void twitterclick() {
        try {

            if (connectionDetector.isConnectingToInternet()) {
                TwitterAuthConfig authConfig = new TwitterAuthConfig(mActivity.TWITTER_KEY, mActivity.TWITTER_SECRET);
                Fabric.with(getActivity(), new Twitter(authConfig));


                /*  push the fragment into Login For twitter and used default android container
                  no Need to take container in Xml
                */
                LoginWithTwitter loginWithTwitter = new LoginWithTwitter();
                mActivity.getFragmentManager().beginTransaction()
                        .add(android.R.id.content, loginWithTwitter).commit();
            } else {
                Toast.makeText(getActivity(), "No internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 1 && requestCode == 100) {
            if (data.hasExtra("Image_URL") && data.hasExtra("user_FB_email") && data.hasExtra("user_FB_id") && data.hasExtra("user_Fname") && data.hasExtra("user_Lname")) {
                String imageurl = data.getExtras().getString("Image_URL");
                String user_FB_email = data.getExtras().getString("user_FB_email");
                String user_FB_id = data.getExtras().getString("user_FB_id");
                String user_Lname = data.getExtras().getString("user_Lname");
                String user_Fname = data.getExtras().getString("user_Fname");

                System.out.println("my activity result@@@@@user_FB_email@@@@@@" + user_FB_email);
                System.out.println("my activity result@@@@@@imageurl@@@@@" + imageurl);
                System.out.println("my activity result@@@@@user_FB_id@@@@@@" + user_FB_id);
                System.out.println("my activity result@@user_Lname@@@@@@@@@" + user_Lname);
                System.out.println("my activity result@@@@@user_Fname@@@@@@" + user_Fname);
            }
        } else {


        }
    }
}
