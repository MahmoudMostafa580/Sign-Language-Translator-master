package com.sign.language.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.sign.language.R;
import com.sign.language.adapters.GridViewAdapter;

public class SignLanguageNumbersActivity extends Activity {

    private GridView gridView;
    private GridViewAdapter customGridAdapter;
    private int photos[] = {
            R.drawable.num_1,
            R.drawable.num_2,
            R.drawable.num_3,
            R.drawable.num_4,
            R.drawable.num_5,
            R.drawable.num_6,
            R.drawable.num_7,
            R.drawable.num_8,
            R.drawable.num_9,
            R.drawable.num_10
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_language_numbers);

        gridView = findViewById(R.id.gridView);
        customGridAdapter = new GridViewAdapter(getApplicationContext(), photos);
        gridView.setAdapter(customGridAdapter);

        gridView.setOnItemClickListener((parent, v, position, id) -> {
            /* Call DisplayActivity with image position */
            Intent displayIntent = new Intent(SignLanguageNumbersActivity.this, DisplayActivity.class);
            displayIntent.putExtra("Image Int", photos[position]);
            startActivity(displayIntent);
        });
    }
}