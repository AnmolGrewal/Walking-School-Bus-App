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

public class ViewProfileActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        modelManager = ModelManager.getInstance();

        getExtra();
        setupIDs();
        setupMonths();

        ProxyBuilder.SimpleCallback<User> getUserByIdCallback = monitoredByUsers -> setupUserInfo(monitoredByUsers);
        modelManager.getUserById(ViewProfileActivity.this, getUserByIdCallback, userId);

        disableEditing();
    }

    private void disableEditing() {
        emailAddress.setEnabled(false);
        userTeacherName.setEnabled(false);
        userBirthYear.setEnabled(false);
        userHomePhoneNumber.setEnabled(false);
        userCellPhoneNumber.setEnabled(false);
        userAddress.setEnabled(false);
        userGrade.setEnabled(false);
        userTeacherName.setEnabled(false);
        userEmergencyContactInfo.setEnabled(false);
        userNameInputed.setEnabled(false);
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
            userBirthYear.setText("N/A");
        }

        if (user.getAddress() != null) {
            userAddress.setText(user.getAddress());
        } else {
            userAddress.setText("N/A");
        }

        if (user.getCellPhone() != null) {
            userCellPhoneNumber.setText(user.getCellPhone());
        } else {
            userCellPhoneNumber.setText("N/A");
        }

        if (user.getEmergencyContactInfo() != null) {
            userEmergencyContactInfo.setText(user.getEmergencyContactInfo());
        } else {
            userEmergencyContactInfo.setText("N/A");
        }

        if (user.getGrade() != null) {
            userGrade.setText(user.getGrade());
        } else {
            userGrade.setText("N/A");
        }

        if (user.getHomePhone() != null) {
            userHomePhoneNumber.setText(user.getHomePhone());
        } else {
            userHomePhoneNumber.setText("N/A");
        }

        if (user.getTeacherName() != null) {
            userTeacherName.setText(user.getTeacherName());
        } else {
            userTeacherName.setText("N/A");
        }
    }

    private void setupIDs() {
        //Setting up EditTexts to be used later on
        emailAddress = findViewById(R.id.anmol_emailAddressUser);
        userNameInputed = findViewById(R.id.anmol_nameUserInput);

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

    private void setupMonths() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userBirthMonth.setClickable(false);
        userBirthMonth.setEnabled(false);
        userBirthMonth.setAdapter(adapter);
    }

    public static Intent makeIntent(Context context, Long userId){
        Intent intent = new Intent(context, ViewProfileActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
