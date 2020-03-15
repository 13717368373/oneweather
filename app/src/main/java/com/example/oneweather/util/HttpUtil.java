package com.example.oneweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//用sendHttpRequest发起一条使用HTTP请求,注册一个回调来处理服务器响应
public class HttpUtil {
   public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
       OkHttpClient client=new OkHttpClient();
       Request request=new Request.Builder().url(address).build();
       client.newCall(request).enqueue(callback);
   }
}
