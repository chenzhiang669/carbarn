package com.carbarn.inter.service;


import com.carbarn.inter.pojo.seafreight.SeaFreightPriceSearchDTO;
import com.carbarn.inter.utils.AjaxResult;

public interface SeaFreightService {
    AjaxResult getPortInfos(String keywords);

    AjaxResult search(SeaFreightPriceSearchDTO seaFreightPriceSearchDTO);
}
