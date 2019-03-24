package com.example.mobile_lab_2;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    private Spinner mSpnNumItems;
    private Spinner mSpnFetFrec;
    private EditText mTxtUrl;
    private Button mBtnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.mSpnNumItems = findViewById(R.id.spn_numItems);
        this.mSpnFetFrec = findViewById(R.id.spn_fetFrec);
        this.mTxtUrl = findViewById(R.id.txt_Url);
        this.mBtnDone = findViewById(R.id.btn_done);

        this.mBtnDone.setEnabled(false);
        registerEditText();

        ArrayAdapter<CharSequence> aNumItems = ArrayAdapter.createFromResource(this, R.array.spinner_vals, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> aFetchFrec = ArrayAdapter.createFromResource(this, R.array.frequency_vals, android.R.layout.simple_spinner_dropdown_item);
        this.mSpnNumItems.setAdapter(aNumItems);
        this.mSpnFetFrec.setAdapter(aFetchFrec);

        this.mBtnDone.setOnClickListener(v->{
            String freq = this.mSpnFetFrec.getSelectedItem().toString();
            Integer numItem = Integer.parseInt(this.mSpnNumItems.getSelectedItem().toString());
            Integer fr;
            SharedPreferences.Editor edit = getSharedPreferences(MainActivity.Pref_Name, MODE_PRIVATE).edit();
            edit.putString("URL", this.mTxtUrl.getText().toString());
            edit.putInt("NumItems", numItem);
            switch (freq){
                case "10min": fr = 10; break;
                case "60min": fr = 60; break;
                case "Once a day": fr = 1440; break;
                default: fr = 0; break;
            }
            edit.putInt("Frequency", fr);
            edit.apply();
            Log.d("SettingsActivity", this.mTxtUrl.getText().toString());
            Log.d("SettingsActivity", numItem.toString());
            Log.d("SettingsActivity", fr.toString());
            finish();
        });

    }

    private void registerEditText(){
        this.mTxtUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Checks if the button should be enabled
                checkEnable();
            }
        });
    }

    private void checkEnable(){
        String s = this.mTxtUrl.getText().toString();
        if(s.matches("")){
            this.mBtnDone.setEnabled(false);
        }
        else{
            this.mBtnDone.setEnabled(true);
        }
    }
}
