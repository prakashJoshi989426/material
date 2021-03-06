package social.com.sociallibrary.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class LoginWithTwitter extends Fragment {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    public static String TWITTER_KEY = "";
    public static String TWITTER_SECRET_KEY = "";
    String twitterId, email;

    TwitterLoginButton twitterLoginButton;

    String TWI_USER_NAME = "";
    String TWI_USER_ID = "";
    String TWI_Image_URL = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            /*LoginWithTwitter loginWithTwitter = new LoginWithTwitter();
            mActivity.getFragmentManager().beginTransaction()
                    .add(android.R.id.content, loginWithTwitter).commit();*/

            TWITTER_KEY = getActivity().getResources().getString(R.string.twitter_key);
            TWITTER_SECRET_KEY = getActivity().getResources().getString(R.string.twitter_secret);
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET_KEY);
            Fabric.with(getActivity(), new Twitter(authConfig));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.social_twitter_login, container, false);


        try {

            initcomponents(rootView);

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

                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure

                    Toast.makeText(getActivity(), "1" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }





/*    @Override
    public void onResume() {
        super.onResume();
        System.out.println("on resume");
        initcomponents();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println("on hiddden changed");
        initcomponents();
    }*/

    private void initcomponents(View rootView) {
        try {
            twitterLoginButton = (TwitterLoginButton) rootView.findViewById(R.id.login_button);
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET_KEY);
            Fabric.with(getActivity(), new Twitter(authConfig));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
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

}
