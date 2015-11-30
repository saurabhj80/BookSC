package com.parse.starter.Utilities;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.Model.Book;
import com.parse.starter.Model.User;

/**
 * Created by Ritvik on 11/20/15.
 */
public class ParseManager {

    /*
        LOGIN
     */

    // Sign up
    public static boolean createUser(String username, String password) {

        if (username == null || password == null) return false;
        if (username.equals("") || password.equals("")) return false;

        // Create a new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(username);

        try {
            user.signUp();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Edit the user
    public static void editUser(String name, String number, ParseFile image) {
        User current = (User)ParseUser.getCurrentUser();
        current.setName(name);
        current.setNumber(number);

        // If we have an image
        if (image != null) {
            current.setUserImage(image);
        }

        current.saveInBackground();
    }

    // Log in
    public static boolean loginUser(String username, String password) {

        if (username == null || password == null) return false;
        if (username.equals("") || password.equals("")) return false;

        try {
            ParseUser.logIn(username, password);
            return true;
        } catch (ParseException e) {
            // login unsuccessful
            return false;
        }
    }

    // Sign out
    public static void signOut() {
        ParseUser.logOut();
    };

    // Guest Login
    public static void guestLogin(LogInCallback callback) {
        ParseAnonymousUtils.logIn(callback);
    };


    /*
        BOOKS
     */

    public static void getMyBuyBooks(FindCallback callback){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Book")
                .whereEqualTo("user", ParseUser.getCurrentUser())
                .whereEqualTo("isSelling", false)
                .orderByAscending("updatedAt");
        query.findInBackground(callback);
    };

    public static void getMySellBooks(FindCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Book")
                .whereEqualTo("user", ParseUser.getCurrentUser())
                .whereEqualTo("isSelling", true)
                .orderByAscending("updatedAt");
        query.findInBackground(callback);
    };

    public static void createBook (Book book) {
        book.setTitle(book.getTitle().toLowerCase());
        book.saveInBackground();
    };

    public static void editBook(Book book, String name, String author, String ISBN,
                                String course, ParseFile picture, String quality,
                                int price){
        book.setTitle(name.toLowerCase());
        book.setAuthor(author);
        book.setiSBN(ISBN);
        book.setPicture(picture);
        book.setQuality(quality);
        book.saveInBackground();
    };

    public static void searchBookByTitle(String title, FindCallback callback){
        ParseQuery query = ParseQuery.getQuery(Book.class);
        //query.whereEqualTo("title", title.toLowerCase());
        query.whereContains("title", title.toLowerCase());
        query.findInBackground(callback);
    };

    public static void getRecentActivity(FindCallback callback){
        ParseQuery<ParseObject> query;
        query = ParseQuery.getQuery("Book")
                .orderByDescending("updatedAt")
                .setLimit(15);
        query.include("user");
        query.findInBackground(callback);
    };

    public static void getInterested(Book book){};

    public static void addInterested(Book book){};


    /*
        CHAT
     */

    public static void createChat(User userFrom, User userTo){};
    public static void sendMsg(User userFrom, User userTo){};

}
