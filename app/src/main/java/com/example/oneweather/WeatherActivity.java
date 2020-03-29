package com.example.oneweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.oneweather.gson.Daily_forecast;
import com.example.oneweather.gson.Weather;
import com.example.oneweather.util.HttpUtil;
import com.example.oneweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView comfText;
    private TextView sportText;
    private TextView cwText;
    private ImageView bingPicImg;

    private String mWeatherId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);           //布局显示在状态栏上面
            getWindow().setStatusBarColor(Color.TRANSPARENT);       //状态栏设置为透明
        }
        setContentView(R.layout.activity_weather);
        bingPicImg=findViewById(R.id.bing_pic_img);
        weatherLayout=findViewById(R.id.weather_layout);
        titleCity=findViewById(R.id.title_city);
        titleUpdateTime=findViewById(R.id.title_update_time);
        degreeText=findViewById(R.id.degree_text);
        weatherInfoText=findViewById(R.id.weather_info_text);
        comfText=findViewById(R.id.comf_text);
        sportText=findViewById(R.id.sport_text);
        cwText=findViewById(R.id.cw_text);
        forecastLayout=findViewById(R.id.forecast_layout);

        //从本地获取数据信息
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString =preferences.getString("weather",null);
        if(weatherString!=null){
            /*Weather weather= Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);*/

            Weather weather= Utility.handleWeatherResponse(weatherString);
            //mWeatherId=weather.basic.cityId;

            showWeatherInfo(weather);

        }else{
          /*  String weatherId=getIntent().getStringExtra("weather_id");
            requestWeather(weatherId);*/

            mWeatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);

        }
        //获取本地必应图片
        String bingPic=preferences.getString("bing_pic",null);
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }
    }



//通过请求获取天气信息
    public void requestWeather(final String weatherId){
        final String weatherUrl="https://free-api.heweather.com/s6/weather?location="+weatherId.toString()+
                "&key=26c2a976920d45dab0804d87e52f89c8";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather= Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        loadBingPic();
    }

    private void loadBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor= PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather){

        //一天天气基本信息
        String cityName=weather.basic.cityName;
        //Log.i("城市名称：",cityName);
        String updateTime=weather.update.updateTime.split(" ")[1];
        //Log.i("updateTime：",updateTime);
        String degree=weather.now.temperature+"℃";
        //Log.i("degree：",degree);
        String weatherInfo=weather.now.cond_txt;
        //Log.i("weatherInfo：",weatherInfo);
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        //一周天气预报
        forecastLayout.removeAllViews();
        for(Daily_forecast forecast:weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dataText=view.findViewById(R.id.data_text);
            TextView infoText=view.findViewById(R.id.info_text);
            TextView maxText=view.findViewById(R.id.max_text);
            TextView minText=view.findViewById(R.id.min_text);

            dataText.setText(forecast.date);
            infoText.setText(forecast.Daycond);
            maxText.setText(forecast.tmp_max);
            minText.setText(forecast.tmp_min);
            forecastLayout.addView(view);
           /* Log.i("日期",forecast.date);
            Log.i("天气状况",forecast.Daycond);
            Log.i("最高温度",forecast.tmp_max);
            Log.i("最低温度",forecast.tmp_min);*/
        }

        //生活指数
        String comfort="舒适度："+weather.lifestyle.get(0).lifeBrief+"，"+weather.lifestyle.get(0).info;
        String carWash="洗车指数："+weather.lifestyle.get(6).lifeBrief+"，"+weather.lifestyle.get(6).info;
        String sport="运动建议："+weather.lifestyle.get(3).lifeBrief+"，"+weather.lifestyle.get(3).info;
        comfText.setText(comfort);
        cwText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }

}
