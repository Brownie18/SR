package com.example.ridge.upmod;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Ash on 3/5/17.
 */

public class navBarActivity extends Activity{
    ImageButton home, search, upload, activity, profile;

    public navBarActivity() {
        addListenerOnHomeButton();
        addListenerOnSearchButton();
        addListenerOnUploadButton();
        addListenerOnActivityButton();
        addListenerOnProfileButton();

    }

    public void onCreate() {
        addListenerOnHomeButton();
        addListenerOnSearchButton();
        addListenerOnUploadButton();
        addListenerOnActivityButton();
        addListenerOnProfileButton();
    }

    public void addListenerOnHomeButton() {
        home = (ImageButton) findViewById(R.id.homeButtonGray);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(navBarActivity.this, "Home button is clicked!", Toast.LENGTH_SHORT).show();
                home.setImageResource(R.drawable.home_icon_white);
                search.setImageResource(R.drawable.search_icon);
                upload.setImageResource(R.drawable.upload_icon);
                activity.setImageResource(R.drawable.activity_icon);
                profile.setImageResource(R.drawable.profile_icon);
            }
        });
    }

    public void addListenerOnSearchButton() {
        search = (ImageButton) findViewById(R.id.searchButtonGray);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(navBarActivity.this, "Search button is clicked!", Toast.LENGTH_SHORT).show();
                home.setImageResource(R.drawable.home_icon);
                search.setImageResource(R.drawable.search_icon_white);
                upload.setImageResource(R.drawable.upload_icon);
                activity.setImageResource(R.drawable.activity_icon);
                profile.setImageResource(R.drawable.profile_icon);
            }
        });
    }

    public void addListenerOnUploadButton() {
        upload = (ImageButton) findViewById(R.id.uploadButtonGray);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(navBarActivity.this, "Upload button is clicked!", Toast.LENGTH_SHORT).show();
                home.setImageResource(R.drawable.home_icon);
                search.setImageResource(R.drawable.search_icon);
                upload.setImageResource(R.drawable.upload_icon_white);
                activity.setImageResource(R.drawable.activity_icon);
                profile.setImageResource(R.drawable.profile_icon);
            }
        });
    }

    public void addListenerOnActivityButton() {
        activity = (ImageButton) findViewById(R.id.activityButtonGray);
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(navBarActivity.this, "Activity button is clicked!", Toast.LENGTH_SHORT).show();
                home.setImageResource(R.drawable.home_icon);
                search.setImageResource(R.drawable.search_icon);
                upload.setImageResource(R.drawable.upload_icon);
                activity.setImageResource(R.drawable.activity_icon_white);
                profile.setImageResource(R.drawable.profile_icon);
            }
        });
    }

    public void addListenerOnProfileButton() {
        profile = (ImageButton) findViewById(R.id.profileButtonGray);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(navBarActivity.this, "Profile button is clicked!", Toast.LENGTH_SHORT).show();
                home.setImageResource(R.drawable.home_icon);
                search.setImageResource(R.drawable.search_icon);
                upload.setImageResource(R.drawable.upload_icon);
                activity.setImageResource(R.drawable.activity_icon);
                profile.setImageResource(R.drawable.profile_icon_white);
            }
        });
    }
}
