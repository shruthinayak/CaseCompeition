package com.example.sg0222540.voiceintravel.activities;// Copyright (c) 2014, VoiceVault Inc.

// All rights reserved.

// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:

//  * Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.

//  * Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.

//  * Neither the name of VoiceVault nor the names of its contributors
//    may be used to endorse or promote products derived from this
//    software without specific prior written permission.

//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL VOICEVAULT BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
// OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
// LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sg0222540.voiceintravel.R;
import com.example.sg0222540.voiceintravel.utilities.Utility;
import com.voicevault.vvlibrary.RestResponseObject;
import com.voicevault.vvlibrary.ViGoLibrary;
import com.voicevault.vvlibrary.VoiceVaultAPIVoiceCallback;


public class RecordingActivity extends Activity implements VoiceVaultAPIVoiceCallback {

    /**
     * VIGO CONFIGURATION PARAMETERS
     * <p/>
     * INSERT YOUR VIGO CONFIGURATION PARAMETERS FOR DIGITS OR PASSPHRASE BELOW...
     */

    // Certified ViGo passphrase to prompt user for, or empty string for DIGITS
    //  e.g. "VoiceVault knows me by the sound of my voice"
    private final static String VIGO_PHRASE_TEXT = "People carry umbrellas when it is raining";

    // ViGo configuration identifier, MUST match with the VIGO_PHRASE_TEXT above
    //  e.g. "01234567-89ab-cdef-0123-456789abcdef"
    private final static String VIGO_CONFIGURATION_ID = "90d3bb6b-cfc2-4ba4-824e-f57cbfe0b255";

    // Language for passphrase, do NOT alter unless you have a supported CONFIGURATION_ID from VoiceVault
    //  e.g. "EnglishUnitedStates"
    private static final String VIGO_PHRASE_LANGUAGE = "EnglishUnitedStates";

    // Recording time in milliseconds, set long enough for the phrase used
    private static final int VIGO_RECORD_TIME_MILLISECS = 4000;


    /**
     * Reference to our dialogue id
     */
    private String mDialogueId;

    /**
     * Reference to our claimant id
     */
    private String mClaimantId;

    /**
     * Prompt used for DIGITS, returned from ViGo back-end
     */
    private String mPromptHint;

    /**
     * Flag to indicate if we are registering or logging in
     */
    private boolean isRegistration;

    /**
     * UI TextView objects for prompt and status updates
     */
    private TextView mTextViewPhrase;
    private TextView mTextViewStatus;

    /**
     * Flag to check if dialogue is still in progress
     */
    private boolean isDialogueInProgress = false;

    /**
     * Flag for ViGo AudioSource Voice Verification capture mode
     */
    private boolean isAudioRecordVoiceRecognitionOptionEnabled;

    /**
     * ViGo Adaptation PIN code
     */
    private String mPinCode;


    /**
     * onCreate handler
     * Update UI and prepare for registration or login process
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        getActionBar().hide();
        // find out if this is registration or login process
        isRegistration = getIntent().getBooleanExtra("REGISTRATION", false);

        // find required UI objects
        mTextViewPhrase = (TextView) findViewById(R.id.textViewPhrase);
        mTextViewStatus = (TextView) findViewById(R.id.textViewStatus);

        // start a ViGo dialogue (calls onDialogueStarted on completion)
        // mClaimantId = getIntent().getExtras().getString("CLAIMANT_ID");
        //  mPinCode = getIntent().getExtras().getString("PIN_CODE");
        mClaimantId = Utility.mClaimantId;
        mPinCode = Utility.mPinNumber;
        isAudioRecordVoiceRecognitionOptionEnabled = getIntent().getExtras().getBoolean("AUDIO_FLAG", false);
        ViGoLibrary.getInstance().startDialogue(mClaimantId, VIGO_CONFIGURATION_ID, VIGO_PHRASE_LANGUAGE, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback for ViGo dialogue started
     * Called after a dialogue established with the ViGo back-end server
     *
     * @param dialogueId The ViGo dialogue identifier
     * @param promptHint Actual prompt for user (N.B. DIGIT MODE ONLY)
     */
    @Override
    public void onDialogueStarted(String dialogueId, String promptHint) {
        // persist the current dialogue identifier
        //mDialogueId = dialogueId;
        mDialogueId = Utility.mDialogueId;
        mPromptHint = Utility.promptHint;
        isDialogueInProgress = true;

        // update the UI ready to speak
        mTextViewStatus.setText(getString(R.string.status_say));
        findViewById(R.id.buttonRecord).setEnabled(true);
        findViewById(R.id.buttonRecord).setSoundEffectsEnabled(false);

        // check if DIGITS or PASSPHRASE mode
        if (VIGO_PHRASE_TEXT.isEmpty()) {
            // for DIGITS the prompt is passed from back-end as promptHint
            mPromptHint = promptHint;
            mTextViewPhrase.setText(mPromptHint);
        } else {
            // for passphrase mode use the predefined phrase
            mTextViewPhrase.setText(VIGO_PHRASE_TEXT);
        }
    }

    /**
     * Start recording audio
     * Called after the user clicks on record button
     *
     * @param recordButton The record button object
     */
    public void recordClick(View recordButton) {
        // update UI
        findViewById(R.id.buttonRecord).setEnabled(false);
        findViewById(R.id.buttonRecord).setSoundEffectsEnabled(false);
        mTextViewStatus.setText(getString(R.string.status_recording));

        // start recording for preset time
        ViGoLibrary.getInstance().startRecording(
                VIGO_RECORD_TIME_MILLISECS, isAudioRecordVoiceRecognitionOptionEnabled, this);
    }

    /**
     * Callback for recording completed
     * Called after the audio recording has finished
     */
    @Override
    public void onRecordComplete(boolean b) {
        // update the status from the UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextViewStatus.setText(getString(R.string.status_uploading));
            }
        });

        // check if DIGITS or PASSPHRASE mode
        if (VIGO_PHRASE_TEXT.isEmpty()) {
            // for DIGITS send promptHint returned from StartDialogue or onPhraseSubmitted
            ViGoLibrary.getInstance().submitPhrase(mDialogueId, mPromptHint, this);
        } else {
            // for passphrase mode use the predefined phrase
            ViGoLibrary.getInstance().submitPhrase(mDialogueId, VIGO_PHRASE_TEXT, this);
        }
    }

    /**
     * Callback for phrase submitted
     * Called after the phrase audio has been submitted to the
     * ViGo back-end and return value received
     *
     * @param vigoResponse The response from ViGo web service
     */
    @Override
    public void onPhraseSubmitted(RestResponseObject vigoResponse) {
        // only test if valid response
        if (vigoResponse != null) {

            // submitted phrase was accepted but more work to do...
            if (vigoResponse.isAccepted()) {
                // check if DIGITS or PASSPHRASE mode
                if (VIGO_PHRASE_TEXT.isEmpty()) {
                    // for DIGITS mode the ViGo back-end returns the next prompt
                    mPromptHint = vigoResponse.getResult(RestResponseObject.PROMPT_HINT);
                    mTextViewPhrase.setText(mPromptHint);
                } else {
                    // for PASSPHRASE mode we need to supply the correct phrase
                    mTextViewPhrase.setText(VIGO_PHRASE_TEXT);
                }

                // prompt for next phrase from user
                mTextViewStatus.setText(getString(R.string.status_say));
                findViewById(R.id.buttonRecord).setEnabled(true);
                findViewById(R.id.buttonRecord).setSoundEffectsEnabled(false);
            }
            // CLAIMANT VERIFIED SUCCESSFULLY!
            else if (vigoResponse.isSucceeded()) {
                isDialogueInProgress = false;

                if (isRegistration) {
                    // SUCCESSFUL REGISTRATION
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_enroll_success), Toast.LENGTH_LONG)
                            .show();
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    // SUCCESSFUL VERIFICATION
                    Toast.makeText(RecordingActivity.this, "Authentication Successful", Toast.LENGTH_LONG).show();
                    Utility.userAuthenticated = true;
                    Intent returnIntent = new Intent();
                    setResult(Utility.RESULT_OKAY, returnIntent);
                    //startActivity(new Intent(RecordingActivity.this, MainActivity.class));
                    finish();
                }
            }
            // CLAIMANT NOT VERIFIED BUT SUITABLE FOR MANUAL ADAPTATION WITH ANOTHER FACTOR
            else if (vigoResponse.isRejected() && mPinCode != null) {
                isDialogueInProgress = false;

                // ALERT DIALOG TO ASK FOR PIN CODE TO VALIDATE
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_identity_unproven))
                        .setMessage(getString(R.string.dialog_identity_request))
                        .setView(input)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get the PIN number
                                Editable value = input.getText();
                                if (value.toString().equals(mPinCode)) {
                                    ViGoLibrary.getInstance().adaptClaimant(mDialogueId, mClaimantId);
                                    startActivity(new Intent(RecordingActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    // Claimant identity NOT confirmed with voice or PIN!
                                    // just returns in sample app but should handle appropriately...
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing...
                                Toast.makeText(getApplicationContext(), getString(R.string.toast_verify_cancel), Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }).show();
            } else
            // CLAIMANT REJECTED OR UNHANDLED ERROR
            {
                if (!isRegistration)
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_verify_failed), Toast.LENGTH_LONG)
                            .show();
                finish();
            }
        }
    }

    /**
     * Volume update callback
     * Use to catch any volume changes
     *
     * @param level The volume level, from 0 to 100
     */
    @Override
    public void onVolumeUpdate(final int level) {
    /*
        // start monitoring on the UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("Volume level", "" + level);
            }
        });
    */
    }

    /**
     * onPause handler
     * Before leaving the activity screen we need to stop recording
     * and abort any open dialogues with the ViGo back-end
     */
    @Override
    protected void onPause() {
        super.onPause();
        ViGoLibrary.getInstance().abortRecord();
        if (isDialogueInProgress)
            ViGoLibrary.getInstance().abortDialogue(mDialogueId);
    }
}
