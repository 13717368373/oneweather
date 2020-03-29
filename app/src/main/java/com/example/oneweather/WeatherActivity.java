package com.example.oneweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    private String mWeatherId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherLayout=findViewById(R.id.weather_layout);
        titleCity=findViewById(R.id.title_city);
        titleUpdateTime=findViewById(R.id.title_update_time);
        degreeText=findViewById(R.id.degree_text);
        weatherInfoText=findViewById(R.id.weather_info_text);
        comfText=findViewById(R.id.comf_text);
        sportText=findViewById(R.id.sport_text);
        cwText=findViewById(R.id.cw_text);
        forecastLayout=findViewById(R.id.forecast_layout);


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
    }


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
