package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import com.cmpt276.project.walkinggroupapp.R;


/**
 * Class for the User to log in using proper credentials or to register a new account
 * */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    public static final String PREFERENCE_EMAIL = "saved.email.key";
    public static final String PREFERENCE_PASSWORD = "saved.password.key";
    public static final String PREFERENCE_IS_LOGOUT = "saved.logout.key";

    private Button mLoginButton;
    private Button mRegisterButton;

//    private Button mMapTestButton;
    
    private Button mHelpButton;
    private TextView mForgotPasswordTextView;
    private EditText mPasswordEditText;
    private EditText mEmailEditText;

    private String mPassword;
    private String mEmail;

    private Intent intent;






    private ModelManager modelManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SharedPreferences sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        mEmail = sharedPreferences.getString(PREFERENCE_EMAIL, null);
        mPassword = sharedPreferences.getString(PREFERENCE_PASSWORD,null);
        String savedIsLogout = sharedPreferences.getString(PREFERENCE_IS_LOGOUT, "false");
        
        modelManager = ModelManager.getInstance();
        modelManager.setApiKey(getString(R.string.gerry_apikey));


        //set up all buttons, texViews etc.
        registerViews();


        setupRegisterButton();
        setupLoginButton();
        setupHelpButton();
        setupForgetPasswordTextViewOnClick();


        if (savedIsLogout.equals("false") && mEmail != null && mPassword != null) {
            login();
        }


//        loginRequest();
    }

    public static Intent makeIntent(Context context)
    {
        return new Intent(context, LoginActivity.class);
    }

    private void setupRegisterButton() {
        mRegisterButton = findViewById(R.id.gerry_Register_Button_login);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = RegisterActivity.makeIntent(getApplicationContext());
                startActivity(registerActivity);
            }
        });
    }

    private void setupLoginButton() {
        mLoginButton = findViewById(R.id.gerry_Login_Button_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
//                ProxyBuilder.SimpleCallback<Void> callback = returnedNothing -> loginResponse(returnedNothing);
//                modelManager.login(LoginActivity.this, callback, mEmail, mPassword);
            }
        });
    }

    private void setupHelpButton() {
        mHelpButton = findViewById(R.id.jacky_help_button);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHelp = HelpActivity.makeIntent(LoginActivity.this);
                startActivity(intentHelp);
            }
        });
    }

    private void setupForgetPasswordTextViewOnClick() {
        mForgotPasswordTextView = findViewById(R.id.gerry_ForgotPassword_TextView_login);
        mForgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to ForgotPassword Activity
            }
        });
    }

    private void registerViews() {

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

//        //test map Button
//        mMapTestButton = findViewById(R.id.gerry_Map_Button_login);
//        mMapTestButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //go to test map activity
//                Intent intentMap = new Intent(LoginActivity.this, MapActivity.class);
//                startActivity(intentMap);
//            }
//        });

    }


    private void login() {
        mLoginButton.setEnabled(false);

        Toast.makeText(LoginActivity.this,"Logging in...",Toast.LENGTH_SHORT).show();

        ProxyBuilder.SimpleCallback<Void> onResponseCallback = returnedNothing -> loginSuccessResponse(returnedNothing);
        ProxyBuilder.SimpleCallback<Void> onFailureCallback = returnedNothing -> loginFailResponse(returnedNothing);
        modelManager.login(LoginActivity.this, onResponseCallback, onFailureCallback, mEmail, mPassword);
    }

    private void loginSuccessResponse(Void returnedNothing) {
        Log.w(TAG, "Server replied to login request (no content was expected).");

        Toast.makeText(LoginActivity.this,"Login Success",Toast.LENGTH_SHORT).show();

        SavePreferences();

        intent = MainMenuActivity.makeIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    private void loginFailResponse(Void returnedNothing) {
        mLoginButton.setEnabled(true);

        Toast.makeText(LoginActivity.this,"Login failed, please try again.",Toast.LENGTH_SHORT).show();

    }


//    private void loginResponse(Void returnedNothing) {
//        Log.w(TAG, "Server replied to login request (no content was expected).");
//
//        //login success, save data to preference
//        SavePreferences();
//
//        //go to Main Menu
//        Toast.makeText(LoginActivity.this,"Login Success",Toast.LENGTH_SHORT).show();
//
//        intent = MainMenuActivity.makeIntent(LoginActivity.this);
//        startActivity(intent);
//        finish();
//    }


//    private void loginRequest() {
//        //test creating user
//        //CreateUserTest();
//
//        //If user did not logout -- auto login
//        //Load Saved data from preferences
//        final SharedPreferences sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
//        String savedEmail = sharedPreferences.getString(PREFERENCE_EMAIL, null);
//        String savedPassword = sharedPreferences.getString(PREFERENCE_PASSWORD,null);
//        String savedIsLogout = sharedPreferences.getString(PREFERENCE_IS_LOGOUT, "false");
//        //set mEmail and mPassword since SavePReference() relies on them
//        mEmail = savedEmail;
//        mPassword = savedPassword;
//
//        if(savedIsLogout.equals("false") && savedEmail != null && savedPassword != null) {
//            //login using data from preferences
//            //mLoginButton.setVisibility(View.INVISIBLE);
//            //mRegisterButton.setVisibility(View.INVISIBLE);
//            mPasswordEditText.setText(savedPassword);
//            mEmailEditText.setText(savedEmail);
//            ProxyBuilder.SimpleCallback<Void> callback = returnedNothing -> loginResponse(returnedNothing);
//            modelManager.login(LoginActivity.this, callback, savedEmail, savedPassword);
//        }
//    }


    //save data using shared preferences
    private void SavePreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //clear current data first
        editor.clear();

        //put current email and password to preferences
        editor.putString(PREFERENCE_EMAIL,mEmail);
        editor.putString(PREFERENCE_PASSWORD,mPassword);

        //Assume user does not logout--change this when user preses logout manually
        editor.putString(PREFERENCE_IS_LOGOUT, "false");

        //commit to preference
        editor.commit();

        Log.w(TAG,"save using preferences success");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loginRequest();
    }
}
