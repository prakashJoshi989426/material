package com.social;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;


import com.social.fragment.CointainFragment;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.Image;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import social.com.sociallibrary.Util.ConnectionDetector;

public class MainActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener, View.OnClickListener {


    ConnectionDetector connectionDetector;

    // gplus
    private static final String TAG = "GooglePlus";
    private static final int RC_SIGN_IN = 0;
    private static final int DIALOG_PLAY_SERVICES_ERROR = 0;
    private static final String SAVED_PROGRESS = "sign_in_progress";
    public GoogleApiClient mGoogleApiClient;
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;
    private int mSignInError;
    public SignInButton mSignInButton;
    String gplusid = "", gpluspersonname = "", gplusprofileimage = "";


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpluslogininitialize(savedInstanceState);
        // facebookHashKey();
        connectionDetector = new ConnectionDetector(getApplicationContext());
        pushfragment(false, new CointainFragment());

    }

    /**
     * @param savedInstanceState call this method in oncreate
     *                           before that put in XML G+ default button
     */
    private void gpluslogininitialize(Bundle savedInstanceState) {
        try {
            mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
            mSignInButton.setOnClickListener(this);

            if (savedInstanceState != null) {
                mSignInProgress = savedInstanceState.getInt(SAVED_PROGRESS,
                        STATE_DEFAULT);
            }
            mGoogleApiClient = buildGoogleApiClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @return
     */
    private GoogleApiClient buildGoogleApiClient() {
        try {
            return new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API, Plus.PlusOptions.builder().build())
                    .addScope(Plus.SCOPE_PLUS_PROFILE)
                    .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //Log.i(TAG, "onConnected");

        try {

            mSignInButton.setEnabled(false);

            // Retrieve some profile information to personalize our app for the user.
            Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            if (currentUser.hasId()) {
                gplusid = currentUser.getId();
            }

            if (currentUser.hasDisplayName()) {
                gpluspersonname = currentUser.getDisplayName();
            }

            if (currentUser.hasUrl()) {
                gplusprofileimage = currentUser.getUrl();
            }

            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            String personPhotoUrl = currentUser.getImage().getUrl();

            //String[] img=personPhotoUrl.split("'?sz=50'");
            String image = personPhotoUrl.replace("?sz=50", "");


            System.out.println("id======"+gplusid);
            System.out.println("name======"+gpluspersonname);
            System.out.println("profile image======"+image);
            System.out.println("email======"+email);


            //  System.out.println("mgggggg===>" + image);

            /**
             * getting pf profile image thrtough json result
             *//*
             *   String personGooglePlusProfile = currentUser.getUrl();
             *  Image personPhoto = currentUser.getImage();
            String jsonimage = currentUser.getImage().getUrl().toString();
            System.out.println("jsonimage=====>"+jsonimage);
            JSONArray jsonArray = new JSONArray("[" + jsonimage + "]");
            JSONObject jo;
            for (int i = 0; i < jsonArray.length(); i++) {

                jo = jsonArray.getJSONObject(i);


                gplusprofileimage = (jo.getString("url"));
            }*/


            if (gplusid.equalsIgnoreCase("") || gplusid == null) {

            } else {

                // send broadcast  in LOginActivity


                Intent intent = new Intent("Googleplus");
                intent.putExtra("id", gplusid);
                intent.putExtra("email", email);
                intent.putExtra("name", gpluspersonname);
                intent.putExtra("image", image);

                sendBroadcast(intent);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        mSignInProgress = STATE_DEFAULT;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        try {


            Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + result.getErrorCode());

            if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {

            } else if (mSignInProgress != STATE_IN_PROGRESS) {
                mSignInIntent = result.getResolution();
                mSignInError = result.getErrorCode();

                if (mSignInProgress == STATE_SIGN_IN) {
                    resolveSignInError();
                }
            }
            onSignedOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is called in the gplus button o click
     */
    public void resolveSignInError() {
        try {

            if (mSignInIntent != null) {
                try {
                    mSignInProgress = STATE_IN_PROGRESS;
                    startIntentSenderForResult(mSignInIntent.getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "Sign in intent could not be sent: " + e.getLocalizedMessage());
                    mSignInProgress = STATE_SIGN_IN;
                    mGoogleApiClient.connect();
                }
            } else {

                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }
                onSignedOut();

                makeDialog(DIALOG_PLAY_SERVICES_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSignedOut() {
        try {
            mSignInButton.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int cause) {
        try {
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeDialog(int id) {
        try {

            switch (id) {
                case DIALOG_PLAY_SERVICES_ERROR:

                    if (mSignInProgress == STATE_SIGN_IN) {
                        resolveSignInError();
                    }


                default:
                    return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // .............gooogle-plus............................
    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        try {
            savedState.putInt(SAVED_PROGRESS, mSignInProgress);
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

        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    mSignInProgress = STATE_SIGN_IN;
                } else {
                    mSignInProgress = STATE_DEFAULT;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;
        }



        /*Fragment fragment = getFragmentManager()
                .findFragmentById(android.R.id.content);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }*/
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
