package com.topcoder.hp.idol.ondemand.Helpers;

import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Log;

public class Utilities {

    public static void HandleException(Exception ex) {
        ex.printStackTrace();
    }

    public static void WriteLogcat(String msg) {
        Log.d("APP", msg);
    }


    public static void HandleError(String msg) {
        Log.d("IDOL-ERROR", "@IDOL-ERROR@ " + msg);

    }

    public static String GetStringFromByteArray(byte[] bytes) {
        return new String(bytes);
    }

    private static String GetAndroidId(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public static String GetUniqueTextIndexForDevice(Context context) {
        return Consts.TEXT_INDEX_PREFIX + "Device_" + Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public static String GetUniqueTextIndexDescriptionForDevice(Context context) {
        return Consts.TEXT_INDEX_DESC_PREFIX + " for device: " + Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }
}
