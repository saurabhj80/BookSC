package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.starter.Model.MessageContainer;
import com.parse.starter.Model.User;

import java.util.List;

/**
 * Created by Ritvik on 11/22/15.
 */
public class ChatMainAdapter extends ArrayAdapter<MessageContainer>{

    private User mUserId;
    private List<MessageContainer> messagesContainers;
    private User toUser;
    private final Context context;
    public ChatMainAdapter(Context context, User userId, List<MessageContainer> messages) {
        super(context, 0, messages);
        this.mUserId = userId;
        this.messagesContainers = messages;
        this.context=context;
        System.out.println(" asd asd a "+ messages.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.chatmainitem, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.image = (ImageView)convertView.findViewById(R.id.user_image);
            holder.name = (TextView)convertView.findViewById(R.id.user_email);
            //holder.openButton = (Button)convertView.findViewById(R.id.button_open);
            holder.deleteButton = (Button)convertView.findViewById(R.id.button_delete);
            convertView.setTag(holder);

        }

//




//        for(int i=0; i<3; i++){
//            System.out.println("");
//            System.out.println(messagesContainers.get(i).getUserB().getUsername()+" is B");
//            System.out.println(messagesContainers.get(i).getUserA().getUsername()+" is A");
//            System.out.println("");
//        }


        final ViewHolder holder = (ViewHolder)convertView.getTag();
       User userCurr=null;/// The other person in the chat
        if(messagesContainers.get(position).getUserA().equals(ParseUser.getCurrentUser())){
            userCurr=messagesContainers.get(position).getUserB();
            //System.out.println(messagesContainers.get(position).getUserB().getUsername()+" is B");


        }else {
            userCurr=messagesContainers.get(position).getUserA();
            //System.out.println(messagesContainers.get(position).getUserB().getUsername()+" is A");

        }



        final User temp=userCurr;
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("FromUser", ParseUser.getCurrentUser().getObjectId().toString());
                intent.putExtra("ToUser", temp.getObjectId().toString());
                context.startActivity(intent);
            }
        };

       //holder.openButton.setOnClickListener(listener);
        final int pos=position;
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesContainers.get(pos).deleteInBackground();
            }
        });

        System.out.println(userCurr.getObjectId());

        try {
            userCurr.fetchIfNeeded();

            holder.name.setText(userCurr.getUsername());

            ParseFile image = temp.getUserImage();
//        final ImageView User_view = (ImageView) parent.findViewById(R.id.user_image);




            if(image != null) {
                image.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                            holder.image.setImageBitmap(bmp);

                        }
                    }
                });
            }



        } catch (ParseException e) {
            e.printStackTrace();
        }
        convertView.setClickable(true);
        convertView.setOnClickListener(listener);

//        convertView.setong
//
//        convertView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if(event.getAction() == Motion)
//                return false;
//            }
//        });

//        holder.image.setImageBitmap((BitMap)userCurr.getUserImage());
        return convertView;
    }


    final class ViewHolder {
        //public Button openButton;
        public Button deleteButton;
        public ImageView image;
        public TextView name;
    }

}

