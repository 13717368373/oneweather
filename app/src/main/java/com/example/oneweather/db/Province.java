package com.example.oneweather.db;

import org.litepal.crud.DataSupport;

public class Province extends DataSupport {
    private int id;   //记录省的ID
    private String provinceName;    //记录省的名称
    private int provinceCode;       //记录省的代号

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getId() {
        return id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }
}

