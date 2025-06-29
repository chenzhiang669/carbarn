package com.carbarn.inter.service;


import com.carbarn.inter.pojo.firstpage.FirstPageHotCarsSearchDTO;
import com.carbarn.inter.utils.AjaxResult;

public interface FirstPageService {


    AjaxResult countInfo(long user_id);

    AjaxResult hotCars(FirstPageHotCarsSearchDTO firstPageHotCarsSearchDTO);

    AjaxResult contractDeal(String language);
}
