/* Activity that does voice translation and sign language conversion */
package com.sign.language.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.sign.language.R;

public class SearchTextActivity extends Activity implements OnClickListener {
    private static final int REQUEST_CODE = 1234;
    private AppCompatImageView resultImageView;
    private TextInputLayout searchLayout;
    private AppCompatImageButton searchImageBtn,goToDictionaryImageBtn,voiceConverterImageBtn;

    private SpeechRecognizer sr;
    private Bitmap bitmap;
    private StringBuffer url;
    /* URLs list which will contain URLs for resultant image */
    private List<StringBuffer> mURLs = new LinkedList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_text);

        goToDictionaryImageBtn=findViewById(R.id.go_to_dictionary_imageBtn);
        resultImageView=findViewById(R.id.result_img);
        searchLayout=findViewById(R.id.search_layout);
        searchImageBtn=findViewById(R.id.search_imageBtn);
        voiceConverterImageBtn=findViewById(R.id.voice_converter_imageBtn);

        goToDictionaryImageBtn.setOnClickListener(this);
        resultImageView.setOnClickListener(this);
        searchImageBtn.setOnClickListener(this);
        voiceConverterImageBtn.setOnClickListener(this);

        initSpeechRecognizer();
    }
    /*
    * Onclick handlers for buttons on the Sign Language Conversion Activity
    */
    public void onClick(View v) {

        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        /* Voice recognition using Google Speech to Text API*/
        if (v.getId() == R.id.voice_converter_imageBtn) {

            Objects.requireNonNull(searchLayout.getEditText()).setText("");

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.sign.language");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak));
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            startActivityForResult(intent, REQUEST_CODE);

        }

        if (v.getId() == R.id.search_imageBtn) {

            inputManager = (InputMethodManager) this.getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            Toast.makeText(getApplicationContext(), R.string.message_processing, Toast.LENGTH_SHORT).show();

            /* ImageView which shows the result images*/
            String searchString;

            if (Objects.requireNonNull(searchLayout.getEditText()).getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.message_empty, Toast.LENGTH_SHORT).show();
                return;
            } else
                searchString = searchLayout.getEditText().getText().toString();
            /* Split the input sentence into words */
            String[] words = searchString.split("\\s+");

            /*Remove special characters and convert uppercase characters to lowercase characters*/
            for (int i = 0; i < words.length; i++) {
                words[i] = words[i].replaceAll("[^\\w]", "");
                words[i] = words[i].toLowerCase();
            }

            /*Create a URL for each word and it to the URLs list*/
            for (int i = 0; i < words.length; i++) {
                /*
                * "http://www.lifeprint.com/asl101/pages-signs/" + FIRST_CHAR + WORD + ".htm" has images for WORD
                * */
                url = new StringBuffer("https://www.lifeprint.com/asl101/pages-signs/");
                url.append(words[i].charAt(0));
                url.append('/');
                url.append(words[i]);
                url.append(".htm");
                mURLs.add(url);
            }
            try {
                loadNext();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        /* Open the Sign Language dictionary */
        if (v.getId() == R.id.go_to_dictionary_imageBtn) {
            Intent intent = new Intent(SearchTextActivity.this, SelectDictionaryActivity.class);
            startActivity(intent);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            /* matches will contain possible words for voice */
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String topResult;
            /* Choose the first word from the result and append to the edit TextBox */
            topResult = matches.get(0);
            Objects.requireNonNull(searchLayout.getEditText()).append(topResult);

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /* Initialize speech recognizer */
    private void initSpeechRecognizer() {
        if (sr == null) {
            sr = SpeechRecognizer.createSpeechRecognizer(this);
            /* Disable the voice translation button if speech recognition is not available in the phone */
            if (!SpeechRecognizer.isRecognitionAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Speech Recognition is not available in your phone,You have to enter text in edit box next to you", Toast.LENGTH_LONG).show();
                voiceConverterImageBtn.setEnabled(false);
            }
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }


    /* Removes URL one by one from the list and delegates to LoadImage method */
    private void loadNext() throws InterruptedException, ExecutionException {
        if (mURLs.isEmpty()) {
            return;
        }
        url = mURLs.remove(0);
        if (url != null) {
            new LoadImage().execute();
        }
    }

    private class LoadImage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... args) {
            /* if url is URL of number/alphabet */
            if(url.indexOf("numbers")>=0 || url.indexOf("fingerspelling")>=0){
                try {
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(url.toString()).getContent());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            /* if url is URL of a word */
            else {
                try {
                    /* Connect to the website using Jsoup and retrieve the first "jpg" image as Bitmap*/
                    Document doc = Jsoup.connect(url.toString()).ignoreContentType(true).get();
                    Element img = doc.select("img[src$=.jpg]").first();
                    String Str = img.attr("abs:src");
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(Str).getContent());
                }
                catch (UnknownHostException e) {
                    /* UnKnownHostException raised when there is no internet */
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(SearchTextActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch (Exception e) {
                    /* Exception is raised when the word is not there in lifeprint.com */
                    bitmap = null;
                    String t = url.toString();

                    /* Get the word from the URL */
                    String str = t.substring(46, t.lastIndexOf('.'));

                    /* Traverse the word from last and add URL for each character to the beginning of URLs list*/
                    for (int i = str.length() - 1; i >= 0; i--) {
                        /* If the character is a number use "https://www.lifeprint.com/asl101/signjpegs/numbers/number0" and add it to the beginning of URLs list */
                        if (str.charAt(i) <= '9' && str.charAt(i) >= '0')
                            mURLs.add(0, new StringBuffer("https://www.lifeprint.com/asl101/signjpegs/numbers/number0" + str.charAt(i) + ".jpg"));

                        /* If the character is an alphabet use "https://www.lifeprint.com/asl101/fingerspelling/abc-gifs/" and add it to the beginning of URLs list */
                        else
                            mURLs.add(0, new StringBuffer("https://www.lifeprint.com/asl101/fingerspelling/abc-gifs/" + str.charAt(i) + "_small.gif"));
                    }

                    /* Remove the newly added character URL and get the image as bitmap */
                    StringBuffer url = mURLs.remove(0);
                    try {
                        bitmap = BitmapFactory.decodeStream((InputStream) new URL(url.toString()).getContent());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            /* The bitmap is set to ImageView onPost downloading and loadNext method is called for next URL in the URLs list*/
            if (bitmap != null) {
                resultImageView.setImageBitmap(bitmap);
            }
            try {
                loadNext();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}

