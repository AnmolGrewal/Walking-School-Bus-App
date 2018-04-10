package com.cmpt276.project.walkinggroupapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cmpt276.project.walkinggroupapp.R;

/***
 * Fragment used to display information of past permission
 *
 * It creates a small dialog box to allow the user to see the status and who accepted/rejected the permission
 */

public class viewPastPermissionFragment extends AppCompatDialogFragment
{
    /*private String message;
    private String status;
    private String email;
    private String name;

    private Long userId;


    TextView messageLocation;
    TextView statusLocation;
    TextView emailLocation;
    TextView nameLocation;
    TextView userIdLocation;

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState)
    {
        Bundle variables = getArguments();

        message = variables.getString("MessageForPastPermission", "Error With Message");
        status = variables.getString("StatusOfPermission", "Error with Status");
        email = variables.getString("UserEmailWhoAuthorized", "Error with email");
        name = variables.getString("UserNameWhoAuthorized", "Error with name");
        userId = variables.getLong("UserIdWhoAuthorized", 0);

        //Create the activity to show
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.view_past_permission_layout, null);
        //Create a Button Listener
        messageLocation = v.findViewById(R.id.jacky_permision_message_dynamic);
        messageLocation.setText(message);

        statusLocation = v.findViewById(R.id.jacky_status_message_dynamic);
        statusLocation.setText(status);

        emailLocation = v.findViewById(R.id.jacky_permission_email);
        emailLocation.setText(email);

        nameLocation = v.findViewById(R.id.jacky_dynamic_name_permission);
        nameLocation.setText(name);

        userIdLocation = v.findViewById(R.id.jacky_userid_dynamic_permissions);
        userIdLocation.setText(Long.toString(userId));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Do nothing
                        break;
                }
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle("Viewing Past Permissions")
                .setView(v)
                .setPositiveButton("Done", listener)
                .create();
    }*/

}
