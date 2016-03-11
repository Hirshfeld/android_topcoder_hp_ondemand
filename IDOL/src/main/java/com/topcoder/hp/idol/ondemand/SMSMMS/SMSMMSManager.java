package com.topcoder.hp.idol.ondemand.SMSMMS;


import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.topcoder.hp.idol.ondemand.Helpers.Utilities;

import java.util.ArrayList;
import java.util.List;

public class SMSMMSManager {

    public static List<SMS> GetAllSMS(Activity activity) {
        List<SMS> lstSms = new ArrayList<SMS>();
        SMS objSms = new SMS();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = activity.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        activity.startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new SMS();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
    }

    public static List<MMS> GetAllMMS(final Activity activity) {

        List<MMS> lstMMS = new ArrayList<MMS>();
        String[] columns = null;
        String[] values = null;

        try {
            Cursor curPdu = activity.getContentResolver().query(Uri.parse("content://mms"), null, null, null, null);
            curPdu.moveToFirst();
            do {
                String id = curPdu.getString(curPdu.getColumnIndex("_id"));
                Cursor curPart = activity.getContentResolver().query(Uri.parse("content://mms/" + id + "/part"), null, null, null, null);

                while (curPart.moveToNext()) {
                    columns = curPart.getColumnNames();
                    if (values == null)
                        values = new String[columns.length];

                    for (int i = 0; i < curPart.getColumnCount(); i++) {
                        values[i] = curPart.getString(i);
                    }
                    MMS newMMS = new MMS(values[0], values[12], values[3]);
                    lstMMS.add(newMMS);
                }
            } while (curPdu.moveToNext());
        } catch (Exception e) {
            Utilities.HandleException(e);
        }

        return lstMMS;
    }
}
