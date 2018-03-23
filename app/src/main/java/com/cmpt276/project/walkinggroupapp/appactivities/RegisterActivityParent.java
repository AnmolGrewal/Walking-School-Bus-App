package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmpt276.project.walkinggroupapp.R;

public class RegisterActivityParent extends AppCompatActivity {

    private String name;
    private String email;
    private String password1;
    private int birthYear;
    private int birthMonth;
    private String address;
    private String cellPhone;
    private String homePhone;
    private String grade;
    private String teacherName;
    private String emergencyContactInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_parent);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, RegisterActivityParent.class);
    }
}

                    /*
                    birthYear = 2012;
                    birthMonth = 12;
                    address = "#1 big way, Surrey BC, H0H 0H0, Canada";
                    cellPhone = "+1.778.098.7765";
                    homePhone = "(604) 123-4567";
                    grade = "Kindergarten";
                    teacherName = "Mr.Big";
                    emergencyContactInfo = "Call my mom! She knows how to help.";
                    ProxyBuilder.SimpleCallback<Void> callback = returnedNothing -> registerResponse(returnedNothing);
                    modelManager.register(RegisterActivity.this, callback, name, email, password1,
                            birthYear, birthMonth, address, cellPhone, homePhone, grade, teacherName, emergencyContactInfo);
                    */

