package com.example.weather.HTTP;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

public class HttpUtils {
static OkHttpClient okHttpClient= new OkHttpClient();
static String url="https://api.jisuapi.com/weather/query?appkey=3efb7f36fa537acd&city=高州";
    public static String get1() throws IOException {
        Request request = new Request.Builder().get().url(url).build();
        String string = okHttpClient.newCall(request).execute().body().string();
        return string;
    }
}
