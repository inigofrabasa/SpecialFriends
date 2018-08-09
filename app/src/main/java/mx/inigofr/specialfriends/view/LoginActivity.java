package mx.inigofr.specialfriends.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import mx.inigofr.specialfriends.R;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    boolean isLoggedInFacebook = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        statusLoginFacebook();
        if(isLoggedIn()){
            navigateToHome();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    void statusLoginFacebook(){
        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(loginResult != null){
                    navigateToHome();
                }
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                if(exception !=null){

                }
            }
        });
    }

    boolean isLoggedIn(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedInFacebook = accessToken != null && !accessToken.isExpired();
        if(isLoggedInFacebook){
            return true;
        }

        return false;
    }

    public void navigateToHome(){
        startActivity(new Intent(this, MainActivity.class));
    }

    //   void getFriendList(){
//        GraphRequestBatch batch = new GraphRequestBatch(
//                GraphRequest.newMeRequest(
//                        AccessToken.getCurrentAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(
//                                    JSONObject jsonObject,
//                                    GraphResponse response) {
//                                if(jsonObject != null){
//
//                                }
//                            }
//                        })
//        );
//
//        batch.addCallback(new GraphRequestBatch.Callback() {
//            @Override
//            public void onBatchCompleted(GraphRequestBatch graphRequests) {
//                // Application code for when the batch finishes
//            }
//        });
//        batch.executeAsync();
//    }
}
