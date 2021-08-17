/* Activity which shows the image of Alphabet/Number/Frequently used word */
package com.sign.language.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.sign.language.R;

public class DisplayActivity extends Activity implements OnClickListener {
    private ImageButton finishBtn;
    private AppCompatImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        imageView = findViewById(R.id.image_display);
        finishBtn = findViewById(R.id.Exit_button);

        Intent intent = getIntent();

        imageView.setImageResource(intent.getIntExtra("Image Int", 0));

        finishBtn.setOnClickListener(this);
    }

    public void onClick(View v) {

        if (v.getId() == R.id.Exit_button) {
            finish();
        }
    }
}
