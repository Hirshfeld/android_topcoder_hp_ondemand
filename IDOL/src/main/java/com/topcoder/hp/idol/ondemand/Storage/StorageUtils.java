package com.topcoder.hp.idol.ondemand.Storage;

import android.content.Context;

import com.topcoder.hp.idol.ondemand.Helpers.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;


public class StorageUtils {

    public static boolean SaveFile(Context context, String filePath, byte[] data) {
        try {
            FileOutputStream outputStream;
            outputStream = context.openFileOutput(filePath, Context.MODE_PRIVATE);
            outputStream.write(data);
            outputStream.close();

            return true;
        } catch (Exception e) {
            Utilities.HandleException(e);
            return false;
        }
    }

    private static boolean saveFile(Context context, String filePath, InputStream inputStream)

    {
        try {
            FileOutputStream outputStream;
            outputStream = context.openFileOutput(filePath, Context.MODE_PRIVATE);
            byte[] buffer = new byte[StorageConsts.BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();

            return true;

        } catch (Exception e) {
            Utilities.HandleException(e);
            return false;
        }
    }

    public static boolean DeleteFile(Context context, String fileName) {
        boolean deleted = false;
        try {
            File dir = context.getFilesDir();
            File file = new File(dir, fileName);
            deleted = file.delete();
        } catch (Exception e) {
            Utilities.HandleException(e);

        }
        return deleted;
    }

    public static byte[] GetFile(Context context, String fileName) {
        try {

            FileInputStream inputStream; //= new FileInputStream(filePath);;

            try {
                inputStream = context.openFileInput(fileName);
            } catch (FileNotFoundException e) {
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write(new byte[0]);
                fos.close();
                inputStream = context.openFileInput(fileName);
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[StorageConsts.BUFFER_SIZE];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            inputStream.close();

            return buffer.toByteArray();

        } catch (Exception e) {
            Utilities.HandleException(e);
            return null;
        }
    }

    private static String removeSeparators(String path) {

        return path.replaceAll("/", "-");
    }

}
