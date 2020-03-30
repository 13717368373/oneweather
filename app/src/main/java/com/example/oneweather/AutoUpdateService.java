package com.example.oneweather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.oneweather.gson.Weather;
import com.example.oneweather.util.HttpUtil;
import com.example.oneweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        //设置每小时刷新时间
        int RefreHour=4*60*60*1000;
        long Time= SystemClock.elapsedRealtime()+RefreHour;
        Intent intent1=new Intent(this, AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,intent1,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,Time,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather(){
        final SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherstring=preferences.getString("weather",null);
        if(weatherstring!=null){
            //有缓存时直接解析数据
            Weather weather= Utility.handleWeatherResponse(weatherstring);
            String weatherId=weather.basic.cityId;
            String weatherUrl="https://free-api.heweather.com/s6/weather?location="+weatherId.toString()+
                    "&key=26c2a976920d45dab0804d87e52f89c8";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText=response.body().string();
                    Weather weather= Utility.handleWeatherResponse(responseText);
                    if(weather!=null && "ok".equals(weather.status)){
                        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(
                                AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    private void updateBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.
                        getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });
    }
}
