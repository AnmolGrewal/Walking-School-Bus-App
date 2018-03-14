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


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String PREFERENCE_EMAIL = "saved.email.key";
    private static final String PREFERENCE_PASSWORD = "saved.password.key";
    private static final String PREFERENCE_IS_LOGOUT = "saved.logout.key";

    private Button mLoginButton;
    private Button mRegisterButton;
    private Button mMapTestButton;
    private TextView mForgotPasswordTextView;
    private EditText mPasswordEditText;
    private EditText mEmailEditText;

    private String mPassword;
    private String mEmail;
//    private long userId = 0;

    private Intent intent;

//    private WGServerProxy proxy;




    private ModelManager modelManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        modelManager = ModelManager.getInstance();
        modelManager.setApiKey(getString(R.string.gerry_apikey));

        // Build the server proxy--used for logging in
//        proxy = ProxyBuilder.getProxy(getString(R.string.gerry_apikey), null);

        //set up all buttons, texViews etc.
        RegisterViews();

        //test creating user
        //CreateUserTest();

        //If user did not logout -- auto login
        //Load Saved data from preferences
        final SharedPreferences sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString(PREFERENCE_EMAIL, " ");
        String savedPassword = sharedPreferences.getString(PREFERENCE_PASSWORD," ");
        String savedIsLogout = sharedPreferences.getString(PREFERENCE_IS_LOGOUT, "false");
        //set mEmail and mPassword since SavePReference() relies on them
        mEmail = savedEmail;
        mPassword = savedPassword;

        if(savedIsLogout.equals("false") && !savedEmail.equals(" ") && !savedPassword.equals(" ")) {
            //login using data from preferences
//            Login(savedEmail,savedPassword);
            mPasswordEditText.setText(savedPassword);
            mEmailEditText.setText(savedEmail);
            ProxyBuilder.SimpleCallback<Void> callback = returnedNothing -> loginResponse(returnedNothing);
            modelManager.login(LoginActivity.this, callback, savedEmail, savedPassword);
        }
    }






    private void loginResponse(Void returnedNothing) {
        Log.w(TAG, "Server replied to login request (no content was expected).");

        //login success, save data to preference
        SavePreferences();

        //go to Main Menu
        Toast.makeText(LoginActivity.this,"Login Success",Toast.LENGTH_SHORT).show();

//        ProxyBuilder.SimpleCallback<Void> callback = nothing -> updateUserResponse(nothing);
//        modelManager.updateUser(callback);

//        modelManager.updateUser();


        intent = MainMenuActivity.makeIntent(LoginActivity.this);
        startActivity(intent);
    }

//    private void updateUserResponse(Void nothing) {
//        intent = MainMenuActivity.makeIntent(LoginActivity.this);
//        startActivity(intent);
//    }

//    private void onReceiveToken(String token) {
//        // Replace the current proxy with one that uses the token!--to enable server data access
//        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
//        proxy = ProxyBuilder.getProxy(getString(R.string.gerry_apikey), token);
//        intent = MainMenuActivity.makeIntent(this, token);
//    }





    private void RegisterViews() {

        //login button
        mLoginButton = findViewById(R.id.gerry_Login_Button_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //login using data from editTexts
//                Login(mEmail,mPassword);
                ProxyBuilder.SimpleCallback<Void> callback = returnedNothing -> loginResponse(returnedNothing);
                modelManager.login(LoginActivity.this, callback, mEmail, mPassword);

            }
        });

        //register button
        mRegisterButton = findViewById(R.id.gerry_Register_Button_login);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to register activity
                Intent registerActivity = RegisterActivity.makeIntent(getApplicationContext());
                startActivity(registerActivity);
            }
        });

        //forgot password textView
        mForgotPasswordTextView = findViewById(R.id.gerry_ForgotPassword_TextView_login);
        mForgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to ForgotPassword Activity
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

        //test map Button
        mMapTestButton = findViewById(R.id.gerry_Map_Button_login);
        mMapTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to test map activity
                Intent intentMap = new Intent(LoginActivity.this, MapActivity.class);
                startActivity(intentMap);
            }
        });
    }





//   private void Login(String email, String password) {
//       // Build new user
//       User user = new User();
//       user.setEmail(email);
//       user.setPassword(password);
//
//       // Register for token received:
//       ProxyBuilder.setOnTokenReceiveCallback( token -> onReceiveToken(token));
//
//       // Make call
//       Call<Void> caller = proxy.login(user);
//       ProxyBuilder.callProxy(LoginActivity.this, caller, returnedNothing -> loginResponse(returnedNothing));
//   }




//    private void CreateUserTest() {
//        // Build new user
//        User user = new User();
//        user.setEmail("gerry1@test.com");
//        user.setName("Gerry Test1");
//        user.setPassword("justtesting");
//
//        // Make call
//        Call<User> caller = proxy.createNewUser(user);
//        ProxyBuilder.callProxy(LoginActivity.this, caller, returnedUser -> response(returnedUser));
//    }

//    private void response(User user) {
//        Log.w(TAG, "Server replied with user: " + user.toString());
//    }

    public static Intent makeIntent(Context context)
    {
        return new Intent(context, LoginActivity.class);
    }


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
}
