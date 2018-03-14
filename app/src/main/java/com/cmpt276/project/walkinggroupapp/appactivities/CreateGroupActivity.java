package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.WalkingGroup;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

public class CreateGroupActivity extends AppCompatActivity {

    private Button createBtn;

    private ModelManager modelManager;

    private EditText startLatEditText, startLngEditText, endLatEditText, endLngEditText, groupDescriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        modelManager = ModelManager.getInstance();


        setupCreateButton();
    }

    private void setupCreateButton() {
        createBtn = findViewById(R.id.jacky_create_new_group_btn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startLatEditText = findViewById(R.id.jacky_start_latitude);
                String groupStartLatString = startLatEditText.getText().toString();

                startLngEditText = findViewById(R.id.jacky_start_longitude);
                String groupStartLonString = startLngEditText.getText().toString();

                endLatEditText = findViewById(R.id.jacky_end_latitude);
                String groupEndLatString = endLatEditText.getText().toString();

                endLngEditText = findViewById(R.id.jacky_end_longitude);
                String groupEndLonString = endLngEditText.getText().toString();

                groupDescriptionEditText = findViewById(R.id.jacky_create_group_description_dynamic);

                if(groupEndLatString.equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid inputs", Toast.LENGTH_LONG).show();
                }
                else if(groupEndLonString.equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid inputs", Toast.LENGTH_LONG).show();
                }
                else if(groupStartLatString.equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid inputs", Toast.LENGTH_LONG).show();
                }
                else if(groupStartLonString.equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid inputs", Toast.LENGTH_LONG).show();
                }
                else
                {
                    double groupStartLat = Double.parseDouble(groupStartLatString);
                    double groupStartLng = Double.parseDouble(groupStartLonString);
                    double groupEndLat = Double.parseDouble(groupEndLatString);
                    double groupEndLng = Double.parseDouble(groupEndLonString);
                    String groupDescription = groupDescriptionEditText.getText().toString();


                    ProxyBuilder.SimpleCallback<WalkingGroup> callback = returnedGroup -> createNewGroupResponse(returnedGroup);

                    modelManager.createNewWalkingGroup(CreateGroupActivity.this,
                            callback,
                            groupDescription,
                            groupStartLat, groupStartLng,
                            groupEndLat, groupEndLng);


//                    finish();
                }



            }
        });

    }

    private void createNewGroupResponse(WalkingGroup returnedGroup) {
        finish();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, CreateGroupActivity.class);
    }
}
