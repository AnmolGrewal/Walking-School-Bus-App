package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;

public class RegisterActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                email = emailAddress.getText().toString();
                name = userNameInputed.getText().toString();
                if(name.length() >= 1 && email.length() >= 1) {
                    if(password1.equals(password2)) {
                        String checkTeacherStudent = teacherOrStudent.getSelectedItem().toString();
                        if(checkTeacherStudent.equals("Student")) {
                            Intent intent = RegisterStudentActivity.makeIntent(RegisterActivity.this);
                            intent.putExtra(USER_EMAIL, email);
                            intent.putExtra(USER_PASS, password1);
                            intent.putExtra(USER_NAME, name);
                            startActivity(intent);
                        } else {
                            Intent intent = RegisterParentActivity.makeIntent(RegisterActivity.this);
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
                } else {
                    Toast.makeText(RegisterActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
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

}
