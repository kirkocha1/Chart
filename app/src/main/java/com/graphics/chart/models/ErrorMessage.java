package com.graphics.chart.models;

import android.util.Base64;
import android.util.Log;

import com.graphics.chart.network.Settings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorMessage {
    private String message;
    private byte[] byteArray;

    public ErrorMessage(String message) {
        this.message = message;
    }

    private void validateMessage() {
        if (checkForBase64()) {
            try {
                byteArray = message.getBytes("UTF-8");
                byte[] decodedMessage = Base64.decode(byteArray, Base64.DEFAULT);
                message = new String(decodedMessage, "UTF-8");
            } catch (Exception ex) {
                Log.d(Settings.APP_TAG, "Not base64 code");
            }
        }
    }

    private boolean checkForBase64() {
        boolean isBase64 = false;
        if (message != null) {
            Pattern pattern = Pattern.compile(Settings.base64Format);
            Matcher matcher = pattern.matcher(message);
            isBase64 = matcher.matches();
        }
        return isBase64;
    }

    public String getMessage() {

        if (byteArray == null) {
            validateMessage();
        }
        return message;
    }
}
