package com.parse.starter;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.Model.Message;
import com.parse.starter.Model.MessageContainer;
import com.parse.starter.Model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = ChatActivity.class.getName();
    private static User sUserId;

    public static final String USER_ID_KEY = "userId";
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;


    private EditText etMessage;
    private Button btSend;


    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;

    private String fromId;
    private String toId;
    private User fromUser;
    private User toUser;

    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getActionBar()!=null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }


//        ActionBar ab = getActionBar();
////        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#b31913"));
////        ab.setBackgroundDrawable(colorDrawable);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        startWithCurrentUser();
        setupMessagePosting();
        this.fromId= getIntent().getStringExtra("FromUser");
        this.toId= getIntent().getStringExtra("ToUser");
        this.fromUser=(User)ParseUser.getCurrentUser();


        System.out.println(" ");
        System.out.println(toId);
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        ParseQuery<ParseUser> query = ParseUser.getQuery()
                .whereEqualTo("objectId", toId);
        List<ParseUser> list= null;
        try {
            list = query.find();
            if (list != null && list.size() > 0) {
                this.toUser =(User) list.get(0);
                mAdapter.setToUser(toUser);
                handler.postDelayed(runnable, 100);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 5000);
        }
    };

    private void refreshMessages() {
        receiveMessage();
    }

    // Get the userId from the cached currentUser object
    private void startWithCurrentUser() {
        sUserId =(User) ParseUser.getCurrentUser();
    }


    // Setup button event handler which posts the entered message to Parse
    private void setupMessagePosting() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        mMessages = new ArrayList<Message>();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        mAdapter = new ChatListAdapter(ChatActivity.this, sUserId, mMessages);
        lvChat.setAdapter(mAdapter);

        }

    public void sendMessage(View view){

        ParseQuery<MessageContainer> queryFromTo = ParseQuery.getQuery(MessageContainer.class)
                .whereEqualTo("userA", fromUser)
                .whereEqualTo("userB", toUser);
        ParseQuery<MessageContainer> queryToFrom = ParseQuery.getQuery(MessageContainer.class)
                .whereEqualTo("userA", toUser)
                .whereEqualTo("userB", fromUser);

        List<ParseQuery<MessageContainer>> queries = new ArrayList<ParseQuery<MessageContainer>>();
        queries.add(queryToFrom);
        queries.add(queryFromTo);


        ParseQuery<MessageContainer> mainQuery = ParseQuery.or(queries);
        // Configure limit and sort order
        mainQuery.setLimit(1);
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        mainQuery.findInBackground(new FindCallback<MessageContainer>() {
            public void done(List<MessageContainer> messagesContainer, ParseException f) {
                if (f == null) {
                    if(messagesContainer.size()==0) {
                        MessageContainer newMC=new MessageContainer();
                        newMC.setUserA(fromUser);
                        newMC.setUserB(toUser);
                        newMC.saveInBackground();
                    }
                } else {
                    Log.d("message", "Error: " + f.getMessage());
                }
            }
        });



        String body = etMessage.getText().toString();
        // Use Message model to create new messages now
        Message message = new Message();
        message.setFromUser((User) ParseUser.getCurrentUser());

        message.setToUser(toUser);

        message.setBody(body);
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                receiveMessage();
            }
        });
        etMessage.setText("");
    }

    private void receiveMessage() {



        // Construct query to execute
        ParseQuery<Message> queryFromTo = ParseQuery.getQuery(Message.class)
            .whereEqualTo("fromUser", toUser)
            .whereEqualTo("toUser",fromUser);

        ParseQuery<Message> queryToFrom = ParseQuery.getQuery(Message.class)
                .whereEqualTo("toUser", toUser)
                .whereEqualTo("fromUser", fromUser);

        List<ParseQuery<Message>> queries = new ArrayList<ParseQuery<Message>>();
        queries.add(queryToFrom);
        queries.add(queryFromTo);


        ParseQuery<Message> mainQuery = ParseQuery.or(queries);
        // Configure limit and sort order
        mainQuery.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        mainQuery.orderByAscending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        mainQuery.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }




    }