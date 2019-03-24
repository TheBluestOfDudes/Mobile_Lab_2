package com.example.mobile_lab_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mSettingButton;
    private Button mFeedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSettingButton = (Button) findViewById(R.id.btn_toSettings);
        this.mFeedButton = (Button) findViewById(R.id.btn_toFeeds);

        this.mSettingButton.setOnClickListener(v->{
            final Intent toSettings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(toSettings);
        });

        this.mFeedButton.setOnClickListener(v->{
            final Intent toFeeds = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(toFeeds);
        });
    }
}