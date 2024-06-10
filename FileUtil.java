package com.sailors.audiorecorder.chatgpt;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public static void writeStringToFile(Context context, String fileName, String data) {
        FileOutputStream fos = null;
        try {
            // Create or open the file
            File file = new File(context.getFilesDir(), fileName);
            fos = new FileOutputStream(file);
            
            // Write the string data to the file
            fos.write(data.getBytes());
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
