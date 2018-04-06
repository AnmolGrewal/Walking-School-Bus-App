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
import com.cmpt276.project.walkinggroupapp.model.MapState;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.WalkingGroup;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;


/**
 * Class for creating Walking Groups in the map
 * Accepts only manual input latitude and longitude for now
 * */
public class CreateGroupActivity extends AppCompatActivity {

    private Button createBtn;
    private Button selectStartLocationBtn;
    private Button selectEndLocationBtn;

    private ModelManager modelManager;
    private MapState mapState;

    private EditText startLatEditText, startLngEditText, endLatEditText, endLngEditText, groupDescriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        modelManager = ModelManager.getInstance();
        mapState = MapState.getInstance();

        setUpEditText();

        setupSelectStartLocationButton();

        setupSelectEndLocationButton();

        setupCreateButton();
    }


    private void setUpEditText() {
        startLatEditText = findViewById(R.id.jacky_start_latitude);
        startLngEditText = findViewById(R.id.jacky_start_longitude);
        endLatEditText = findViewById(R.id.jacky_end_latitude);
        endLngEditText = findViewById(R.id.jacky_end_longitude);
        groupDescriptionEditText = findViewById(R.id.jacky_create_group_description_dynamic);
    }

    private void setupSelectStartLocationButton() {
        selectStartLocationBtn = findViewById(R.id.gerry_Select_Start_Location_Button_create_group);
        selectStartLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set to let map know what to do
                //set to let map know what to do
                MapState.CurrentStateEnum currentState = MapState.CurrentStateEnum.IsSelectingStartLocation;
                mapState.setCurrentStateEnum(currentState);

                //go to map activity
                Intent intent = new Intent(CreateGroupActivity.this, MapActivity.class);
                startActivity(intent);

            }
        });
    }

    private void setupSelectEndLocationButton() {
        selectEndLocationBtn = findViewById(R.id.gerry_Select_End_Location_Button_create_group);
        selectEndLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set to let map know what to do
                MapState.CurrentStateEnum currentState = MapState.CurrentStateEnum.IsSelectingEndLocation;
                mapState.setCurrentStateEnum(currentState);

                //go to map activity
                Intent intent = new Intent(CreateGroupActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupCreateButton() {
        createBtn = findViewById(R.id.jacky_create_new_group_btn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String groupStartLatString = startLatEditText.getText().toString();

                String groupStartLonString = startLngEditText.getText().toString();

                String groupEndLatString = endLatEditText.getText().toString();

                String groupEndLonString = endLngEditText.getText().toString();


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


                    //clear the text for the editTexts
                    startLatEditText.getText().clear();
                    startLngEditText.getText().clear();
                    endLatEditText.getText().clear();
                    endLngEditText.getText().clear();
                    groupDescriptionEditText.getText().clear();


//                    finish();
                }



            }
        });

    }




    @Override
    public void onResume(){
        super.onResume();

        //get data from MapState and put it to the editText
        if(mapState.getSelectedStartLocation() != null) {
            double lat = mapState.getSelectedStartLocation().latitude;
            double lng = mapState.getSelectedStartLocation().longitude;
            String latString = String.valueOf(lat);
            String lngString = String.valueOf(lng);

            startLatEditText.setText(latString);
            startLngEditText.setText(lngString);

        }

        if(mapState.getSelectedEndLocation() != null) {
            double lat = mapState.getSelectedEndLocation().latitude;
            double lng = mapState.getSelectedEndLocation().longitude;
            String latString = String.valueOf(lat);
            String lngString = String.valueOf(lng);

            endLatEditText.setText(latString);
            endLngEditText.setText(lngString);

        }

    }




    private void createNewGroupResponse(WalkingGroup returnedGroup) {
        finish();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, CreateGroupActivity.class);
    }
}
