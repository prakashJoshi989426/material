package social.com.sociallibrary.Social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import social.com.sociallibrary.Util.ConnectionDetector;

/**
 * Created by mind on 29/3/16.
 */
public class FaceBookLoginActivity extends Activity {

    ConnectionDetector connectionDetector;

    private LoginManager loginManager;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;

    private String user_FB_id = "";
    private String user_FB_email = "";
    private String user_Lname = "";
    private String Image_URL = "";
    private String user_Fname = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            connectionDetector = new ConnectionDetector(getApplicationContext());
            facebbokinitialize();
            facebookloginclick(FaceBookLoginActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
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
        try {
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

                                                        finish();


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
                    loginManager.logInWithReadPermissions(FaceBookLoginActivity.this, permissionNeeds);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.hide();
                }
            } else {
                Toast.makeText(context, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void finish() {
        try {
            // Create one data intent
            Intent data = new Intent();
            data.putExtra("Image_URL", Image_URL);
            data.putExtra("user_FB_email", user_FB_email);
            data.putExtra("user_FB_id", user_FB_id);
            data.putExtra("user_Fname", user_Fname);
            data.putExtra("user_Lname", user_Lname);
            setResult(1, data);
            super.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
