package com.example.oneweather.db;

import org.litepal.crud.DataSupport;

public class County extends DataSupport {
    private int id;             //县区的ID
    private String countyName;  //县区的名称
    private String weatherId;   //县区对应的天气id
    private int cityId;         //县区所在市的ID

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
