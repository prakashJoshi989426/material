package com.social.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.social.R;

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        }
    }
}
