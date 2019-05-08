package com.haiyu.manager.utils;

import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public static Response post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static boolean testHtml(String testhtml) {
        URL url = null;
        try {
//            System.out.println("测试地址：" + testhtml);
            url = new URL(testhtml);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            URLConnection urlConn = url.openConnection();
            urlConn.setReadTimeout(2000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            return reader.readLine() != null;
        } catch (Exception var6) {
            return false;
        }
    }

    public static boolean testsocket(String url, String port){
        Socket socket = null;
        boolean tong = false;
        try {
            socket = new Socket(url,Integer.parseInt(port));
            tong = true;
        }
        catch (Exception e){
            tong = false;
        }
        finally {
            if(socket!=null){
                try {
                    socket.close();
                }
                catch (Exception e){
                }
            }
        }
        return tong;
    }

}