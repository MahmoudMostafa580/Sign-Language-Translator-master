/* Activity which shows gridview of Alphabets/Numbers/Frequently used words based on choice */
package com.sign.language.activities;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.GridView;

import com.sign.language.R;
import com.sign.language.adapters.GridViewAdapter;

public class SignLanguageAlphabetsActivity extends Activity {
    private GridView gridView;
    private GridViewAdapter customGridAdapter;
    private int photos[]={
            R.drawable.frame_001,
            R.drawable.frame_002,
            R.drawable.frame_003,
            R.drawable.frame_004,
            R.drawable.frame_005,
            R.drawable.frame_006,
            R.drawable.frame_007,
            R.drawable.frame_008,
            R.drawable.frame_009,
            R.drawable.frame_010,
            R.drawable.frame_011,
            R.drawable.frame_012,
            R.drawable.frame_013,
            R.drawable.frame_014,
            R.drawable.frame_015,
            R.drawable.frame_016,
            R.drawable.frame_017,
            R.drawable.frame_018,
            R.drawable.frame_019,
            R.drawable.frame_020,
            R.drawable.frame_021,
            R.drawable.frame_022,
            R.drawable.frame_023,
            R.drawable.frame_024,
            R.drawable.frame_025,
            R.drawable.frame_026
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_language_alphabets);

        gridView = findViewById(R.id.gridView);
        customGridAdapter=new GridViewAdapter(getApplicationContext(),photos);
        gridView.setAdapter(customGridAdapter);

        gridView.setOnItemClickListener((parent, v, position, id) -> {
            /* Call DisplayActivity with image position */
            Intent displayIntent = new Intent(SignLanguageAlphabetsActivity.this, DisplayActivity.class);
            displayIntent.putExtra("Image Int", photos[position]);
            startActivity(displayIntent);
        });
    }

}
