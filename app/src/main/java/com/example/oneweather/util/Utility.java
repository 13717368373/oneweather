package com.example.oneweather.util;


import android.text.TextUtils;

import com.example.oneweather.db.City;
import com.example.oneweather.db.County;
import com.example.oneweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//用于解析服务器返回的JSON数据
public class Utility {

    //01解析返回的省级数据
    public static boolean handleProvinceResponse(String response) throws JSONException {
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces=new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    //获取JSON对象
                    JSONObject provinceObject=allProvinces.getJSONObject(i);
                    Province province=new Province();

                    //获取省级名称、代号信息
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        return false;
    }

    //02解析返回的市级数据
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allcities=new JSONArray(response);
                for(int i=0;i<allcities.length();i++){
                    JSONObject cityObject=allcities.getJSONObject(i);
                    City city =new City();
                    //获取城市名称、代号和所属省份
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    //03解析返回的县区级数据
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allcounties=new JSONArray(response);
                for(int i=0;i<allcounties.length();i++){
                    JSONObject countyObject=allcounties.getJSONObject(i);
                    County county=new County();

                    //获取县区名称、所属市ID、天气ID
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
