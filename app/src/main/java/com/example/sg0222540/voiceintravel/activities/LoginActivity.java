package com.example.sg0222540.voiceintravel.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sg0222540.voiceintravel.R;
import com.example.sg0222540.voiceintravel.utilities.Utility;
import com.voicevault.vvlibrary.ViGoLibrary;
import com.voicevault.vvlibrary.VoiceVaultAPIUserCallback;

/**
 * Created by SG0222540 on 6/22/2015.
 */
public class LoginActivity extends Activity implements View.OnClickListener, VoiceVaultAPIUserCallback {
    private ImageButton btnSpeak;
    private static String mPinNumber = null;

    public static final int VIGO_REQUEST_CODE = 999;
    public static boolean enableAudioRecordVoiceRecognitionSetting = false;
    private static String mClaimantId = null;
    private static boolean mIsClaimantRegistered = false;
    private EditText inputName;
    private EditText inputPhNo;
    private LinearLayout lytOuter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getActionBar().hide();
        if (!Utility.userLogin) {
            lytOuter = (LinearLayout) findViewById(R.id.lytOuter);
            inputName = (EditText) findViewById(R.id.inputName);
            inputPhNo = (EditText) findViewById(R.id.inputPhNo);
            btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
            btnSpeak.setOnClickListener(this);
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void startRegistrationClick(View button) {
        lytOuter.setVisibility(View.GONE);
        if (mClaimantId == null) {
            ViGoLibrary.getInstance().registerClaimant(this);
        } else if (!mIsClaimantRegistered) {
            registerClaimantCallback(mClaimantId);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSpeak:
                if (inputName.getText().toString().isEmpty() && inputPhNo.getText().toString().isEmpty())
                    Toast.makeText(LoginActivity.this, "Please enter your name and phone number", Toast.LENGTH_LONG).show();
                else {
                    if (Utility.validate(inputName.getText().toString(), inputPhNo.getText().toString()))
                        startRegistrationClick(v);
                }
                break;
        }
    }

    @Override
    public void registerClaimantCallback(String claimantId) {
        mClaimantId = claimantId;
        Utility.mClaimantId = claimantId;
        // Open the Recording Activity indicating it is for Registration
        // Return result indicates if it was successful or not
        if (mClaimantId != null) {
            Intent registrationIntent = new Intent(LoginActivity.this, RecordingActivity.class);
            registrationIntent.putExtra("CLAIMANT_ID", mClaimantId);
            registrationIntent.putExtra("REGISTRATION", true);
            registrationIntent.putExtra("AUDIO_FLAG", enableAudioRecordVoiceRecognitionSetting);
            startActivityForResult(registrationIntent, VIGO_REQUEST_CODE);
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.toast_register_failed), Toast.LENGTH_LONG)
                    .show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // confirm correct activity request code
        if (requestCode == VIGO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // registration completed successfully so activate login
                mIsClaimantRegistered = true;
                //    findViewById(R.id.buttonLogin).setEnabled(true);
                //  findViewById(R.id.buttonRegistration).setEnabled(false);

                // request for second factor

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_adaptation_title))
                        .setMessage(getString(R.string.dialog_adaptation_text))
                        .setView(input)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get the PIN number
                                Editable value = input.getText();
                                mPinNumber = value.toString();
                                Utility.mPinNumber = mPinNumber;
                                Toast.makeText(LoginActivity.this, getString(R.string.toast_adaptation_set), Toast.LENGTH_LONG)
                                        .show();
                            }
                        })
                        .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // ADAPTATION DISABLED IF CANCELLED
                                Toast.makeText(LoginActivity.this, getString(R.string.toast_adaptation_not_set), Toast.LENGTH_LONG)
                                        .show();
                            }
                        }).show();
            }
            if (resultCode == RESULT_CANCELED) {
                // registration failed so allow repeat registration
                Toast.makeText(LoginActivity.this, getString(R.string.toast_register_cancelled), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
