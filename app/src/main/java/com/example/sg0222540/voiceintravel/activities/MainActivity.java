package com.example.sg0222540.voiceintravel.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sg0222540.voiceintravel.R;
import com.example.sg0222540.voiceintravel.utilities.Utility;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int USER_AUTHETICATED = 777;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int CHECK_CODE = 0x1;
    private final int LONG_DURATION = 5000;
    private final int SHORT_DURATION = 1200;
    private Button btnAuthenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeTTS();
        /*if(Utility.userLogin){
        initializeTTS();}
        else {
            Intent goToLoginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToLoginIntent);
        }*/
        getActionBar().hide();


    }

    private void initializeTTS() {
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnAuthenticate = (Button) findViewById(R.id.btnAuthenticate);
        txtSpeechInput.setVisibility(View.VISIBLE);
        btnSpeak.setVisibility(View.VISIBLE);
        btnSpeak.setOnClickListener(this);
        btnAuthenticate.setVisibility(View.GONE);
        //btnAuthenticate.setOnClickListener(this);
        checkTTS();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        speaker.destroy();
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result.get(0).equals("yes")) {
                        Utility.performAction(true);
                    } else {
                        Utility.performAction(false);
                    }
                    txtSpeechInput.setText(result.get(0));
                }
                break;
            }
            case CHECK_CODE: {
               /* if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    speaker = new Speaker(this);
                    speaker.speakOut("Hello Shruthi! Would you like to Check In?");
                } else {
                    Intent install = new Intent();
                    install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(install);
                }
                speaker.speakOut("Hello Shruthi! Would you like to Check In?");*/
            }
            break;
            case USER_AUTHETICATED:
                if (resultCode == Utility.RESULT_OKAY) {
                    txtSpeechInput.setVisibility(View.VISIBLE);
                    btnSpeak.setVisibility(View.VISIBLE);
                    btnAuthenticate.setVisibility(View.GONE);
                }
        }
    }

    private void checkTTS() {
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnAuthenticate:
                Intent registrationIntent = new Intent(MainActivity.this, RecordingActivity.class);
                registrationIntent.putExtra("CLAIMANT_ID", Utility.mClaimantId);
                registrationIntent.putExtra("PIN_CODE", Utility.mPinNumber);
                registrationIntent.putExtra("REGISTRATION", false);
                startActivityForResult(registrationIntent, USER_AUTHETICATED);
                break;
            case R.id.btnSpeak:
                promptSpeechInput();
                break;
        }
    }
}