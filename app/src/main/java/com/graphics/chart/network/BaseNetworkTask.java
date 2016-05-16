package com.graphics.chart.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public abstract class BaseNetworkTask extends AsyncTask<String, Void, String> {

    private final String TAG = Settings.APP_TAG;

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.d("Chart", "Start request");
            final String url = params[0];
            final String method = (params.length > 1 && params[1] != null) ? params[1] : "GET";
            final String body = (params.length > 2 && params[2] != null) ? params[2] : null;
            String content = loadFromNetwork(url, method, body);
            return content;
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            return null;
        }
    }

    public void post(String url, String method, String body) {

        execute(url, method, body);
    }

    @Override
    protected void onPostExecute(String content) {
        super.onPostExecute(content);
        onDataRecieved(content);
    }

    public abstract void onDataRecieved(String content);

    private String loadFromNetwork(String url, String method, String body) throws IOException {
        HttpsURLConnection connection = null;
        InputStream stream = null;
        String response = "";
        try {
            connection = openConnection(url, method, body);
            if (connection != null) {
                stream = connection.getInputStream();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }

    private HttpsURLConnection openConnection(String urlString, String method, String body) throws IOException {

        HttpsURLConnection conn = null;
        int responseCode;
        try {

            ignoreCertificateCheck();

            URL url = new URL(urlString);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            byte[] outputInBytes = body.getBytes("UTF-8");
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(outputInBytes);
            outputStream.close();
            responseCode = conn.getResponseCode();

            if (responseCode == 404) {
                errorHandling(responseCode);
            }

            conn.connect();
        } catch (IOException e) {
            ConnectException connectException = new ConnectException(e.getMessage());
            connectException.initCause(e);
            throw connectException;
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }

        return conn;
    }

    public abstract void errorHandling(int responseCode);

    private void ignoreCertificateCheck() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
