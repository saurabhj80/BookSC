/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.starter.Model.Book;
import com.parse.starter.Model.Message;
import com.parse.starter.Model.MessageContainer;
import com.parse.starter.Model.User;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    ParseObject.registerSubclass(Book.class);
    ParseObject.registerSubclass(User.class);
    ParseObject.registerSubclass(Message.class);
    ParseObject.registerSubclass(MessageContainer.class);
    Parse.initialize(this, "m9XC4t4RH5noubPQsbLCaztIpVl0wGODWk42hPbc", "HtmFTNqWJ6rBdBwziiCFAd7y6DIrxV5IyFyxysY2");

    ParseInstallation.getCurrentInstallation().saveInBackground();
  }
}
