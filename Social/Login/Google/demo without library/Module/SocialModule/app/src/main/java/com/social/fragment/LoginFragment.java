package com.social.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.plus.Plus;
import com.social.R;

import social.com.sociallibrary.Social.FaceBookLoginActivity;
import social.com.sociallibrary.Social.TwitterLoginActivity;


public class LoginFragment extends BaseFragment implements View.OnClickListener {

    int FACEBOOK = 100;
    int TWITTER = 200;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().registerReceiver(Googleplus, new IntentFilter("Googleplus"));
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        return rootView;
    }

    BroadcastReceiver Googleplus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String Gemail = intent.getExtras().getString("email");
                String GName = intent.getExtras().getString("name");
                Toast.makeText(getActivity(), "Email-==>" + Gemail, Toast.LENGTH_SHORT).show();
                System.out.println("googleemail=" + Gemail + "==name=" + GName);
                // SignUp_Google_Api();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

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

    /**
     * unregister the receiver
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(Googleplus); // unregister broadcast
    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.rl_facebook:
                    try {
                        Intent intent = new Intent(getActivity(), FaceBookLoginActivity.class);
                        startActivityForResult(intent, FACEBOOK);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btn_facebook:
                    try {
                        Intent intent = new Intent(getActivity(), FaceBookLoginActivity.class);
                        startActivityForResult(intent, FACEBOOK);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.rl_twitter:
                    try {
                        Intent intent = new Intent(getActivity(), TwitterLoginActivity.class);
                        startActivityForResult(intent, TWITTER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.btn_twitter:
                    try {
                        Intent intent = new Intent(getActivity(), TwitterLoginActivity.class);
                        startActivityForResult(intent, TWITTER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.rl_gplus:


                    gplusclick();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gplusclick() {
        try {
            if (connectionDetector.isConnectingToInternet()) {
                if (mActivity.mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mActivity.mGoogleApiClient);
                    mActivity.mGoogleApiClient.disconnect();
                    mActivity.mGoogleApiClient.connect();
                }
                /**
                 *  call the method from MainActivity
                 */
                try {
                    mActivity.resolveSignInError();
                    //System.out.println("googleplus-call");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && requestCode == FACEBOOK) {
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
        } else if (resultCode == 1 && requestCode == TWITTER) {
            String image_url = "", twit_id = "", twit_user_name = "";
            if (data.hasExtra("Twit_Image_URL")) {
                image_url = data.getExtras().getString("Twit_Image_URL");
            }
            if (data.hasExtra("twit_id")) {
                twit_id = data.getExtras().getString("twit_id");
            }
            if (data.hasExtra("twit_user_name")) {
                twit_user_name = data.getExtras().getString("twit_user_name");
            }
            System.out.println("id onmy activit yresult=====>" + twit_id);
            System.out.println("name onmy activit yresult=====>" + twit_user_name);
            System.out.println("image url onmy activit yresult=====>" + image_url);
        } else {

        }
    }
}
