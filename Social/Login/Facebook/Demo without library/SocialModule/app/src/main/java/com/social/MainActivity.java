package com.social;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.social.Util.ConnectionDetector;
import com.social.fragment.CointainFragment;
import com.social.fragment.LoginFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    ConnectionDetector connectionDetector;


    // FB

    private LoginManager loginManager;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;

    private String user_FB_id = "";
    private String user_FB_email = "";
    private String user_Lname = "";
    private String Image_URL = "";
    private String user_Fname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        facebookHashKey();
        connectionDetector = new ConnectionDetector(getApplicationContext());

        facebbokinitialize();

        pushfragment(false, new CointainFragment());

    }


    /**
     * generating the hashkey put the both key whenever you register application on developer facebook
     */
    private void facebookHashKey() {

        try {
            //Toast.makeText(getApplicationContext(), "test method", Toast.LENGTH_LONG).show();
            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo("com.app.bhojdeals", PackageManager.GET_SIGNATURES);
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


    /**
     * initialization of facebook
     */
    private void facebbokinitialize() {
        try {
            if (connectionDetector.isConnectingToInternet()) {
                FacebookSdk.sdkInitialize(getApplicationContext());
                callbackManager = CallbackManager.Factory.create();
            } else {
                //    Methods.toast(Commonmessage.noInternet, MainActivity.this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void facebookloginclick(final Context context) {

        if (connectionDetector.isConnectingToInternet()) {
            try {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please wait");
                progressDialog.show();
                loginManager = LoginManager.getInstance();

                //isfacebook = true;
                loginManager.registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {

                            @Override
                            public void onSuccess(final LoginResult loginResult) {
                                // App code


                                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                                    @Override
                                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                        progressDialog.hide();
                                        System.out.println("JO >>>>>>>>> " + jsonObject);
                                        System.out.println("GR +++++++++ " + graphResponse);
                                        if (jsonObject != null) {
                                            try {
                                                user_FB_id = jsonObject.getString("id");
                                                user_FB_email = jsonObject.getString("email");
                                                user_Fname = jsonObject.getString("first_name");
                                                user_Lname = jsonObject.getString("last_name");

                                                Image_URL = "http://graph.facebook.com/" + user_FB_id + "/picture?type=square";

                                                if (user_FB_email != null && !user_FB_email.isEmpty()) {
                                                    // CheckResponse.callSignInApi(mActivity, user_FB_email,null, WebApi.TYPE_FACEBBOK, user_FB_id);
                                                    // Toast.makeText(getApplicationContext(), "" + user_FB_email, Toast.LENGTH_SHORT).show();

                                                    System.out.println("id===" + user_FB_id);
                                                    System.out.println("email===" + user_FB_email);
                                                    System.out.println("first===" + user_Fname);
                                                    System.out.println("user===" + user_Lname);
                                                    System.out.println("image===" + Image_URL);
                                                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                                    //  facebookapi(user_FB_email, user_FB_id, Login_type);
                                                } else {
                                                    //        System.out.println("===========ERROR============");
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        } else {
                                            System.out.println("facebook else");
                                        }
                                        // Application code
                                        //Log.v("LoginActivity", graphResponse.toString());
                                    }

                                });

                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id, first_name, last_name, email");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel() {
                                // App code
                                //   System.out.println("on cancel");

                                progressDialog.hide();
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                //      System.out.println("on error");
                                progressDialog.hide();
                            }
                        });

                List<String> permissionNeeds = Arrays.asList("public_profile", "email");
                loginManager.logInWithReadPermissions(MainActivity.this, permissionNeeds);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.hide();
            }
        } else {
            Toast.makeText(context, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
       /* if (shouldAdd) {
            ft.add(R.id.container, fragment, fragment.getClass().getCanonicalName());
            if (MainActivity.this.getFragmentManager().findFragmentById(R.id.container) != null) {
                ft.hide(MainActivity.this.getFragmentManager().findFragmentById(R.id.container));
            }
        } else {
            //System.out.println("else");
            //clearBackStack();

            //ft.replace(id, fragment, fragment.getClass().getCanonicalName());
        }*/

        ft.commit();
    }


}
