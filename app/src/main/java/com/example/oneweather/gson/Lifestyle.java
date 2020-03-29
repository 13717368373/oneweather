package com.example.oneweather.gson;

import com.google.gson.annotations.SerializedName;

public class Lifestyle {
    //生活指数详细描述
    @SerializedName("txt")
    public String info;
    //生活指数简介
    @SerializedName("brf")
    public String lifeBrief;
}
