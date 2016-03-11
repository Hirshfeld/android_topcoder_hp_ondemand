package com.topcoder.hp.idol.ondemand.Settings;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.Storage.StorageUtils;

import java.lang.reflect.Type;

public class SettingsManager {


    private static SettingsObject _settingsObject = new SettingsObject();

    private static SettingsObject getSettingsObject(Context context) {

        try {
            synchronized (_settingsObject) {
                byte[] settingsObjectByteArr = StorageUtils.GetFile(context, SettingsConsts.SETTINGS_OBJECT_FILE_NAME);
                String settingsObjectString = Utilities.GetStringFromByteArray(settingsObjectByteArr);
                Type settingsObjectType = new TypeToken<SettingsObject>() {
                }.getType();
                _settingsObject = new Gson().fromJson(settingsObjectString, settingsObjectType);
                if (_settingsObject == null) {
                    _settingsObject = new SettingsObject();
                    saveSettingsObject(context);
                }
            }
            return _settingsObject;
        } catch (Exception e) {
            Utilities.HandleException(e);
            return null;
        }
    }

    private static void saveSettingsObject(Context context) {

        try {
            synchronized (_settingsObject) {
                String settingsObjectStr = new Gson().toJson(_settingsObject);
                if (!StorageUtils.SaveFile(context, SettingsConsts.SETTINGS_OBJECT_FILE_NAME, settingsObjectStr.getBytes())) {
                    Utilities.HandleError("saveSettingsObject - Error when saving SettingsObject.");
                }
            }
        } catch (Exception e) {
            Utilities.HandleException(e);
        }
    }

    public static String GetServerAddress(Context context) {
        try {
            synchronized (_settingsObject) {
                _settingsObject = getSettingsObject(context);
                return _settingsObject.getServerAddress();
            }
        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }

    public static void SetServerAddress(Context context, String i_ServerAddress) {
        try {
            synchronized (_settingsObject) {
                _settingsObject = getSettingsObject(context);
                _settingsObject.setServerAddress(i_ServerAddress);
                saveSettingsObject(context);
            }

        } catch (Exception ex) {
            Utilities.HandleException(ex);
        }
    }

    public static String GetAPIKey(Context context) {
        try {
            synchronized (_settingsObject) {
                _settingsObject = getSettingsObject(context);
                return _settingsObject.getApiKey();
            }
        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }

    public static String GetAPIKeyWithoutPrefix(Context context) {
        try {
            synchronized (_settingsObject) {
                _settingsObject = getSettingsObject(context);
                return _settingsObject.getApiKeyWithOutPrefix();
            }
        } catch (Exception ex) {
            Utilities.HandleException(ex);
            return null;
        }
    }

    public static void SetAPIKey(Context context, String i_APIKey) {
        try {
            synchronized (_settingsObject) {
                _settingsObject = getSettingsObject(context);
                _settingsObject.setApiKey(i_APIKey);
                saveSettingsObject(context);
            }

        } catch (Exception ex) {
            Utilities.HandleException(ex);
        }
    }
}
