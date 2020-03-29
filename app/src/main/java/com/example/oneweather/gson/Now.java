package com.example.oneweather.gson;

import com.google.gson.annotations.SerializedName;

public class Now {
    //实况天气图标
    @SerializedName("cond_code")
    public String cond_code;
    //实况天气状况描述
    @SerializedName("cond_txt")
    public String cond_txt;

    //风向
    @SerializedName("wind_dir")
    public String wind_dir;

    //风速
    @SerializedName("wind_spd")
    public String winSpeed;

    //温度，不是体感温度
    @SerializedName("tmp")
    public String temperature;

    //能见度
    @SerializedName("vis")
    public String visibility;
}
