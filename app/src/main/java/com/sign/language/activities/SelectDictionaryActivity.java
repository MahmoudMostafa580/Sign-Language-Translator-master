/* Activity which shows 3 choices in the sign language dictionary */
package com.sign.language.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.content.Intent;

import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.button.MaterialButton;
import com.sign.language.R;

public class SelectDictionaryActivity extends Activity implements OnClickListener {
    AppCompatImageButton homeBtn;
    MaterialButton alphabetBtn,numbersBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dictionary);

        alphabetBtn =findViewById(R.id.alphabets_btn);
        numbersBtn=findViewById(R.id.numbers_btn);
        homeBtn=findViewById(R.id.home_btn);

        alphabetBtn.setOnClickListener(this);
        numbersBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        /* Call MainActivity with appropriate choice */
        Intent intent = new Intent(SelectDictionaryActivity.this, SignLanguageSymbolsActivity.class);
        switch (v.getId())
        {
            case R.id.alphabets_btn:
                intent.putExtra("Userchoice", 1);
                startActivity(intent);
                break;

            case R.id.numbers_btn:
                intent.putExtra("Userchoice", 2);
                startActivity(intent);
                break;

            case R.id.home_btn:
                startActivity(new Intent(SelectDictionaryActivity.this, SearchTextActivity.class));
                finish();
                break;
        }
    }
}
