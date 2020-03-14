package com.example.oneweather.db;

import org.litepal.crud.DataSupport;

public class City extends DataSupport {
    private int id;         //记录城市的ID
    private String cityName;    //记录城市的名称
    private int cityCode;       //记录城市的代号
    private int provinceId;     //记录所在省的ID

    public void setId(int id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }
}
