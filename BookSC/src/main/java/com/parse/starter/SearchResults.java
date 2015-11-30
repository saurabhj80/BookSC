package com.parse.starter;

import android.app.DownloadManager;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.starter.Model.Book;
import com.parse.starter.Utilities.ParseManager;

import java.util.ArrayList;
import java.util.List;

public class SearchResults extends AppCompatActivity {

    private ArrayList<Book> mBooks = new ArrayList<Book>();

    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())){
            adapter = new MyAdapter(this, mBooks, R.layout.guest_global_activity_row);

        }
        else{
            adapter = new MyAdapter(this, mBooks, R.layout.global_activity_row);
        }


        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);


        handleIntent(getIntent());
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        setIntent(intent);
//        handleIntent(intent);
//    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            System.out.println("Query");
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query){
        ParseManager.searchBookByTitle(query, fillActivity);
    }

    private FindCallback fillActivity = new FindCallback<Book>() {
        @Override
        public void done(List<Book> books, ParseException e) {
            if (e == null) {
                System.out.println(books.size());
                mBooks.clear();
                //for(Book book : books) System.out.println("Title:"+book.getTitle());
                mBooks.addAll(books);
                adapter.notifyDataSetChanged(); // update adapter
            }
        }
    };

}
