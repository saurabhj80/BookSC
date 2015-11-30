package com.parse.starter.Model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.List;

/**
 * Created by saurabhj80 on 11/18/15.
 */

@ParseClassName("Book")
public class Book extends ParseObject {

    private double rating = -1;
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    // Name
    private String title;
    public String getTitle() { return getString("title"); }
    public void setTitle(String name) { put("title", name); }

    // Author
    private String author;
    public String getAuthor() { return getString("author"); }
    public void setAuthor(String author) { put("author", author); }

    // ISBN
    private String iSBN;
    public String getiSBN() { return getString("iSBN"); }
    public void setiSBN(String iSBN) { put("iSBN", iSBN); }

//    // Course
//    private String course;
//    public String getCourse() {return getString("course");}
//    public void setCourse(String course) {put("course", course);}

    // Picture
    private ParseFile picture;
    public ParseFile getPicture() {return getParseFile("picture");}
    public void setPicture(ParseFile picture) { put("picture", picture);}

    // Quality
    private String quality;
    public String getQuality() {return getString("quality");}
    public void setQuality(String quality) {put("quality", quality);}


    // Price
    private String price;
    public String getPrice() {return getString("price");}
    public void setPrice(String price) {put("price", price);}

//    // Negotiable
//    private boolean negotiable = true;
//    public boolean isNegotiable() {return getBoolean("negotiable");}
//    public void setNegotiable(boolean negotiable) {put("negotiable", negotiable);}

    // Owner
    private User owner;
    public User getOwner() {return (User)getParseUser("user");}
    public void setOwner(User owner) {put("user", owner);}

    // Status
    private boolean isSelling;
    public boolean getStatus() {return getBoolean("isSelling");}
    public void setStatus(boolean status) {put("isSelling", status);}

    // Interested user
    private List<ParseUser> interestedUsers;
    public List<ParseUser> getInterestedUsers() {return getList("interestedUsers");}
    public void setInterestedUsers(List<ParseUser> interestedUsers) {put("interestedUsers", interestedUsers);}

//    private String message;
//    public String getMessage() {return getString("message");}
//    public void setMessage(String message) {put("message", message);}

    public Book() {}
    public Book(String Title, String ISBN, String author) {
        super();
        this.setTitle(Title);
        this.setiSBN(ISBN);
        this.setAuthor(author);
    };

}