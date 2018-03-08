package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;
import com.cmpt276.project.walkinggroupapp.proxy.WGServerProxy;
import retrofit2.Call;

import com.cmpt276.project.walkinggroupapp.R;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private Button mLoginButton;
    private Button mRegisterButton;
    private TextView mForgotPasswordTextView;
    private EditText mPasswordEditText;
    private EditText mEmailEditText;

    private String mPassword;
    private String mEmail;
    private long userId = 0;
    private Boolean mLoginSuccess = false;

    private WGServerProxy proxy;

    private  Intent intent;     ////Remove Jacky Testing ONLY

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Build the server proxy
        proxy = ProxyBuilder.getProxy(getString(R.string.gerry_apikey), null);

        //set up all buttons, texViews etc.
        RegisterViews();

        //test creating user
        CreateUserTest();
    }



    private void response(User user) {
        Log.w(TAG, "Server replied with user: " + user.toString());
    }

    private void loginResponse(Void returnedNothing) {
        Log.w(TAG, "Server replied to login request (no content was expected).");
        mLoginSuccess = true;
        //go to Main Menu
        if(mLoginSuccess) {
            Toast.makeText(LoginActivity.this,"Login Success",Toast.LENGTH_SHORT).show();
        }

    }


    private void response(Void returnedNothing) {
        Log.w(TAG, "Server replied to login request (no content was expected).");
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.gerry_apikey), token);
        intent =  MainMenu_Activity.makeIntnet(getApplicationContext(), token);
        startActivity(intent);

    }


    private void RegisterViews() {

        //login button
        mLoginButton = findViewById(R.id.gerry_Login_Button_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            // Build new user
            User user = new User();
            user.setEmail(mEmail);
            user.setPassword(mPassword);

            // Register for token received:
            ProxyBuilder.setOnTokenReceiveCallback( token -> onReceiveToken(token));

            // Make call
            Call<Void> caller = proxy.login(user);
            ProxyBuilder.callProxy(LoginActivity.this, caller, returnedNothing -> loginResponse(returnedNothing));
                                                                        //sets mLoginSuccess to true if successful
                                                                        //goes to mainMenu as well



            }
        });



        //register button
        mRegisterButton = findViewById(R.id.gerry_Register_Button_login);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to register activity
            }
        });



        //forgot password textView
        mForgotPasswordTextView = findViewById(R.id.gerry_ForgotPassword_TextView_login);
        mForgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to ForgotPassword Activity
                //go to Main Menu
            }
        });



        //password editText
        mPasswordEditText = findViewById(R.id.gerry_Password_EditText_login);
        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() != 0) {

                    mPassword = s.toString();
                }

            }
        });


        //email editText
        mEmailEditText = findViewById(R.id.gerry_Email_EditText_login);
        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() != 0) {

                    mEmail = s.toString();
                }
            }
        });

    }

    private void ValidateUser(String email, String password) {


    }


    private void CreateUserTest() {
        // Build new user
        User user = new User();
        user.setEmail("gerry1@test.com");
        user.setName("Gerry Test1");
        user.setPassword("justtesting");

        // Make call
        Call<User> caller = proxy.createNewUser(user);
        ProxyBuilder.callProxy(LoginActivity.this, caller, returnedUser -> response(returnedUser));

    }
}
