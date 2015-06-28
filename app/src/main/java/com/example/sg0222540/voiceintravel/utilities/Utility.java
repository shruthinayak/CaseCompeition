package com.example.sg0222540.voiceintravel.utilities;

/**
 * Created by SG0222540 on 6/22/2015.
 */
public class Utility {

    public static boolean userLogin = true;
    public static String mClaimantId = "fb6d8910-d329-407c-80d0-5f6e2d8a7c73";
    public static String mPinNumber = "8938";
    public static boolean userAuthenticated = false;
    public static int RESULT_OKAY = 45;
    public static String mDialogueId = "106e75d4-9ef7-4b46-b1d6-6b5c743b32f0";
    public static String promptHint = "passphrase";

    public static void performAction(boolean checkIn) {
        makeServerCall(checkIn);
    }

    private static void makeServerCall(boolean checkIn) {
        if (checkIn) {
            //api call for checkin
        } else {
            //api call for checkout
        }
    }


    public static boolean validate(String name, String phoneNo) {
        //Validate from the already exisiting crew members from the db
        return true;
    }
}
