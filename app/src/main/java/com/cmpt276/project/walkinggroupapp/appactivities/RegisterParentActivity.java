package com.cmpt276.project.walkinggroupapp.appactivities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import static com.cmpt276.project.walkinggroupapp.appactivities.LoginActivity.PREFERENCE_EMAIL;
import static com.cmpt276.project.walkinggroupapp.appactivities.LoginActivity.PREFERENCE_IS_LOGOUT;
import static com.cmpt276.project.walkinggroupapp.appactivities.LoginActivity.PREFERENCE_PASSWORD;
import static com.cmpt276.project.walkinggroupapp.appactivities.RegisterActivity.USER_EMAIL;
import static com.cmpt276.project.walkinggroupapp.appactivities.RegisterActivity.USER_NAME;
import static com.cmpt276.project.walkinggroupapp.appactivities.RegisterActivity.USER_PASS;

public class RegisterParentActivity extends AppCompatActivity {

    private String name;
    private String email;
    private String password1;
    private Integer birthYear;
    private Integer birthMonth;
    private String address;
    private String cellPhone;
    private String homePhone;
    private String grade;
    private String teacherName;
    private String emergencyContactInfo;

    private Spinner userBirthMonth;
    private EditText userBirthYear;
    private EditText userHomePhoneNumber;
    private EditText userCellPhoneNumber;
    private EditText userAddress;
    private Button userRegister;

    private ModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_parent);

        modelManager = ModelManager.getInstance();

        setupIds();
        setupHints();
        setupMonths();

        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userBirthMonth.getSelectedItemPosition() != 12) {
                    int spinner_pos = userBirthMonth.getSelectedItemPosition();
                    String[] month_values = getResources().getStringArray(R.array.months_values);
                    birthMonth = Integer.valueOf(month_values[spinner_pos]);
                } else {
                    birthMonth = null;
                }
                String temp = userBirthYear.getText().toString().trim();
                try {
                    birthYear = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    birthYear = null;
                }
                Intent intent = getIntent();
                name = intent.getStringExtra(USER_NAME);
                email = intent.getStringExtra(USER_EMAIL);
                password1 = intent.getStringExtra(USER_PASS);
                //Get Birth Month
                int spinner_pos = userBirthMonth.getSelectedItemPosition();
                String[] month_values = getResources().getStringArray(R.array.months_values);
                birthMonth = Integer.valueOf(month_values[spinner_pos]);
                address = userAddress.getText().toString().trim();
                cellPhone = userCellPhoneNumber.getText().toString().trim();
                homePhone = userHomePhoneNumber.getText().toString().trim();
                grade = null;
                teacherName = null;
                emergencyContactInfo = null;
                ProxyBuilder.SimpleCallback<Void> callback = returnedNothing -> registerResponse(returnedNothing);
                modelManager.register(RegisterParentActivity.this, callback, name, email, password1,
                        birthYear, birthMonth, address, cellPhone, homePhone, grade, teacherName, emergencyContactInfo);
            }
        });
    }

    private void setupHints() {
        userAddress.setHint("#1 big house way, Surrey BC, H0H 0H0, Canada");
        userBirthYear.setHint("2005");
        userHomePhoneNumber.setHint("(604) 123-4567");
        userCellPhoneNumber.setHint("+1.778.098.7765");
    }

    private void setupIds() {
        userBirthMonth = findViewById(R.id.anmol_monthBornUserP);
        userBirthYear = findViewById(R.id.anmol_yearBornUserP);
        userHomePhoneNumber = findViewById(R.id.anmol_homePhoneUserP);
        userCellPhoneNumber = findViewById(R.id.anmol_cellPhoneUserP);
        userAddress = findViewById(R.id.anmol_addressUserP);
        userRegister = findViewById(R.id.anmol_registerP);
    }

    private void setupMonths() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userBirthMonth.setAdapter(adapter);
        userBirthMonth.setSelection(12);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, RegisterParentActivity.class);
    }

    private void registerResponse(Void returnedNothing) {
        Toast.makeText(RegisterParentActivity.this,"Account Created",Toast.LENGTH_SHORT).show();

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

        finishAffinity();
        Intent intent = LoginActivity.makeIntent(RegisterParentActivity.this);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

