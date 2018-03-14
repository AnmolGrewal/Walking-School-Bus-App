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

public class CreateGroup extends AppCompatActivity {

    private Button createBtn;

    private ModelManager modelManager;

    private EditText startLat, startLon, endLat, endLon, groupDescription;

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

                startLat = findViewById(R.id.jacky_start_latitude);
                String groupStartLatString = startLat.getText().toString();

                startLon = findViewById(R.id.jacky_start_longitude);
                String groupStartLonString = startLon.getText().toString();

                endLat = findViewById(R.id.jacky_end_latitude);
                String groupEndLatString = endLat.getText().toString();

                endLon = findViewById(R.id.jacky_end_longitude);
                String groupEndLonString = endLon.getText().toString();

                groupDescription = findViewById(R.id.jacky_create_group_description_dynamic);

                if(groupEndLatString.matches("")){
                    Toast.makeText(getApplicationContext(), "Invalid inputs", Toast.LENGTH_LONG).show();
                }
                else if(groupEndLonString.matches("")){
                    Toast.makeText(getApplicationContext(), "Invalid inputs", Toast.LENGTH_LONG).show();
                }
                else if(groupStartLatString.matches("")){
                    Toast.makeText(getApplicationContext(), "Invalid inputs", Toast.LENGTH_LONG).show();
                }
                else if(groupStartLonString.matches("")) {
                    Toast.makeText(getApplicationContext(), "Invalid inputs", Toast.LENGTH_LONG).show();
                }
                else
                {
                    double groupStartLat = Double.parseDouble(groupStartLatString);
                    double groupStartLon = Double.parseDouble(groupStartLonString);
                    double groupEndLat = Double.parseDouble(groupEndLatString);
                    double groupEndLon = Double.parseDouble(groupEndLonString);
                    String newGroupDescription = groupDescription.getText().toString();
                    //TODO
                    //modelManager.
                    finish();
                }



            }
        });

    }

    public static Intent makeIntent(Context context){
        return new Intent(context, CreateGroup.class);
    }
}
