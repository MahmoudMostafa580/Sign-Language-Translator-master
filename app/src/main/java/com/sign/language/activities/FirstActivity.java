/* Activity which shows 3 choices in the sign language dictionary */
package com.sign.language.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.content.Intent;

import androidx.appcompat.widget.AppCompatImageButton;

import com.sign.language.R;

public class FirstActivity extends Activity implements OnClickListener {
    AppCompatImageButton alphabetBtn,numbersBtn,frequentlyUsedBtn,homeBtn,backBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        alphabetBtn =findViewById(R.id.alphabets_btn);
        numbersBtn=findViewById(R.id.numbers_btn);
        frequentlyUsedBtn=findViewById(R.id.frequently_used_btn);
        homeBtn=findViewById(R.id.home_btn);
        backBtn=findViewById(R.id.back_btn);

        alphabetBtn.setOnClickListener(this);
        numbersBtn.setOnClickListener(this);
        frequentlyUsedBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        /* Call MainActivity with appropriate choice */
        Intent intent = new Intent(FirstActivity.this, MainActivity.class);
        switch (v.getId())
        {
            case R.id.alphabets_btn:
                intent.putExtra("Userchoice", 1);
                startActivity(intent);
                finish();
                break;

            case R.id.numbers_btn:
                intent.putExtra("Userchoice", 2);
                startActivity(intent);
                finish();
                break;

            case R.id.frequently_used_btn:
                intent.putExtra("Userchoice", 3);
                startActivity(intent);
                finish();
                break;

            case R.id.back_btn:
                startActivity(new Intent(FirstActivity.this, SearchTextActivity.class));
                finish();
                break;

            case R.id.home_btn:
                startActivity(new Intent(FirstActivity.this, SearchTextActivity.class));
                finish();
                break;
        }
    }
}
