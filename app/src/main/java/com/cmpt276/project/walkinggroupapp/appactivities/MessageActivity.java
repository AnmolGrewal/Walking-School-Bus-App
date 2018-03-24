package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.fragments.sendMessageFragment;
import com.cmpt276.project.walkinggroupapp.fragments.viewMessageFragment;
import com.cmpt276.project.walkinggroupapp.model.Message;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

import java.util.ArrayList;
import java.util.List;

/******
 *
 * Main Menu Activity
 *  Notice the complex List Adapter is based off from https://www.youtube.com/watch?v=WRANgDgM2Zg
 *  a video provided by the prof for assignment 2
 *  Code is adapted from Jacky.T  Assignment 2
 *
 *  Fragments are based off of the videos that Brian Fraser created
 *  https://developer.android.com/guide/topics/ui/dialogs.html
 *
 *  When this activity is called we will make a call to the server inorder to retrieve a copy of all the messages sent to the current user.
 *  When we get the callback we will populate the list using the modified array adapter.
 */

public class MessageActivity extends AppCompatActivity {

    private Button btnSend;

    private ListView messageListView;
    private List<Message> messageList = new ArrayList<>();
    private Message readMessage;

    private ModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        setupSendButton();

        modelManager = ModelManager.getInstance();

        ProxyBuilder.SimpleCallback<List<Message>> messageCallback = messageList -> getMessageList(messageList);
        modelManager.getMessagesForUser(MessageActivity.this, messageCallback);
    }

    private void setupSendButton(){
        btnSend = findViewById(R.id.jacky_send_button);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage(){
        sendDialog();
    }

    private void populateMessageList() {
        ArrayAdapter<Message> adapter = new MessageActivity.messageAdapter();
        //Configure ListView
        messageListView = findViewById(R.id.jacky_message_list);
        messageListView.setAdapter(adapter);
    }

    private class messageAdapter extends ArrayAdapter<Message> {
        public messageAdapter() {
            super(MessageActivity.this, R.layout.message_list_layout, messageList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.message_list_layout, parent, false);
            }
            //Find a user to add
            Message currentMessage = messageList.get(position);
            Long currentMessageId = currentMessage.getId();
            User fromUser = currentMessage.getFromUser();

            //Icon
            if(currentMessage.isRead()) {
                ImageView readImage = itemView.findViewById(R.id.jacky_message_image_view);
                readImage.setImageResource(R.drawable.read_icon);
            }else{
                ImageView readImage = itemView.findViewById(R.id.jacky_message_image_view);
                readImage.setImageResource(R.drawable.unread_icon);
            }

            //Message id:
            TextView makeName = itemView.findViewById(R.id.jacky_message_id_dynamic);
            makeName.setText(Long.toString(currentMessageId));

            //Email
            TextView makeEmail = itemView.findViewById(R.id.jacky_from_dynamic);
            makeEmail.setText(Long.toString(fromUser.getId()));

            return itemView;
        }
    }

    private void registerReadMessage()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.jacky_message_list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                readMessage = messageList.get(position);
                if(readMessage.isRead() == false){
                    readMessage.setRead(false);
                    ProxyBuilder.SimpleCallback<Void> readCallback = readResponse -> serverResponse(readResponse);
                    modelManager.markMessageAsRead(MessageActivity.this, readCallback, readMessage.getId());
                }else{
                    alertDialog();
                }
            }
        });
    }

    private void serverResponse(Void noResponse){
        alertDialog();
        ProxyBuilder.SimpleCallback<List<Message>> messageCallback = messageList -> getMessageList(messageList);
        modelManager.getMessagesForUser(MessageActivity.this, messageCallback);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MessageActivity.class);
    }

    private void alertDialog()
    {
        FragmentManager manager = getSupportFragmentManager();
        viewMessageFragment dialog = new viewMessageFragment();

        // Supply index input as an argument.
        Bundle variables = new Bundle();
        variables.putString("MessageForUser", readMessage.getText());

        dialog.setArguments(variables);
        dialog.show(manager, "MyApp");
    }

    private void sendDialog(){
        FragmentManager manager = getSupportFragmentManager();
        sendMessageFragment dialog = new sendMessageFragment();

        dialog.show(manager, "SendView");
    }

    private void getMessageList(List<Message> sortedList){
        messageList = sortedList;
        populateMessageList();
        registerReadMessage();
    }
}
