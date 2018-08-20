package mx.inigofr.specialfriends.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import mx.inigofr.specialfriends.R;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    Button fb;
    boolean isLoggedInFacebook = false;
    String userName;
    //FrameLayout loggedFBLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //loggedFBLayout = (FrameLayout) findViewById(R.id.fl_login_fb);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        fb = (Button) findViewById(R.id.fb);

        loginButton.setReadPermissions("email");
        statusLoginFacebook();

        GraphRequestBatch batch = new GraphRequestBatch(
                GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(

                                    JSONObject jsonObject,
                                    GraphResponse response) {
                                if(jsonObject != null){
                                    try {
                                        userName = jsonObject.getString("name");
                                        if(isLoggedIn()){
                                            navigateToHome(userName);
                                        } else{

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        })
        );

        batch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch graphRequests) {
                // Application code for when the batch finishes
            }
        });
        batch.executeAsync();
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
                    navigateToHome("");
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

    public void navigateToHome(String name){

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name_user", userName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onClick(View view) {
        if (view == fb) {
            loginButton.performClick();
        }
    }
}
