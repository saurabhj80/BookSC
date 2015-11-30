package com.parse.starter.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Ritvik on 11/22/15.
 */
@ParseClassName("Message")
public class Message extends ParseObject {
    public User getFromUser() {
        return (User) getParseUser("fromUser");
    }


    public User getToUser(){
        return (User)getParseUser("toUser");
    }

    public String getBody() {
        return getString("body");
    }

    public void setFromUser(User userId) {
        put("fromUser", userId);
    }

    public void setToUser(User userId) {
        put("toUser", userId);
    }

    public void setBody(String body) {
        put("body", body);
    }
}