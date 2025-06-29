package com.carbarn.inter.service;

import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.carseek.CarsSeekPOJO;
import com.carbarn.inter.pojo.carseek.ListUserCarsSeekSearchDTO;
import com.carbarn.inter.pojo.carseek.SearchCarsSeekDTO;
import com.carbarn.inter.pojo.carseek.UserCarsSeekDTO;
import com.carbarn.inter.pojo.dto.cars.*;
import com.carbarn.inter.utils.AjaxResult;

import java.util.List;
import java.util.Map;

public interface CarsSeekService {
    void insertNewCarSeek(String language, CarsSeekPOJO carsSeekPOJO);

    void updateCarSeekById(String language, CarsSeekPOJO carsSeekPOJO);

    AjaxResult listUserCarSeek(ListUserCarsSeekSearchDTO listUserCarsSeekSearchDTO);

    AjaxResult searchCarSeek(SearchCarsSeekDTO searchCarsSeekDTO);

    AjaxResult deleteCarSeek(CarsSeekPOJO carsSeekPOJO);

    AjaxResult listUserCarSeekExpire(ListUserCarsSeekSearchDTO listUserCarsSeekSearchDTO);

    AjaxResult listUserCarSeekUnExpire(ListUserCarsSeekSearchDTO listUserCarsSeekSearchDTO);

    AjaxResult getCarSeekById(String language, CarsSeekPOJO carsSeekPOJO);
}
