package com.cmpt276.project.walkinggroupapp.appactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;

public class ActivityRegister extends AppCompatActivity {

    private Button confirmButton;
    private EditText emailAddress;
    private EditText firstPassword;
    private EditText secondPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupIDs();
        setupHints(emailAddress, firstPassword, secondPassword);
        setupButtonClick();
    }

    private void setupIDs() {
        //Setting up EditTexts to be used later on
        emailAddress = findViewById(R.id.anmol_emailAddressUser);
        firstPassword = findViewById(R.id.anmol_firstPasswordUser);
        secondPassword = findViewById(R.id.anmol_secondPasswordUser);
        confirmButton = findViewById(R.id.anmol_confirmRegister);
    }

    private void setupButtonClick() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password1 = firstPassword.getText().toString();
                String password2 = secondPassword.getText().toString();

                if(password1.equals(password2))
                {
                    String email = emailAddress.getText().toString();
                    //TODO Call Model Manager Use String email for username and use String password1 for password

                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.anmol_passNoMatch, Toast.LENGTH_SHORT )
                            .show();
                }
            }
        });
    }

    private void setupHints(EditText email, EditText pass1, EditText pass2) {
        email.setHint(R.string.anmol_userEmailHint);
        pass1.setHint(R.string.anmol_setPasswordOneHint);
        pass2.setHint(R.string.anmol_setPasswordTwoHint);
    }



}
