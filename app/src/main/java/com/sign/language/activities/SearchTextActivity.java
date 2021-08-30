/* Activity that does voice translation and sign language conversion */
package com.sign.language.activities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.sign.language.R;

public class SearchTextActivity extends Activity implements OnClickListener {
    private static final int REQUEST_CODE = 1234;
    String searchString;
    Uri videoUri;
    List<Uri> videosUri = new ArrayList<>();
    private VideoView videoView;
    private TextInputLayout searchLayout;
    ImageView help_img;
    LinearLayout reply;
    FloatingActionButton fab;
    private AppCompatImageButton searchImageBtn, goToDictionaryImageBtn, voiceConverterImageBtn;
    private SpeechRecognizer sr;
    private StringBuffer url;
    int size;
    String lastSearch = "";
    /* URLs list which will contain URLs for resultant video */
    public List<StringBuffer> mURLs = new LinkedList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_text);

        goToDictionaryImageBtn = findViewById(R.id.go_to_dictionary_imageBtn);
        videoView = findViewById(R.id.videoView);
        searchLayout = findViewById(R.id.search_layout);
        searchImageBtn = findViewById(R.id.search_imageBtn);
        voiceConverterImageBtn = findViewById(R.id.voice_converter_imageBtn);
        help_img = findViewById(R.id.help_img);
        reply=findViewById(R.id.reply);
        fab=findViewById(R.id.fab);

        goToDictionaryImageBtn.setOnClickListener(this);
        searchImageBtn.setOnClickListener(this);
        voiceConverterImageBtn.setOnClickListener(this);
        reply.setOnClickListener(this);
        fab.setOnClickListener(this);

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
        if (v.getId() == R.id.fab){
            help_img.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            reply.setVisibility(View.GONE);
            videoView.setVideoURI(videosUri.get(0));
            videoView.start();
            it = 1;
            videoView.setOnCompletionListener(mp -> {
                if (it == videosUri.size())
                    return;
                videoView.setVideoURI(videosUri.get(it));
                videoView.start();
                it++;
            });
        }

        if (v.getId() == R.id.reply){
            help_img.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            reply.setVisibility(View.GONE);
            videoView.setVideoURI(videosUri.get(0));
            videoView.start();
            it = 1;
            videoView.setOnCompletionListener(mp -> {
                if (it == videosUri.size())
                    return;
                videoView.setVideoURI(videosUri.get(it));
                videoView.start();
                it++;
            });
        }


        if (v.getId() == R.id.search_imageBtn) {
            searchString = Objects.requireNonNull(searchLayout.getEditText()).getText().toString();

            if (searchString.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.message_empty, Toast.LENGTH_SHORT).show();
                return;
            } else {

                if (searchString.equals(lastSearch)) {
                    help_img.setVisibility(View.GONE);
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(videosUri.get(0));
                    videoView.start();
                    it = 1;
                    videoView.setOnCompletionListener(mp -> {
                        if (it == videosUri.size())
                            return;
                        videoView.setVideoURI(videosUri.get(it));
                        videoView.start();
                        it++;
                    });
                } else {
                    Toast.makeText(getApplicationContext(), R.string.message_processing, Toast.LENGTH_SHORT).show();
                    processingInput();
                    new LoadVideo().execute();
                }
                lastSearch = searchString;
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


    private void processingInput() {

        mURLs = new LinkedList<>();
        videosUri = new ArrayList<>();
        /* Split the input sentence into words */
        String[] words = searchString.split("\\s+");


        /*Remove special characters and convert uppercase characters to lowercase characters*/
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("[^\\w]", "");
            words[i] = words[i].toLowerCase();
        }

        /*Create a URL for each word and add it to the URLs list*/
        for (int i = 0; i < words.length; i++) {
            if (words[i].length() == 1 && Character.isLetter(words[i].charAt(0)) && !words[i].equals("i")) {

                String videosUrl = "https://media.spreadthesign.com/video/mp4/13/alphabet-letter-"
                        +
                        (((int) words[i].charAt(0)) - 97 + 591)
                        + "-1.mp4";
                url = new StringBuffer(videosUrl);
                mURLs.add(url);
            } else {
                boolean isNum = true;
                for (int j = 0; j < words[i].length(); j++) {
                    if (Character.isLetter(words[i].charAt(j))) {
                        isNum = false;
                        break;
                    }
                }
                if (isNum) {

                    List<String> numbers = new ArrayList<>();
                    int num = Integer.parseInt(words[i]);
                    if (num <= 100) {
                        url = new StringBuffer("https://www.spreadthesign.com/en.us/search/?q=");
                        url.append(words[i]);
                        mURLs.add(url);
                    } else {
                        numbers.add(String.valueOf(Integer.parseInt(words[i].substring(words[i].length() - 2))));
                        for (int j = words[i].length() - 3; j >= 0; j--) {
                            numbers.add(j == 0 && words[i].length() == 4 ? words[i].charAt(j) + "000" :
                                    words[i].charAt(j) + "00");
                        }
                        Collections.reverse(numbers);
                        for (int j = 0; j < numbers.size(); j++) {
                            url = new StringBuffer("https://www.spreadthesign.com/en.us/search/?q=");
                            url.append(numbers.get(j));
                            mURLs.add(url);
                        }
                    }

                } else {
                    url = new StringBuffer("https://www.spreadthesign.com/en.us/search/?q=");
                    url.append(words[i]);
                    mURLs.add(url);
                }
            }
        }
        size = mURLs.size();
    }

    /* Removes URL one by one from the list and delegates to LoadImage method */
    private void loadNext() throws InterruptedException, ExecutionException {
        url = mURLs.remove(0);

        if (mURLs.isEmpty()) {
            return;
        }
        if (url != null) {
            new LoadVideo().execute();
        }
    }


    int it = 1;

    private class LoadVideo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... args) {

            try {
                loadNext();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            /* if url is URL of a word */
            try {
                /* Connect to the website using Jsoup and retrieve the video result*/
                Log.w("IN", "" + url.toString());
                if (url.toString().contains("alphabet-letter")) {
                    videosUri.add(Uri.parse(url.toString()));
                } else {
                    Document doc = Jsoup.connect(url.toString()).ignoreContentType(true).get();
                    Elements videoE = doc.select("video");
                    String videoSearchUrl = videoE.attr("abs:src");
                    if (!videoE.hasAttr("abs:src")){
                        String t = url.toString();
                        /* Get the word from the URL */
                        String str = t.substring(46);
                        Log.w("str",str);
                        for (int i = 0; i < str.length() ; i++) {
                            videosUri.add(Uri.parse("https://media.spreadthesign.com/video/mp4/13/alphabet-letter-"
                                    +
                                    (((int) str.charAt(i)) - 97 + 591)
                                    + "-1.mp4"));
                            Log.w("str",str.charAt(i)+"");
                        }
                    }else{
                        Log.v("video url: ", videoSearchUrl + " , " + videoE.hasAttr("abs:src") + " , " + videoE.hasAttr("src"));
                        videosUri.add(Uri.parse(videoSearchUrl));
                    }

                }
            } catch (UnknownHostException e) {
                /* UnKnownHostException raised when there is no internet */
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SearchTextActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                Log.w("LOOOOG", "" + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if ( mURLs.isEmpty()) {
                help_img.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);

                videoView.setVideoURI(videosUri.get(0));
                videoView.start();
                it = 1;
                videoView.setOnCompletionListener(mp -> {
                    if (it == videosUri.size()){
                        reply.setVisibility(View.VISIBLE);
                        return;
                    }

                    Log.w("Vid", videosUri.get(it) + "");
                    videoView.setVideoURI(videosUri.get(it));
                    videoView.start();
                    it++;
                });
            }
        }
    }
}

