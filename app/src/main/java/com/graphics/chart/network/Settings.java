package com.graphics.chart.network;

import android.Manifest;

public class Settings {

    public static final String URL = "https://demo.bankplus.ru/mobws/json/pointsList";
    public static final String WEB_API_VERSION = "1.1";
    public static final String APP_TAG = "Chart";
    public static final String POST = "POST";
    public static final String base64Format = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
}
