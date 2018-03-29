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
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

public class EditProfileActivity extends AppCompatActivity {

    private Button nextButton;
    private EditText emailAddress;
    private EditText userNameInputed;
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_ID = "UserID";
    private long userId;
    User user;
    private ModelManager modelManager;

    private Spinner userBirthMonth;
    private EditText userBirthYear;
    private EditText userHomePhoneNumber;
    private EditText userCellPhoneNumber;
    private EditText userAddress;
    private EditText userGrade;
    private EditText userTeacherName;
    private EditText userEmergencyContactInfo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        modelManager = ModelManager.getInstance();

        getExtra();
        setupIDs();
        setupHints();
        setupMonths();

        ProxyBuilder.SimpleCallback<User> getUserByIdCallback = monitoredByUsers -> setupUserInfo(monitoredByUsers);
        modelManager.getUserById(EditProfileActivity.this, getUserByIdCallback, userId);

        setupButtonClick();
    }

    private void getExtra() {
        Intent intent = getIntent();
        userId = intent.getLongExtra(USER_ID, -1);
    }

    private void setupUserInfo(User currentPulledUser) {
        user = currentPulledUser;
        emailAddress.setText(user.getEmail());
        userNameInputed.setText(user.getName());

        if (user.getBirthMonth() != null) {
            userBirthMonth.setSelection(user.getBirthMonth() - 1);
        } else {
            userBirthMonth.setSelection(12);
        }

        if (user.getBirthYear() != null) {
            userBirthYear.setText(user.getBirthYear().toString());
        } else {
            userBirthYear.setText("");
        }

        if (user.getAddress() != null) {
            userAddress.setText(user.getAddress());
        } else {
            userAddress.setText("");
        }

        if (user.getCellPhone() != null) {
            userCellPhoneNumber.setText(user.getCellPhone());
        } else {
            userCellPhoneNumber.setText("");
        }

        if (user.getEmergencyContactInfo() != null) {
            userEmergencyContactInfo.setText(user.getEmergencyContactInfo());
        } else {
            userEmergencyContactInfo.setText("");
        }

        if (user.getGrade() != null) {
            userGrade.setText(user.getGrade());
        } else {
            userGrade.setText("");
        }

        if (user.getHomePhone() != null) {
            userHomePhoneNumber.setText(user.getHomePhone());
        } else {
            userHomePhoneNumber.setText("");
        }

        if (user.getTeacherName() != null) {
            userTeacherName.setText(user.getTeacherName());
        } else {
            userTeacherName.setText("");
        }
    }

    private void setupIDs() {
        //Setting up EditTexts to be used later on
        emailAddress = findViewById(R.id.anmol_emailAddressUser);
        userNameInputed = findViewById(R.id.anmol_nameUserInput);
        nextButton = findViewById(R.id.anmol_nextButton);

        //Part 2
        userAddress = findViewById(R.id.anmol_addressUserC4);
        userBirthMonth = findViewById(R.id.anmol_monthBornUserC3);
        userBirthYear = findViewById(R.id.anmol_yearBornUserC3);
        userCellPhoneNumber = findViewById(R.id.anmol_cellPhoneUserC4);
        userHomePhoneNumber = findViewById(R.id.anmol_homePhoneUserC4);
        userGrade = findViewById(R.id.anmol_gradeUserC3);
        userTeacherName = findViewById(R.id.anmol_userTeacherNameC3);
        userEmergencyContactInfo = findViewById(R.id.anmol_emergencyContactInfoUserC4);
    }

    private void setupButtonClick() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailAddress.getText().toString();
                name = userNameInputed.getText().toString();
                if(name.length() >= 1 && email.length() >= 1) {
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
                    //Get Birth Month

                    if(userAddress.getText().toString().trim().equals("")) {
                        address = null;
                    } else {
                        address = userAddress.getText().toString().trim();
                    }

                    if(userCellPhoneNumber.getText().toString().trim().equals("")) {
                        cellPhone = null;
                    } else {
                        cellPhone = userCellPhoneNumber.getText().toString().trim();
                    }

                    if(userHomePhoneNumber.getText().toString().trim().equals("")) {
                        homePhone = null;
                    } else {
                        homePhone = userHomePhoneNumber.getText().toString().trim();
                    }

                    if(userGrade.getText().toString().trim().equals("")) {
                        grade = null;
                    } else {
                        grade = userGrade.getText().toString().trim();
                    }

                    if(userTeacherName.getText().toString().trim().equals("")) {
                        teacherName = null;
                    } else {
                        teacherName = userCellPhoneNumber.getText().toString().trim();
                    }

                    if(userEmergencyContactInfo.getText().toString().trim().equals("")) {
                        emergencyContactInfo = null;
                    } else {
                        emergencyContactInfo = userEmergencyContactInfo.getText().toString().trim();
                    }
                    //TODO: NEED HELP HERE JUSTIN THANKS!
                    ProxyBuilder.SimpleCallback<User> callback = setupNewInformation -> editInformation(setupNewInformation);
                    modelManager.editUserById(EditProfileActivity.this, callback, userId, name, email,
                            birthYear, birthMonth, address, cellPhone, homePhone, grade, teacherName, emergencyContactInfo);
                } else {
                    Toast.makeText(EditProfileActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupMonths() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userBirthMonth.setAdapter(adapter);
        userBirthMonth.setSelection(12);
    }

    private void setupHints() {
        userNameInputed.setHint(R.string.anmol_UserHintName);
        emailAddress.setHint(R.string.anmol_userEmailHint);

        userAddress.setHint("#1 big house way, Surrey BC, H0H 0H0, Canada");
        userBirthYear.setHint("2005");
        userHomePhoneNumber.setHint("(604) 123-4567");
        userCellPhoneNumber.setHint("+1.778.098.7765");
        userTeacherName.setHint("Mr. Big");
        userGrade.setHint("Kindergarten");
        userEmergencyContactInfo.setHint("Call my mom! She knows how to help.");
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, EditProfileActivity.class);
    }

    public static Intent makeIntent(Context context, Long userId){
        Intent intent = new Intent(context, EditProfileActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void editInformation(User monitoredByUsers) {
        Toast.makeText(EditProfileActivity.this,"Account Edited",Toast.LENGTH_SHORT).show();
        finish();
    }
}
