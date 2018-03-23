package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;

import com.cmpt276.project.walkinggroupapp.model.ModelManager;

import static com.cmpt276.project.walkinggroupapp.appactivities.LoginActivity.PREFERENCE_EMAIL;
import static com.cmpt276.project.walkinggroupapp.appactivities.LoginActivity.PREFERENCE_IS_LOGOUT;
import static com.cmpt276.project.walkinggroupapp.appactivities.LoginActivity.PREFERENCE_PASSWORD;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Button nextButton;
    private EditText emailAddress;
    private EditText firstPassword;
    private EditText secondPassword;
    private EditText userNameInputed;
    private Spinner teacherOrStudent;
    private String name;
    private String email;
    private String password1;
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PASS = "USER_PASS";
    public static final String USER_NAME = "USER_NAME";

    private ModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        modelManager = ModelManager.getInstance();

        setupIDs();
        setupHints();
        setupButtonClick();
        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.student_or_teacher, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacherOrStudent.setAdapter(adapter);
    }

    private void setupIDs() {
        //Setting up EditTexts to be used later on
        emailAddress = findViewById(R.id.anmol_emailAddressUser);
        firstPassword = findViewById(R.id.anmol_firstPasswordUser);
        secondPassword = findViewById(R.id.anmol_secondPasswordUser);
        userNameInputed = findViewById(R.id.anmol_nameUserInput);
        nextButton = findViewById(R.id.anmol_nextButton);
        teacherOrStudent = findViewById(R.id.anmol_teacherOrStudent);
    }

    private void setupButtonClick() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password1 = firstPassword.getText().toString();
                String password2 = secondPassword.getText().toString();

                if(password1.equals(password2)) {
                    email = emailAddress.getText().toString();
                    name = userNameInputed.getText().toString();
                    String checkTeacherStudent = teacherOrStudent.getSelectedItem().toString();
                    if(checkTeacherStudent.equals("Student")) {
                        Intent intent = RegisterActivityStudent.makeIntent(RegisterActivity.this);
                        intent.putExtra(USER_EMAIL, email);
                        intent.putExtra(USER_PASS, password1);
                        intent.putExtra(USER_NAME, name);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = RegisterActivityParent.makeIntent(RegisterActivity.this);
                        intent.putExtra(USER_EMAIL, email);
                        intent.putExtra(USER_PASS, password1);
                        intent.putExtra(USER_NAME, name);
                        startActivity(intent);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.anmol_passNoMatch, Toast.LENGTH_SHORT )
                            .show();
                }
            }
        });
    }

    private void setupHints() {
        userNameInputed.setHint(R.string.anmol_UserHintName);
        emailAddress.setHint(R.string.anmol_userEmailHint);
        firstPassword.setHint(R.string.anmol_setPasswordOneHint);
        secondPassword.setHint(R.string.anmol_setPasswordTwoHint);
    }

    public static Intent makeIntent(Context context)
    {
        return new Intent(context, RegisterActivity.class);
    }

    private void registerResponse(Void returnedNothing) {
        Log.w(TAG, "Sent server a create Account Request");
        Toast.makeText(RegisterActivity.this,"Account Created",Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //clear current data first
        editor.clear();

        //put current email and password to preferences
        editor.putString(PREFERENCE_EMAIL,email);
        editor.putString(PREFERENCE_PASSWORD,password1);

        //Assume user does not logout--change this when user preses logout manually
        editor.putString(PREFERENCE_IS_LOGOUT, "false");

        //commit to preference
        editor.commit();

        Log.w(TAG,"Saved Login on Account Creation");
        finish();
    }

}
