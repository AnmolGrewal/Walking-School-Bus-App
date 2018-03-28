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
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import static com.cmpt276.project.walkinggroupapp.appactivities.LoginActivity.PREFERENCE_EMAIL;
import static com.cmpt276.project.walkinggroupapp.appactivities.LoginActivity.PREFERENCE_IS_LOGOUT;
import static com.cmpt276.project.walkinggroupapp.appactivities.LoginActivity.PREFERENCE_PASSWORD;
import static com.cmpt276.project.walkinggroupapp.appactivities.RegisterActivity.USER_EMAIL;
import static com.cmpt276.project.walkinggroupapp.appactivities.RegisterActivity.USER_NAME;
import static com.cmpt276.project.walkinggroupapp.appactivities.RegisterActivity.USER_PASS;

public class EditOwnProfilePart2 extends AppCompatActivity {

    private String name;
    private String email;
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
    private EditText userGrade;
    private EditText userTeacherName;
    private EditText userEmergencyContactInfo;

    User currentUser;
    private ModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        modelManager = ModelManager.getInstance();

        setupIds();
        setupHints();
        setupMonths();

        ProxyBuilder.SimpleCallback<User> getCurrentUser = monitoredByUsers -> setupUserInfo(monitoredByUsers);
        modelManager.getUser(EditOwnProfilePart2.this, getCurrentUser);

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
                //Get Birth Month

                address = userAddress.getText().toString().trim();
                cellPhone = userCellPhoneNumber.getText().toString().trim();
                homePhone = userHomePhoneNumber.getText().toString().trim();
                grade = userGrade.getText().toString().trim();
                teacherName = userTeacherName.getText().toString().trim();
                emergencyContactInfo = userEmergencyContactInfo.getText().toString().trim();
                //TODO: NEED HELP HERE JUSTIN THANKS!
                ProxyBuilder.SimpleCallback<User> callback = setupNewInformation -> editInformation(setupNewInformation);
                modelManager.editUser(EditOwnProfilePart2.this, callback, name, email,
                            birthYear, birthMonth, address, cellPhone, homePhone, grade, teacherName, emergencyContactInfo);
            }
        });
    }

    private void editInformation(User monitoredByUsers) {
        Toast.makeText(EditOwnProfilePart2.this,"Account Edited",Toast.LENGTH_SHORT).show();

        /*
        Intent intent = LoginActivity.makeIntent(EditOwnProfilePart2.this);
        startActivity(intent);
        */
    }

    private void setupUserInfo(User currentPulledUser) {
            try {
                userBirthMonth.setSelection(currentUser.getBirthMonth() - 1);
                userBirthYear.setText(currentUser.getBirthYear());
                userAddress.setText(currentUser.getAddress());
                userCellPhoneNumber.setText(currentUser.getCellPhone());
                userEmergencyContactInfo.setText(currentUser.getEmergencyContactInfo());
                userGrade.setText(currentUser.getGrade());
                userHomePhoneNumber.setText(currentUser.getHomePhone());
                userTeacherName.setText(currentUser.getTeacherName());
            } catch (NullPointerException e) {
                userBirthMonth.setSelection(12);
            }
    }

    private void setupMonths() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userBirthMonth.setAdapter(adapter);
        userBirthMonth.setSelection(12);
    }

    private void setupHints() {
        userAddress.setHint("#1 big house way, Surrey BC, H0H 0H0, Canada");
        userBirthYear.setHint("2005");
        userHomePhoneNumber.setHint("(604) 123-4567");
        userCellPhoneNumber.setHint("+1.778.098.7765");
        userTeacherName.setHint("Mr. Big");
        userGrade.setHint("Kindergarten");
        userEmergencyContactInfo.setHint("Call my mom! She knows how to help.");
    }

    private void setupIds() {
        userAddress = findViewById(R.id.anmol_addressUserC);
        userBirthMonth = findViewById(R.id.anmol_monthBornUserC);
        userBirthYear = findViewById(R.id.anmol_yearBornUserC);
        userCellPhoneNumber = findViewById(R.id.anmol_cellPhoneUserC);
        userHomePhoneNumber = findViewById(R.id.anmol_homePhoneUserC);
        userGrade = findViewById(R.id.anmol_gradeUserC);
        userTeacherName = findViewById(R.id.anmol_userTeacherNameC);
        userEmergencyContactInfo = findViewById(R.id.anmol_emergencyContactInfoUserC);
        userRegister = findViewById(R.id.anmol_registerC);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, EditOwnProfilePart2.class);
    }
}
