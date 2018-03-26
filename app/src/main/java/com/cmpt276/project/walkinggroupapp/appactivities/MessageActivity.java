package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.fragments.sendMessageFragment;
import com.cmpt276.project.walkinggroupapp.fragments.viewMessageFragment;
import com.cmpt276.project.walkinggroupapp.model.Message;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.model.User;
import com.cmpt276.project.walkinggroupapp.model.WalkingGroup;
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
    private Button btnPanic;

    private ListView messageListView;
    private List<Message> messageList = new ArrayList<>();

    private ModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        setupSendButton();
        setupPanicButton();

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

    private void setupPanicButton(){
        btnPanic = findViewById(R.id.jacky_panic_button);
        btnPanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDialog(true);
            }
        });
    }

    private void sendPanicMessageResponse(Message noResponse){
        Log.i("MyApp", "Sent Panic Message");
    }

    private void sendMessage(){
        sendDialog(false);
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
            //Find a message to add
            Message currentMessage = messageList.get(position);
            Long currentMessageId = currentMessage.getId();
            User fromUser = currentMessage.getFromUser();
            WalkingGroup toGroup = currentMessage.getToGroup();

            //Make sure We are given a view
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.message_list_layout, parent, false);
            }

            if(currentMessage.isEmergency() == true){
                itemView.setBackgroundResource(R.color.colorAccent);
            }else{
                itemView.setBackgroundResource(R.color.white);
            }

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

            //Group ID:
            TextView groupID = itemView.findViewById(R.id.jacky_group_id_dynamic);
            if(toGroup == null){
                groupID.setText("No Group");
            }else{
                groupID.setText(Long.toString(toGroup.getId()));
            }

            return itemView;
        }
    }

    private void registerReadMessage()                                                                                    //For clicking on list object
    {
        final ListView list = findViewById(R.id.jacky_message_list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Message readMessage = messageList.get(position);
                if(readMessage.isRead() == false){
                    readMessage.setRead(false);
                    markMessageRead(readMessage.getId());
                    alertDialog(readMessage);
                }else{
                    alertDialog(readMessage);
                }
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Pressed Long to edit" + position, Toast.LENGTH_SHORT).show();
                Log.i("MyApp", "Pressed Long" + position);
                Message readMessage = messageList.get(position);
                PopupMenu popupMenu = new PopupMenu(MessageActivity.this, viewClicked);
                popupMenu.getMenuInflater().inflate(R.menu.popup_message_options, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {          //Code from https://www.youtube.com/watch?v=LXUDqGaToe0
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch(menuItem.getItemId())
                        {
                            case R.id.jacky_mark_as_read:
                                if(readMessage.isRead() == false) {
                                    markMessageRead(readMessage.getId());
                                }
                                break;
                            case R.id.jacky_mark_as_unread:
                                if(readMessage.isRead() == true) {
                                    markMessageUnread(readMessage.getId());
                                }
                                break;
                            case R.id.jacky_delete_message:
                                deleteMessage(readMessage.getId());
                                break;
                        }
                        return true;
                    }

                });
                popupMenu.show();
                return true;
            }
        });
    }

    private void markMessageRead(Long messageId){
        ProxyBuilder.SimpleCallback<Void> readCallback = readResponse -> markMessageResponse(readResponse);
        modelManager.markMessageAsRead(MessageActivity.this, readCallback, messageId);
    }

    private void markMessageUnread(Long messageId){
        ProxyBuilder.SimpleCallback<Void> readCallback = readResponse -> markMessageResponse(readResponse);
        modelManager.markMessageAsUnread(MessageActivity.this, readCallback, messageId);
    }

    private void markMessageResponse(Void noResponse){
        ProxyBuilder.SimpleCallback<List<Message>> messageCallback = messageList -> getMessageList(messageList);
        modelManager.getMessagesForUser(MessageActivity.this, messageCallback);
    }

    private void deleteMessage(Long messageId){
        ProxyBuilder.SimpleCallback<Void> readCallback = readResponse -> deleteMessageResponse(readResponse);
        modelManager.deleteMessageByMessageId(MessageActivity.this, readCallback, messageId);
    }

    private void deleteMessageResponse(Void noResponse){
        ProxyBuilder.SimpleCallback<List<Message>> messageCallback = messageList -> getMessageList(messageList);
        modelManager.getMessagesForUser(MessageActivity.this, messageCallback);
    }



    public static Intent makeIntent(Context context){
        return new Intent(context, MessageActivity.class);
    }

    private void alertDialog(Message readMessage)
    {
        FragmentManager manager = getSupportFragmentManager();
        viewMessageFragment dialog = new viewMessageFragment();

        // Supply index input as an argument.
        Bundle variables = new Bundle();
        variables.putString("MessageForUser", readMessage.getText());

        dialog.setArguments(variables);
        dialog.show(manager, "MyApp");
    }

    private void sendDialog(boolean isEmergency){
        FragmentManager manager = getSupportFragmentManager();
        sendMessageFragment dialog = new sendMessageFragment();

        // Supply index input as an argument.
        Bundle variables = new Bundle();
        variables.putBoolean("IsEmergencyMessage", isEmergency);

        dialog.setArguments(variables);
        dialog.show(manager, "SendView");
    }

    private void getMessageList(List<Message> sortedList){
        int i = 0;
        Message compareMessage;
        Message currentMessages;
        for (i = 0; i < sortedList.size(); i++) {
            currentMessages = sortedList.get(i);
            if(i < sortedList.size() - 1)
            {
                Log.i("MyApp", "i: " + i + " ListSize: " + sortedList.size());
                Log.i("MyApp", "Id1: " + Long.toString(currentMessages.getId()) + " ID2: " + Long.toString(sortedList.get(i+1).getId()));
                compareMessage = sortedList.get(i + 1);
                if(currentMessages.getId().equals(compareMessage.getId())){
                    Log.i("MyApp", "Removed an object");
                    sortedList.remove(i+1);
                }else{
                    Log.i("MyApp", "is This even ran");
                }

            }else{
                Log.i("MyApp", "SameSize");
            }
            Log.i("MyApp", "Increment i");
        }
        messageList.clear();
        messageList = sortedList;
        populateMessageList();
        registerReadMessage();
    }
}
