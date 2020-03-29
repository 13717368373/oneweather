package com.example.oneweather.gson;

import com.google.gson.annotations.SerializedName;


public  class Basic{
    //城市名称
    @SerializedName("location")
    public String cityName;
    //城市国际id
    @SerializedName("cid")
    public String cityId;

}


