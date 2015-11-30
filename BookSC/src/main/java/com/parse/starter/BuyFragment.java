package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.starter.Model.Book;
import com.parse.starter.Utilities.ParseManager;

/**
 * Created by Ratan on 7/29/2015.
 */
public class BuyFragment extends Fragment {

    // Stores books to be posted to stream
    private ArrayList<Book> mBooks = new ArrayList<Book>();

    private MyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.buy_fragment_layout,container,false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateBook.class);
                intent.putExtra("Sell", false);
                startActivity(intent);
            }
        });

        // 1. pass context and data to the custom adapter
        adapter = new MyAdapter(getActivity(), mBooks, R.layout.buy_row);

        ParseManager.getMyBuyBooks(fillActivity);

        // 2. Get ListView from activity_main.xml
        ListView listView = (ListView)view.findViewById(R.id.listView);

        // 3. setListAdapter
        listView.setAdapter(adapter);

        return view;
    }

    private FindCallback fillActivity = new FindCallback<Book>() {
        @Override
        public void done(List<Book> books, ParseException e) {
            if (e == null) {
                mBooks.clear();
                //for(Book book : books) System.out.println("Title:"+book.getTitle());
                mBooks.addAll(books);

                for(Book book : mBooks) {
                    if(book.getRating() < 0) {
                        AmazonRating ar = new AmazonRating(book);
                        ar.execute();
                    }
                }

                adapter.notifyDataSetChanged(); // update adapter
            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // Update the query
            ParseManager.getMyBuyBooks(fillActivity);
        }
    }

}