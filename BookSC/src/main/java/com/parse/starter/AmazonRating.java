package com.parse.starter;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.starter.Model.Book;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

class AmazonRating extends AsyncTask {
    private Book mBook = null;
    public double rating = -1;

    public AmazonRating(Book book) {
        this.mBook = book;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        while(!isCancelled() && mBook.getRating() < 0) {
            // Get the JSON information from the page
            URL url = null;
            URLConnection con = null;
            InputStream in = null;

            try {
                url = new URL("https://api.gocart.ph/amazon/scraper");
                con = url.openConnection();
                con.setRequestProperty("isbn", mBook.getiSBN());
                con.setRequestProperty("title", mBook.getTitle());
                con.setRequestProperty("author", mBook.getAuthor());
                in = con.getInputStream();
            } catch (Exception e) {
                System.out.println("Exception: " + e.toString());
                mBook.setRating(-1);
            }

            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            String json = null;
            try {
                json = IOUtils.toString(in, encoding);
            } catch (IOException e) {
                System.out.println("Exception: " + e.toString());
                mBook.setRating(-1);
            }
            System.out.println("JSON:" + json);

            java.lang.reflect.Type mapType = new TypeToken<Map<String, Object>>() {
            }.getType();
            Gson gson = new Gson();
            Map<String, Object> scraped = gson.fromJson(json, mapType);
            if (scraped.containsKey("rating")) {
                mBook.setRating((double) scraped.get("rating"));
            } else {
                mBook.setRating(0);
            }
        }
        return null;
    }

}