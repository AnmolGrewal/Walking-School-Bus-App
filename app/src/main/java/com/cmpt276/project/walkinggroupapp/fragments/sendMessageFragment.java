package com.cmpt276.project.walkinggroupapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.Message;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

/**
 * A fragment that creates a dialog for the user to send a message to all their parents
 */

public class sendMessageFragment extends AppCompatDialogFragment
{
    private String message;
    private EditText userMessageEditText;
    private ModelManager modelManager;

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState)
    {
        modelManager = ModelManager.getInstance();
        //Create the activity to show
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.send_message_layout, null);
        //Create a Button Listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i){
                    case DialogInterface.BUTTON_POSITIVE:
                        userMessageEditText = v.findViewById(R.id.jacky_user_send_message);
                        message = userMessageEditText.getText().toString();
                        if(message.length() == 0)
                        {
                            message = "Blank Message";
                        }
                        ProxyBuilder.SimpleCallback<Message> sendCallBack = noResponse -> sendMessageToParentsResponse(noResponse);
                        modelManager.sendMessageToParentsOf(getActivity(), sendCallBack, message, false);
                        break;
                }
            }
        };


        return new AlertDialog.Builder(getActivity())
                .setTitle(message)
                .setView(v)
                .setPositiveButton("Send", listener)
                .create();
    }

    private void sendMessageToParentsResponse(Message response){
        Log.i("MyApp", "Message sent as: " + message);
    }


}
