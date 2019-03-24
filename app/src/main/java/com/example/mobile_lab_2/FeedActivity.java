package com.example.mobile_lab_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedActivity extends AppCompatActivity implements RssFeedListAdapter.OnNoteListener {

    private RecyclerView mRecyclerView;
    private EditText mSearch;
    public static ArrayList<RssFeedModel> Mlist;
    private final String TAG = "Somekeyfortestimg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleNews);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearch = findViewById(R.id.searchText);

        // here we will get the recyclerview
        Intent i = getIntent();
        Mlist = (ArrayList<RssFeedModel>)i.getSerializableExtra("RssList");

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mRecyclerView.setAdapter(new RssFeedListAdapter(Mlist, this));

    }

    @Override
    public void onNoteClick(int position) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Mlist.get(position).link));
        startActivity(i);
    }

    public ArrayList<RssFeedModel> filterList(String userInput) {
        if (!userInput.matches("")) {
            ArrayList<RssFeedModel> tmp = new ArrayList<RssFeedModel>();
            Pattern p = Pattern.compile("[A-Z]+");
            Matcher m = p.matcher(userInput.toUpperCase());

            while (m.find()) {
                for (int i = 0; i < Mlist.size(); i++) {
                    if (Mlist.get(i).title.toUpperCase().contains(m.group())) {
                        tmp.add(Mlist.get(i));
                    }
                }
            }
            //mRecyclerView.setAdapter(new RssFeedListAdapter(tmp, this));
            return tmp;
        } else {
            return Mlist;
            //mRecyclerView.setAdapter(new RssFeedListAdapter(Mlist, this));
        }
    }

    public void doFilter() {
        mRecyclerView.setAdapter(new RssFeedListAdapter(filterList(mSearch.getText().toString()), this));
    }


}