package com.example.oneweather.gson;

import com.google.gson.annotations.SerializedName;

public class Daily_forecast {

    @SerializedName("date")
    public String date;    //日期
    @SerializedName("cond_code_d")
    public String Daycond_code;  //白天天气图标
    @SerializedName("cond_txt_d")
    public String Daycond;   //白天天气状况
    @SerializedName("tmp_max") //一天最高温度
    public String tmp_max;
    @SerializedName("tmp_min")
    public String tmp_min;  //一天最低温度
    @SerializedName("sr")
    public String sunrise;  //日出时间
    @SerializedName("ss")
    public String sunset;   //日落时间
    @SerializedName("pop")
    public String ForePop;  //每天降水概率
}
