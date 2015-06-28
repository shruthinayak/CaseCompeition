package com.example.sg0222540.voiceintravel.utilities;// Copyright (c) 2014, VoiceVault Inc.

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

import android.app.Application;

import com.voicevault.vvlibrary.ViGoLibrary;


public class ViGoSourceApplication extends Application {

    /**
     * ViGo Library Application Parameters
     * <p/>
     * INSERT YOUR VIGO APP CREDENTIALS BELOW...
     */

    // ViGo server web services URL
    // e.g. https://a9i1.voicevault.net/RestApi850/
    private final static String VIGO_SERVER_URL = "https://a9i1.voicevault.net/RestApi850/";

    // ViGo Application ID
    private final static String VIGO_APP_ID = "4755b822-4618-4515-be36-4b8ba7542416";

    // ViGo application credentials
    private final static String VIGO_CREDENTIAL_ID = "DnR5P6mgx6kSrNTcUUgA";
    private final static String VIGO_CREDENTIAL_PWD = "ZezyK4BgXTPKNSf8Jz2gVB89CnERwt";


    @Override
    public void onCreate() {
        super.onCreate();
        ViGoLibrary.getInstance().init(
                VIGO_CREDENTIAL_ID,
                VIGO_CREDENTIAL_PWD,
                VIGO_SERVER_URL,
                VIGO_APP_ID);
    }
}
