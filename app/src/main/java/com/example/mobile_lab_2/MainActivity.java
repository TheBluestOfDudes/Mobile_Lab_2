package com.example.mobile_lab_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.Button;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button mSettingButton;
    private Button mFeedButton;
    public static String Pref_Name = "MyPreferences";

    private ArrayList<RssFeedModel> mFeedModelList;
    private String mFeedTitle;
    private String mFeedLink;
    private String mFeedDescription;

    private Timer timer;
    private TimerTask fre;

    private boolean firstFetch;
    private boolean scheduled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSettingButton = (Button) findViewById(R.id.btn_toSettings);
        this.mFeedButton = (Button) findViewById(R.id.btn_toFeeds);

        this.firstFetch = true;

        this.mSettingButton.setOnClickListener(v->{
            final Intent toSettings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(toSettings);
            this.mFeedButton.setEnabled(true);
        });

        this.mFeedButton.setOnClickListener(v->{

            if(this.firstFetch){
                this.scheduled = true;
                this.timer = new Timer();
                this.fre = new TimerTask(){
                    @Override
                    public void run(){
                        FetchURL();
                    }
                };

                timer.schedule(fre, 1, getSharedPreferences(Pref_Name, MODE_PRIVATE).getInt("Frequency",0) * 60000);
            }
            else{
                final Intent toFeed = new Intent(MainActivity.this, FeedActivity.class);
                toFeed.putExtra("RssList", mFeedModelList);
                startActivity(toFeed);
            }
        });

        if(!getSharedPreferences(Pref_Name, MODE_PRIVATE).contains("URL")){
            mFeedButton.setEnabled(false);
        }
    }

    public void FetchURL(){
        new FetchFeedTask(MainActivity.this).execute((Void) null);
    }

    public ArrayList<RssFeedModel> parseFeed(InputStream inputStream, Integer amount) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        ArrayList<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT && amount > 0) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }

                if (title != null && link != null && description != null) {
                    if(isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description);
                        items.add(item);
                        amount--;
                    }
                    else {
                        mFeedTitle = title;
                        mFeedLink = link;
                        mFeedDescription = description;
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;
        private Context ctx;

        private FetchFeedTask(Context context) {
            this.ctx = context.getApplicationContext();
        }

        @Override
        protected void onPreExecute() {
            mFeedTitle = null;
            mFeedLink = null;
            mFeedDescription = null;

            SharedPreferences rss;
            rss = getSharedPreferences(Pref_Name, MODE_PRIVATE);
            urlLink = rss.getString("URL", "");

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "https://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                SharedPreferences rss;
                rss = getSharedPreferences(Pref_Name, MODE_PRIVATE);
                Integer amount = rss.getInt("NumItems", 0);
                mFeedModelList = parseFeed(inputStream, amount);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                if(scheduled){
                    final Intent toFeed = new Intent(MainActivity.this, FeedActivity.class);
                    toFeed.putExtra("RssList", mFeedModelList);
                    startActivity(toFeed);
                    scheduled = false;
                }
            } else {
                Toast.makeText(MainActivity.this,
                        "Enter a valid Rss feed url",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}