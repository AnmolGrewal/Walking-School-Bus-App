package com.cmpt276.project.walkinggroupapp.fragments;

        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.os.Message;
        import android.support.v7.app.AppCompatDialogFragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import com.cmpt276.project.walkinggroupapp.R;

public class viewMessageFragment extends AppCompatDialogFragment
{
    String message;
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState)
    {
        Bundle variables = getArguments();
         message = variables.getString("MessageForUser", "Error With Message");

        //Create the activity to show
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.view_message_layout, null);
        //Create a Button Listener
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
                .setTitle(message)
                .setView(v)
                .setPositiveButton("Okay", listener)
                .create();
    }
}
