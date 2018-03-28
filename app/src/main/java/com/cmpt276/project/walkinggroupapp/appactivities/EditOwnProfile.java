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

public class EditOwnProfile extends AppCompatActivity {

    private Button nextButton;
    private EditText emailAddress;
    private EditText firstPassword;
    private EditText secondPassword;
    private EditText userNameInputed;
    private Spinner teacherOrStudent;
    private String name;
    private String email;
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_ID = "UserID";
    User currentUser;
    private ModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_own_profile);

        modelManager = ModelManager.getInstance();

        setupIDs();
        setupHints();
        setupButtonClick();
        setupSpinner();

        ProxyBuilder.SimpleCallback<User> getCurrentUser = monitoredByUsers -> setupUserInfo(monitoredByUsers);
        modelManager.getUser(EditOwnProfile.this, getCurrentUser);
    }

    private void setupUserInfo(User currentPulledUser) {
        currentUser = currentPulledUser;
        emailAddress.setText(currentUser.getEmail());
        userNameInputed.setText(currentUser.getName());
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
        userNameInputed = findViewById(R.id.anmol_nameUserInput);
        nextButton = findViewById(R.id.anmol_nextButton);
        teacherOrStudent = findViewById(R.id.anmol_teacherOrStudent);
    }

    private void setupButtonClick() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailAddress.getText().toString();
                name = userNameInputed.getText().toString();
                if(name.length() >= 1 && email.length() >= 1)
                {
                        String checkTeacherStudent = teacherOrStudent.getSelectedItem().toString();
                        if(checkTeacherStudent.equals("Student")) {
                            Intent intent = RegisterStudentActivity.makeIntent(EditOwnProfile.this);
                            intent.putExtra(USER_EMAIL, email);
                            intent.putExtra(USER_NAME, name);
                            startActivity(intent);
                        } else {
                            Intent intent = RegisterParentActivity.makeIntent(EditOwnProfile.this);
                            intent.putExtra(USER_EMAIL, email);
                            intent.putExtra(USER_NAME, name);
                            startActivity(intent);
                        }
                } else {
                    Toast.makeText(EditOwnProfile.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupHints() {
        userNameInputed.setHint(R.string.anmol_UserHintName);
        emailAddress.setHint(R.string.anmol_userEmailHint);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, EditOwnProfile.class);
    }

    public static Intent makeIntent(Context context, Long userId){
        Intent intent = new Intent(context, EditOwnProfile.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    public static Intent makeIntent(Context context, Long userID, Boolean isEdit){
        Intent intent = new Intent(context, EditOwnProfile.class);
        intent.putExtra(USER_ID, userID);
        intent.putExtra("isEdit", isEdit);
        return intent;
    }
}
