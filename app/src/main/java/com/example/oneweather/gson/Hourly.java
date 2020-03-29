package com.example.oneweather.gson;

import com.google.gson.annotations.SerializedName;

public class Hourly {
    @SerializedName("time")
    public String time;     //每小时天气预报时间
    @SerializedName("tmp")
    public String HourTmp;     //每小时温度
    @SerializedName("cond_code")
    public String HourCond_code;    //每小时天气图标代码
    @SerializedName("cond_txt")
    public String HourCond_txt;     //每小时天气描述
    @SerializedName("pop")
    public String HourPop;           //每小时天气降水概率
}
