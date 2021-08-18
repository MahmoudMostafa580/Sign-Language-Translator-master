/* Activity which shows 3 choices in the sign language dictionary */
package com.sign.language.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.content.Intent;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;

import com.sign.language.R;

public class SelectDictionaryActivity extends Activity implements OnClickListener {
    AppCompatImageButton homeBtn;
    CardView alphabetBtn,numbersBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dictionary);

        alphabetBtn=findViewById(R.id.alphabets_btn);
        numbersBtn=findViewById(R.id.numbers_btn);
        homeBtn=findViewById(R.id.home_btn);

        alphabetBtn.setOnClickListener(this);
        numbersBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.alphabets_btn:
                Intent alphabetsIntent = new Intent(SelectDictionaryActivity.this, SignLanguageAlphabetsActivity.class);
                startActivity(alphabetsIntent);
                break;

            case R.id.numbers_btn:
                Intent numbersIntent = new Intent(SelectDictionaryActivity.this, SignLanguageNumbersActivity.class);
                startActivity(numbersIntent);
                break;

            case R.id.home_btn:
                finish();
                break;
        }
    }
}
