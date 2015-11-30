package com.parse.starter;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.starter.Model.Book;
import com.parse.starter.Utilities.ParseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ratan on 7/29/2015.
 */
public class GlobalActivityFragment extends Fragment implements Runnable {

    // Create a handler which can run code periodically
    //private Handler handler = new Handler();

    // Stores books to be posted to stream
    private ArrayList<Book> mBooks = new ArrayList<Book>();

    private MyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.global_activity_layout, container, false);

        // 1. pass context and data to the custom adapter
        adapter = new MyAdapter(getActivity(), mBooks, R.layout.global_activity_row);

        ParseManager.getRecentActivity(fillActivity);

        // 2. Get ListView from activity_main.xml
        ListView listView = (ListView) view.findViewById(R.id.listView);

        // 3. setListAdapter
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void run() {
        while (visible) {
            System.out.println("Fetching");

            ParseManager.getRecentActivity(fillActivity);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    // Defines a runnable which is run every second
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            ParseManager.getRecentActivity(fillActivity);
//            handler.postDelayed(this, 5000);
//        }
//    };

    private FindCallback fillActivity = new FindCallback<Book>() {
        @Override
        public void done(List<Book> books, ParseException e) {
            if (e == null) {
                mBooks.clear();
                //for(Book book : books) System.out.println("Title:"+book.getTitle());
                mBooks.addAll(books);

                for(Book book : mBooks) {
                    if(book.getRating() < 0 && visible) {
                        AmazonRating ar = new AmazonRating(book);
                        ar.execute();
                    }
                }

                adapter.notifyDataSetChanged(); // update adapter
            }
        }
    };

    // if we are visible on screen
    private boolean visible = false;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        visible = isVisibleToUser;
        // Make sure that we are currently visible
        if (isVisibleToUser) {
            Thread thread = new Thread(this);
            thread.start();
        }
    }





}
