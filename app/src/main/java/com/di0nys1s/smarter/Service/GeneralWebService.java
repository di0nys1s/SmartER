package com.di0nys1s.smarter.Service;

import android.os.Build;

import com.di0nys1s.smarter.JavaClass.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

public class GeneralWebService {

    public static JSONArray requestRESTServiceArray(String restURL) throws IOException, JSONException {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        JSONArray resultJSONArray = null;
        int statusCode;
        String exceptionJSON = "{%s:%s}";
        String responseFromREST;

        try {
            URL urlToRequest = new URL(restURL);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            statusCode = urlConnection.getResponseCode();
            switch (statusCode) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    resultJSONArray = new JSONArray(String.format(exceptionJSON, URLs.WS_KEY_EXCEPTION, URLs.MSG_401));
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    throw new NullPointerException("");
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    resultJSONArray = new JSONArray(String.format(exceptionJSON, URLs.WS_KEY_EXCEPTION, URLs.MSG_500));
                    break;
                case HttpURLConnection.HTTP_OK:
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    responseFromREST = getResponseText(in);
                    resultJSONArray = new JSONArray(responseFromREST);
                    break;
                default:
                    break;
            }
        } catch (MalformedURLException e) {
            throw e;
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (JSONException e) {
            throw e;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return resultJSONArray;
    }

    public static String postRESTService(String restURL, JSONObject jsonParam) throws IOException {
        String result = "";

        if (jsonParam.length() == 0)
            return URLs.SUCCESS_MSG;
        HttpURLConnection urlConnection=null;

        try {
            URL urlToRequest = new URL(restURL);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            urlConnection.setDoOutput(true);

            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            outputPost.write(jsonParam.toString().getBytes());
            outputPost.flush();
            outputPost.close();

            urlConnection.connect();

            int HttpResult = urlConnection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_NO_CONTENT){
                result = URLs.SUCCESS_MSG;
            }else{
                result = urlConnection.getResponseMessage();
            }
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if(urlConnection!=null)
                urlConnection.disconnect();
            return result;
        }
    }

    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        String str = new Scanner(inStream).useDelimiter("\\A").next();
        return str;
    }
}
