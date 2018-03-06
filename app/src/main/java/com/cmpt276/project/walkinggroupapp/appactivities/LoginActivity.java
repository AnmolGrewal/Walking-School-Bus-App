package com.cmpt276.project.walkinggroupapp.appactivities;

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
import retrofit2.Callback;
import retrofit2.Response;

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

    private WGServerProxy proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Build the server proxy
        proxy = ProxyBuilder.getProxy(getString(R.string.gerry_apikey), null);

        //set up all buttons, texViews etc.
        RegisterViews();

        //test creating user
        CreateUser();
    }



    private void response(User user) {
        Log.w(TAG, "Server replied with user: " + user.toString());
        userId = user.getId();
    }


    private void RegisterViews() {



        //login button
        mLoginButton = findViewById(R.id.gerry_Login_Button_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validate provided email and password

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
                //Toast.makeText(LoginActivity.this,"click succes",Toast.LENGTH_SHORT).show();
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

        //obtain list of all users in server
    }

    private void CreateUser() {
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
