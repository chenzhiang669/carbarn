package com.carbarn.inter.service;


import com.carbarn.inter.pojo.area.AreaPOJO;
import com.carbarn.inter.utils.AjaxResult;

import java.util.List;

public interface AreaService {

    AjaxResult getProvince();

    AjaxResult getCity(String code);

}
