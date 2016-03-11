package com.topcoder.hp.idol.ondemand.Helpers;

import android.app.ProgressDialog;
import android.content.Context;

public class CommonProgressDialog {

    private static ProgressDialog _prgDialog = null;
    private static boolean _isShowing = false;
    private static int _numOfRequests = 0;

    public static void ChangeText(Context context, String title) {
        try {
            if (_isShowing) {
                Utilities.WriteLogcat("CommonProgressDialog ChangeText, changing text.");
                _prgDialog.setTitle(title);
            }
        } catch (Exception ex) {
            Utilities.HandleException(ex);
        }
    }

    public static void ShowProgressDialog(Context context, String title) {
        try {
            if (!_isShowing) {

                Utilities.WriteLogcat("CommonProgressDialog ShowProgressDialog, showing dialog");

                //if (Utilities.CheckIfContextRunning(context)) {
                _numOfRequests = 1;
                _prgDialog = new ProgressDialog(context);
                _prgDialog.setTitle(title);
                _prgDialog.setMessage("Please wait.");
                _prgDialog.setCancelable(false);
                _prgDialog.setIndeterminate(true);

                _prgDialog.show();

                _isShowing = true;
                //} else {

                //Utilities.WriteDebugLogMessage("CommonProgressDialog ShowProgressDialog, context is NOT valid.");

                //}
            } else {

                Utilities.WriteLogcat("CommonProgressDialog ShowProgressDialog, got another reqquest to show");

                _numOfRequests++;
            }
        } catch (Exception ex) {
            Utilities.HandleException(ex);
        }


    }

    public static void Destroy() {
        try {
            if (_isShowing) {
                if (_numOfRequests == 1) {

                    Utilities.WriteLogcat("CommonProgressDialog ShowProgressDialog, destroying dialog!");

                    _numOfRequests--;
                    _isShowing = false;
                    if (_prgDialog != null) {
                        _prgDialog.dismiss();
                        _prgDialog = null;
                    }
                } else {

                    Utilities.WriteLogcat("CommonProgressDialog ShowProgressDialog, decreasing requests for dialog");


                    _numOfRequests--;
                }

            }

        } catch (Exception ex) {

        }
    }

}