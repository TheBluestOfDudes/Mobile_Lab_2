package com.example.mobile_lab_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    private Spinner mSpnNumItems;
    private Spinner mSpnFetFrec;
    private EditText mTxtUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.mSpnNumItems = findViewById(R.id.spn_numItems);
        this.mSpnFetFrec = findViewById(R.id.spn_fetFrec);
        this.mTxtUrl = findViewById(R.id.txt_Url);

        ArrayAdapter<CharSequence> aNumItems = ArrayAdapter.createFromResource(this, R.array.spinner_vals, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> aFetchFrec = ArrayAdapter.createFromResource(this, R.array.frequency_vals, android.R.layout.simple_spinner_dropdown_item);
        this.mSpnNumItems.setAdapter(aNumItems);
        this.mSpnFetFrec.setAdapter(aFetchFrec);

    }
}
