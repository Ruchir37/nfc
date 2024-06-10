package com.sailors.audiorecorder.chatgpt;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Base64Converter {

    public static String convertFileToBase64(String filePath) {
        File file = new File(filePath);
        byte[] fileBytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(fileBytes, Base64.NO_WRAP);
    }
}
