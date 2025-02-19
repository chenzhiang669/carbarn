package com.carbarn.inter.service;

import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.dto.cars.CarsOfUsersDTO;
import com.carbarn.inter.pojo.dto.cars.FirstPageCarsDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import com.carbarn.inter.pojo.language.LanguageDTO;
import com.carbarn.inter.utils.AjaxResult;

import java.util.List;
import java.util.Map;

public interface LanguageService {
    List<LanguageDTO> getLanguage();
}
