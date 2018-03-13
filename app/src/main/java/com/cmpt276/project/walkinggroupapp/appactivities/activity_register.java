package com.cmpt276.project.walkinggroupapp.appactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.cmpt276.project.walkinggroupapp.R;

public class activity_register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Setting up EditTexts to be used later on
        final EditText emailAddress = findViewById(R.id.anmol_emailAddressUser);
        final EditText firstPassword = findViewById(R.id.anmol_firstPasswordUser);
        final EditText secondPassword = findViewById(R.id.anmol_secondPasswordUser);

        setupHints(emailAddress, firstPassword, secondPassword);
    }

    public void setupHints(EditText email, EditText pass1, EditText pass2) {
        email.setHint(R.string.anmol_userEmailHint);
        pass1.setHint("Enter Password Here");
        pass2.setHint("Enter Password Same as Above");
    }

}
