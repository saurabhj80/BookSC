package com.parse.starter;

/**
 * Created by Chaitu on 11/17/15.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
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
import com.parse.starter.Model.Book;
import com.parse.starter.Model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Book> {

    private final Context context;
    private final ArrayList<Book> itemsArrayList;
    private final @LayoutRes
    int row;
    private Bitmap bookView;

    public MyAdapter(Context context, ArrayList<Book> itemsArrayList, @LayoutRes int resource) {

        super(context, resource, itemsArrayList);


        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.row = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(row, parent, false);

        // 3. Get the two text view from the rowView
        TextView userTextView = (TextView) rowView.findViewById(R.id.user_textView);
        TextView statusTextView = (TextView) rowView.findViewById(R.id.book_status_textview);
        TextView titleView = (TextView) rowView.findViewById(R.id.actual_title);
        TextView priceView = (TextView) rowView.findViewById(R.id.actual_price);
        TextView authorView = (TextView) rowView.findViewById(R.id.actual_author_name);
        TextView isbnView = (TextView) rowView.findViewById(R.id.actual_ISBN);
        TextView qualityView = (TextView) rowView.findViewById(R.id.actual_quality);
        TextView ratingView = (TextView) rowView.findViewById(R.id.actual_rating);

        final ImageView bookImg = (ImageView) rowView.findViewById(R.id.book_image);
        final Button messageButton = (Button) rowView.findViewById(R.id.button_message);

        // 4. Set the text for textView
        User user = itemsArrayList.get(position).getOwner();
        if (user != null && userTextView != null) {
            userTextView.setText(itemsArrayList.get(position).getOwner().getName());
        }

        Book book = itemsArrayList.get(position);
        String status = null;

        if (book.getStatus()) {
            status = "Selling";
        } else {
            status = "Buying";
        }

        // Because this text view does not exist on BUY and SELL FRAGMENT
        if (statusTextView != null) {
            statusTextView.setText(status);
        }

        //image processisng
        ParseFile image = (ParseFile) book.getPicture();

        if (image != null) image.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
            if (e == null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    bookImg.setImageBitmap(bmp);
                    }
                }

        });

        //chat processing

        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("FromUser", ParseUser.getCurrentUser().getObjectId());
                intent.putExtra("ToUser", itemsArrayList.get(position).getOwner().getObjectId());
                context.startActivity(intent);
            }
        };

        if(messageButton != null){
            messageButton.setOnClickListener(listener);
        }


        titleView.setText(book.getTitle());
        priceView.setText(book.getPrice()+"");
        authorView.setText(book.getAuthor());
        isbnView.setText(book.getiSBN());
        qualityView.setText(book.getQuality());

        if(ratingView != null) {
            if (book.getRating() <= 0) {
                ratingView.setText("None found");
            } else {
                ratingView.setText(book.getRating() + "");
            }
        }

        // 5. return rowView

        Button editButton = (Button)rowView.findViewById(R.id.button_edit);
        if (editButton != null) {
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CreateBook.class);
                    intent.putExtra("book", itemsArrayList.get(position).getObjectId());
                    context.startActivity(intent);
                }
            });
        }


        return rowView;
    }

}