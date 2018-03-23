package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.cmpt276.project.walkinggroupapp.R;
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
 *  When this activity is called we will make a call to the server inorder to retrieve a copy of all the messages sent to the current user.
 *  When we get the callback we will populate the list using the modified array adapter.
 */

public class MessageActivity extends AppCompatActivity {

    private ListView messageListView;
    private List<Message> messageList = new ArrayList<>();

    private ModelManager modelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        modelManager = ModelManager.getInstance();

        ProxyBuilder.SimpleCallback<List<Message>> messageCallback = messageList -> getMessageList(messageList);
        modelManager.getMessagesForUser(MessageActivity.this, messageCallback);
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
            Log.i("MyApp", "Inside Monitoring By");
            Message currentMessage = messageList.get(position);
            Long currentMessageId = currentMessage.getId();
            User fromUser = currentMessage.getFromUser();

            //Icon
            ImageView readImage = itemView.findViewById(R.id.jacky_message_image_view);
            readImage.setImageResource(R.drawable.temp_pic);

            //Message id:
            TextView makeName = itemView.findViewById(R.id.jacky_message_id_dynamic);
            makeName.setText(Long.toString(currentMessageId));

            //Email
            TextView makeEmail = itemView.findViewById(R.id.jacky_from_dynamic);
            makeEmail.setText(fromUser.getEmail());

            return itemView;
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MainMenuActivity.class);
    }


    private void getMessageList(List<Message> sortedList){
        messageList = sortedList;
        populateMessageList();
    }
}
