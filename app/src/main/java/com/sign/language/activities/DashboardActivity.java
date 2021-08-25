package com.sign.language.activities;

import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sign.language.R;

public class DashboardActivity extends Activity {
    CardView textToSign,signToText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        textToSign=findViewById(R.id.text_to_sign_btn);
        signToText=findViewById(R.id.sign_to_text_btn);

        textToSign.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),SearchTextActivity.class)));

        signToText.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),SearchSignActivity.class)));

    }
}