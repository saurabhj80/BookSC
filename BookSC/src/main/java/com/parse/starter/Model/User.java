package com.parse.starter.Model;

import com.parse.ParseClassName;
import com.parse.ParseUser;
import java.util.List;
import com.parse.ParseFile;

import com.parse.starter.Model.Book;

/**
 * Created by saurabhj80 on 11/18/15.
 */

@ParseClassName("_User")
public class User extends ParseUser {

    public User() {}

    private String name; //Mandatory
    public String getName() { return getString("name"); }
    public void setName(String name) { put("name",name); }

//    private String lastName; //Mandatory
//    public String getLastName() { return getString("lastName"); }
//    public void setLastName(String lastName) { put("lastName",lastName); }

    private ParseFile userImage; //Mandatory
    public ParseFile getUserImage() { return getParseFile("userImage"); }
    public void setUserImage(ParseFile userImage) { put("userImage", userImage); }

    private List<Book> booksToBuy;
    public List<Book> getBooksToBuy() {return getList("booksToBuy");}
    public void setBooksToBuy(List<Book> booksToBuy) {put("booksToBuy", booksToBuy);}

    private List<Book> booksToSell;
    public List<Book> getBooksToSell() {return getList("booksToSell");}
    public void setBooksToSell(List<Book> booksToBuy) {put("booksToSell", booksToSell);}

    private String number;
    public String getNumber() { return getString("number"); }
    public void setNumber(String number) { put("number", number); }
}
