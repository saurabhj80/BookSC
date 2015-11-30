package com.parse.starter.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Ritvik on 11/22/15.
 */
@ParseClassName("MessageContainer")
public class MessageContainer extends ParseObject {
    public User getUserA() {
        return (User) getParseUser("userA");
    }


    public User getUserB(){
        return (User)getParseUser("userB");
    }

    public String getBody() {
        return getString("body");
    }

    public void setUserA(User userId) {
        put("userA", userId);
    }

    public void setUserB(User userId) {
        put("userB", userId);
    }

    public void setBody(String body) {
        put("body", body);
    }
}