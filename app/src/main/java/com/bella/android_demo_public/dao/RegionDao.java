package com.bella.android_demo_public.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.bella.android_demo_public.bean.RegionInfo;

import java.util.List;

@Dao
public interface RegionDao {
    @Query("select * from REGION_INFO")
    List<RegionInfo> getAllAddress();

    @Query("SELECT DISTINCT  COUNTRY_NAME  FROM REGION_INFO")
    List<String> getAllZhCoutry();

    @Query("SELECT DISTINCT  COUNTRY_NAME_EN  FROM REGION_INFO")
    List<String> getAllEnCoutry();

    @Query("SELECT DISTINCT  PROVINCE_NAME  FROM REGION_INFO WHERE COUNTRY_NAME = :courtyName")
    List<String> getAllZhProvincesByCoutryId(String courtyName);

    @Query("SELECT DISTINCT  PROVINCE_NAME_EN  FROM REGION_INFO WHERE COUNTRY_NAME = :courtyName")
    List<String> getAllEnProvincesByCoutryId(String courtyName);

    @Query("select * from REGION_INFO WHERE PROVINCE_NAME = :provinceName")
    List<RegionInfo> getAllCitysByProvinceId(String provinceName);


    @Query("DELETE FROM REGION_INFO")
    void deleteAll();

    @Insert
    void insert(RegionInfo regionInfo);
}
