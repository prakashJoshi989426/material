package social.com.sociallibrary.Social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;
import social.com.sociallibrary.R;

/**
 * Created by mind on 31/3/16.
 */
public class TwitterLoginActivity extends Activity {

    public static String TWITTER_KEY = "";
    public static String TWITTER_SECRET_KEY = "";

    TwitterLoginButton twitterLoginButton;
    String TWI_USER_NAME = "";
    String TWI_USER_ID = "";
    String TWI_Image_URL = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_twitter_login);
        TWITTER_KEY = getApplicationContext().getResources().getString(R.string.twitter_key);
        TWITTER_SECRET_KEY = getApplicationContext().getResources().getString(R.string.twitter_secret);
        initcomponents();
    }


    private void initcomponents() {
        try {
            twitterLoginButton = (TwitterLoginButton) findViewById(R.id.login_button);
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET_KEY);
            Fabric.with(this, new Twitter(authConfig));


            twitterLoginButton.setVisibility(View.GONE);

            twitterLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    String userName = result.data.getUserName();
                    TWI_Image_URL = "https://twitter.com/" + userName + "/profile_image?size=original";
                    TWI_USER_NAME = result.data.getUserName().toString();
                    TWI_USER_ID = String.valueOf(result.data.getUserId());
                    System.out.println("id######################" + TWI_USER_ID);
                    System.out.println("id######################" + TWI_USER_NAME);
                    System.out.println("id######################" + TWI_Image_URL);
                    finish();
                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                    Toast.makeText(TwitterLoginActivity.this, "1" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            twitterLoginButton.performClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button.
        try {
            twitterLoginButton.onActivityResult(requestCode, resultCode,
                    data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void finish() {
        try {
            // Create one data intent
            Intent data = new Intent();
            data.putExtra("Twit_Image_URL", TWI_Image_URL);
            data.putExtra("twit_id", TWI_USER_ID);
            data.putExtra("twit_user_name", TWI_USER_NAME);
            setResult(1, data);
            super.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
