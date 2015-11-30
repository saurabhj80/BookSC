package com.parse.starter;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.starter.Model.User;
import com.parse.starter.Utilities.ParseManager;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

  FragmentManager mFragmentManager;
  FragmentTransaction mFragmentTransaction;
  private  TabFragment tb;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

     tb = new TabFragment();

    /**
     * Lets inflate the very first fragment
     * Here , we are inflating the TabFragment as the first Fragment
     */

    mFragmentManager = getSupportFragmentManager();
    mFragmentTransaction = mFragmentManager.beginTransaction();
//    mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();

    mFragmentTransaction.replace(R.id.containerView,tb).commit();



    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


    TextView username = (TextView)navigationView.getHeaderView(0).findViewById(R.id.user_name);
    TextView email = (TextView)navigationView.getHeaderView(0).findViewById(R.id.user_email);

    User current = (User) ParseUser.getCurrentUser();

    if (current.getName() != null)
      username.setText(current.getName());
    else
      username.setText("Welcome");

    email.setText(current.getEmail());


   //load image into profile pic paste to line 70
    ParseFile image = current.getUserImage();
    final ImageView User_view = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.UserImageView);

    if(image != null) {
      image.getDataInBackground(new GetDataCallback() {
        @Override
        public void done(byte[] data, ParseException e) {
          if(e==null){
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            User_view.setImageBitmap(bmp);
          }
        }
      });
    }

    navigationView.setNavigationItemSelectedListener(this);
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    System.out.println("Called");

    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);

    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
    // Assumes current activity is the searchable activity
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
   // searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
//    if (id == R.id.action_settings) {
//      return true;
//    }
//    if (id == R.id.search) {
//      //onSearchRequested();
//      return true;
//    }


    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onPause(){
    super.onPause();
    tb.gaf.setUserVisibleHint(false);

  }

  @Override
  public void onResume(){
    super.onResume();


    if(tb.tabLayout.getSelectedTabPosition() ==1 ){
      tb.gaf.setUserVisibleHint(true);
    }

  }


  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();



    if (id == R.id.nav_chats){
      Intent intent = new Intent (this, ChatMainActivity.class);
      startActivity(intent);

    } else if(id == R.id.nav_signOut){
        ParseManager.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
