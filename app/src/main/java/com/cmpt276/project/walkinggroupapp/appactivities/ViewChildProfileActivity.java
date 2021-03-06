package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.MapState;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

public class ViewChildProfileActivity extends AppCompatActivity {

    private static final String USER_ID = "userId";

    private ModelManager modelManager;

    private MapState mapState;

    long userId;

    String[] months = new String[12];

    TextView txtName;
    TextView txtEmail;
    TextView txtYear;
    TextView txtMonth;
    TextView txtTeacher;
    TextView txtGrade;
    TextView txtAddress;
    TextView txtHomePhone;
    TextView txtCellPhone;
    TextView txtEmergency;

    Button btnEdit;
    Button btnGroup;
    Button btnLocation;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_child_profile);

        modelManager = ModelManager.getInstance();
        mapState = MapState.getInstance();

        getExtra();
        initMonths();
        initProgressBar();
        setupTextView();
        setupEditButton();
        setupGroupButton();
        setupLocationButton();
        setupProfileDefaultValue();


        progressBar.setVisibility(View.VISIBLE);

        ProxyBuilder.SimpleCallback<User> getUserByIdCallback = returnedUser -> getUserByIdResponse(returnedUser);
        ProxyBuilder.SimpleCallback<String> onFailureCallback = errorMessage -> onFailureResponse(errorMessage);
        modelManager.getUserById(ViewChildProfileActivity.this, getUserByIdCallback, onFailureCallback, userId);


    }

    @Override
    protected void onResume() {
        super.onResume();

        progressBar.setVisibility(View.VISIBLE);

        ProxyBuilder.SimpleCallback<User> getUserByIdCallback = returnedUser -> getUserByIdResponse(returnedUser);
        ProxyBuilder.SimpleCallback<String> onFailureCallback = errorMessage -> onFailureResponse(errorMessage);
        modelManager.getUserById(ViewChildProfileActivity.this, getUserByIdCallback, onFailureCallback, userId);
    }

    public static Intent makeIntent(Context context, long userId) {
        Intent intent = new Intent(context, ViewChildProfileActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }


    private void getExtra() {
        Intent intent = getIntent();
        userId = intent.getLongExtra(USER_ID, -1);
    }

    private void initMonths() {
        months[0] = "January";
        months[1] = "February";
        months[2] = "March";
        months[3] = "April";
        months[4] = "May";
        months[5] = "June";
        months[6] = "July";
        months[7] = "August";
        months[8] = "September";
        months[9] = "October";
        months[10] = "November";
        months[11] = "December";
    }

    private void initProgressBar() {
        progressBar = findViewById(R.id.justin_viewProfileProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setupTextView() {
        txtName = findViewById(R.id.justin_txtName2);
        txtEmail = findViewById(R.id.justin_txtEmail2);
        txtYear = findViewById(R.id.justin_txtYear2);
        txtMonth = findViewById(R.id.justin_txtMonth2);
        txtTeacher = findViewById(R.id.justin_txtTeacher2);
        txtGrade = findViewById(R.id.justin_txtGrade2);
        txtAddress = findViewById(R.id.justin_txtAddress2);
        txtHomePhone = findViewById(R.id.justin_txtHomePhone2);
        txtCellPhone = findViewById(R.id.justin_txtCellPhone2);
        txtEmergency = findViewById(R.id.justin_txtEmergency2);
    }


    private void setupEditButton() {
        btnEdit = findViewById(R.id.justin_btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EditProfileActivity.makeIntent(getApplicationContext(), userId);
                startActivity(intent);
            }
        });
    }

    private void setupGroupButton() {
        btnGroup = findViewById(R.id.justin_btnGroup);
        btnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EditMonitoringUserGroupActivity.makeIntent(getApplicationContext(), userId);
                startActivity(intent);
            }
        });
    }

    private void setupLocationButton() {
        btnLocation = findViewById(R.id.justin_btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set properties to let map know what to do
                //set to let map know what to do
                MapState.CurrentStateEnum currentState = MapState.CurrentStateEnum.IsViewingAChild;
                mapState.setCurrentStateEnum(currentState);

                //go to map activity
                Intent intent = MapActivity.makeIntentViewChild(ViewChildProfileActivity.this, userId);
                startActivity(intent);

            }
        });
    }

    private void setupProfileDefaultValue() {
        txtName.setText("");
        txtEmail.setText("");
        txtYear.setText("");
        txtMonth.setText("");
        txtTeacher.setText("");
        txtGrade.setText("");
        txtAddress.setText("");
        txtHomePhone.setText("");
        txtCellPhone.setText("");
        txtEmergency.setText("");
    }

    private void getUserByIdResponse(User returnedUser) {
        progressBar.setVisibility(View.INVISIBLE);

        populateTextView(returnedUser);
    }

    private void populateTextView(User returnedUser) {
        if (returnedUser.getName() != null) {
            txtName.setText(returnedUser.getName());
        } else {
            txtName.setText("N/A");
        }

        if (returnedUser.getEmail() != null) {
            txtEmail.setText(returnedUser.getEmail());
        } else {
            txtEmail.setText("N/A");
        }

        if (returnedUser.getBirthYear() != null) {
            String yearString = "" + returnedUser.getBirthYear();
            txtYear.setText(yearString);
        } else {
            txtYear.setText("N/A");
        }

        if (returnedUser.getBirthMonth() != null) {
            txtMonth.setText(months[returnedUser.getBirthMonth() - 1]);
        } else {
            txtMonth.setText("N/A");
        }

        if (returnedUser.getTeacherName() != null) {
            txtTeacher.setText(returnedUser.getTeacherName());
        } else {
            txtTeacher.setText("N/A");
        }

        if (returnedUser.getGrade() != null) {
            txtGrade.setText(returnedUser.getGrade());
        } else {
            txtGrade.setText("N/A");
        }

        if (returnedUser.getAddress() != null) {
            txtAddress.setText(returnedUser.getAddress());
        } else {
            txtAddress.setText("N/A");
        }

        if (returnedUser.getHomePhone() != null) {
            txtHomePhone.setText(returnedUser.getHomePhone());
        } else {
            txtHomePhone.setText("N/A");
        }

        if (returnedUser.getCellPhone() != null) {
            txtCellPhone.setText(returnedUser.getCellPhone());
        } else {
            txtCellPhone.setText("N/A");
        }

        if (returnedUser.getEmergencyContactInfo() != null) {
            txtEmergency.setText(returnedUser.getEmergencyContactInfo());
        } else {
            txtEmergency.setText("N/A");
        }

    }

    private void onFailureResponse(String errorMessage) {
        progressBar.setVisibility(View.INVISIBLE);

        Toast.makeText(ViewChildProfileActivity.this,
                "Load user profile file.\n\nError message:\n" + errorMessage,
                Toast.LENGTH_LONG)
                .show();
    }
}
