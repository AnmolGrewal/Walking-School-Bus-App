package com.cmpt276.project.walkinggroupapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.appactivities.CheckPermissionActivity;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.Permission;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import java.util.List;

/***
 * Fragment used to display permission request from a user
 *
 * It creates a small dialog box to allow the user to select approve of deny
 */

public class viewPermissionFragment extends AppCompatDialogFragment
{
    String message;
    TextView messageLocation;
    Long permissionID;
    ModelManager modelManager = ModelManager.getInstance();
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState)
    {
        Bundle variables = getArguments();
        message = variables.getString("MessagePermissionForUser", "Error With Message");
        permissionID = variables.getLong("PermissionID");

        //Create the activity to show
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.view_message_layout, null);
        //Create a Button Listener
        messageLocation = v.findViewById(R.id.jacky_view_message_text_view);
        messageLocation.setText(message);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i){
                    case DialogInterface.BUTTON_POSITIVE:
                        approvePermission();
                        getActivity().onBackPressed();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        denyPermission();
                        getActivity().onBackPressed();
                        break;
                }
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle("Viewing Permission Message")
                .setView(v)
                .setPositiveButton("Allow", listener)
                .setNegativeButton("Deny", listener)
                .create();
    }

    private void approvePermission(){
        ProxyBuilder.SimpleCallback<Void> permissionCallback = response -> changePermissionResponse(response);
        modelManager.changePermissionStatus(getActivity(), permissionCallback, permissionID, "APPROVED");

    }

    private void denyPermission(){
        ProxyBuilder.SimpleCallback<Void> permissionCallback = response -> changePermissionResponse(response);
        modelManager.changePermissionStatus(getActivity(), permissionCallback, permissionID, "DENIED");
    }

    private void changePermissionResponse(Void response){
        Log.i("MyApp", "Changed Permission");
    }


}
