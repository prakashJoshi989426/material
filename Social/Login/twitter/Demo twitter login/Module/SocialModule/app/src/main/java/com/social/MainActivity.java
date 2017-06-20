package com.social;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.social.fragment.CointainFragment;


import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import social.com.sociallibrary.Util.ConnectionDetector;

public class MainActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    public static final String TWITTER_KEY = "V1UVjJsG6rvgaIZCXEKTHVo6c";
    public static final String TWITTER_SECRET = " jgRR5S37o7CD5QdGLkhDjqKmh4CIJrzM3rwpIMO1nOy6baP55C";


    ConnectionDetector connectionDetector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        twitterinitialization();
        // facebookHashKey();
        connectionDetector = new ConnectionDetector(getApplicationContext());


        pushfragment(false, new CointainFragment());

    }

    /**
     * twitter initizalization code automatically generated whenever app register through plugin
     */
    public void twitterinitialization() {
        try {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(this, new Twitter(authConfig));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   /* */

    /**
     * generating the hashkey put the both key whenever you register application on developer facebook
     */
    private void facebookHashKey() {

        try {
            //Toast.makeText(getApplicationContext(), "test method", Toast.LENGTH_LONG).show();
            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo("com.social", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                //Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashCode = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                System.out.println("Print the hashKey for Facebook :" + hashCode);
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

                // syso key:  t+g7W/z17ZqfDHq0W/YjwJ0CAi0=
                // LOg key:   2jmj7l5rSw0yVb/vlWAYkK/YBwk=

                /// ga0RGNYHvNM5d0SLGQfpQWAPGJ8=
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager()
                .findFragmentById(android.R.id.content);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void pushfragment(boolean shouldAdd, Fragment fragment) {

        FragmentManager fragmentManager = MainActivity.this.getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (shouldAdd) {
            ft.addToBackStack(null);
        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setCustomAnimations(
                R.animator.anim_slide_in_left, R.animator.anim_slide_in_right,
                R.animator.anim_slide_in_left1, R.animator.anim_slide_in_right1);

        ft.replace(R.id.container, fragment);


        ft.commit();
    }


}
