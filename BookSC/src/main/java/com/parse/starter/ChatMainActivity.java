package com.parse.starter;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.Model.MessageContainer;
import com.parse.starter.Model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatMainActivity extends AppCompatActivity {
    private User fromUser;
    private List<MessageContainer> messageContainerList;
    private ListView lvChat;
    private ChatMainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }


//        ActionBar ab = getActionBar();
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#b31913"));
//        ab.setBackgroundDrawable(colorDrawable);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        getActionBar().setDisplayHomeAsUpEnabled(true);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        fromUser=(User)ParseUser.getCurrentUser();
        setupMessagePosting();


    }

    private void setupMessagePosting() {

        lvChat = (ListView) findViewById(R.id.lvChatMain);
        messageContainerList = new ArrayList<MessageContainer>();

        ParseQuery<MessageContainer> queryFromTo = ParseQuery.getQuery(MessageContainer.class)
                .whereEqualTo("userA", fromUser);

        ParseQuery<MessageContainer> queryToFrom = ParseQuery.getQuery(MessageContainer.class)
                .whereEqualTo("userB", fromUser);

        List<ParseQuery<MessageContainer>> queries = new ArrayList<ParseQuery<MessageContainer>>();
        queries.add(queryToFrom);
        queries.add(queryFromTo);

        ParseQuery<MessageContainer> mainQuery = ParseQuery.or(queries);
        // Configure limit and sort order
        mainQuery.orderByAscending("updatedAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        mainQuery.findInBackground(new FindCallback<MessageContainer>() {
            public void done(List<MessageContainer> messagesContainer, ParseException f) {
                if (f == null) {
                    messageContainerList.clear();
                    messageContainerList.addAll(messagesContainer);

                    lvChat.setAdapter(mAdapter);

                    // Scroll to the bottom of the list on initial load
                } else {
                    Log.d("message", "Error: " + f.getMessage());
                }
            }
        });



        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        mAdapter = new ChatMainAdapter(ChatMainActivity.this, fromUser, messageContainerList);




        System.out.println(" ");



    }

}

