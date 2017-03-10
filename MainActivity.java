package com.example.ridge.upmod;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageButton home, search, upload, activity, profile;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

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
                Toast.makeText(MainActivity.this, "Home button is clicked!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Search button is clicked!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Upload button is clicked!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Activity button is clicked!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Profile button is clicked!", Toast.LENGTH_SHORT).show();
                home.setImageResource(R.drawable.home_icon);
                search.setImageResource(R.drawable.search_icon);
                upload.setImageResource(R.drawable.upload_icon);
                activity.setImageResource(R.drawable.activity_icon);
                profile.setImageResource(R.drawable.profile_icon_white);
            }
        });
    }
}
