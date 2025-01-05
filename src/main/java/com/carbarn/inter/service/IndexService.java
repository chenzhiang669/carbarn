package com.carbarn.inter.service;

import com.carbarn.inter.pojo.dto.cars.index.HotBrandDTO;
import com.carbarn.inter.pojo.dto.cars.index.HotIndexDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IndexService {
    List<HashMap<String, Object>> getIndex(String language);

    List<Object> getBrand(String language);


    Map<String, Object>  getSeries(String language, int brand_id);


    List<Object> getType(String language, int brand_id, int series_id);


    List<HotIndexDTO> getHotWord(String language);


    List<HotBrandDTO>  getHotBrand(String language);
}
