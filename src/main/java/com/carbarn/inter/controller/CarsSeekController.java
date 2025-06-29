package com.carbarn.inter.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.carseek.CarsSeekPOJO;
import com.carbarn.inter.pojo.carseek.ListUserCarsSeekSearchDTO;
import com.carbarn.inter.pojo.carseek.SearchCarsSeekDTO;
import com.carbarn.inter.pojo.carseek.UserCarsSeekDTO;
import com.carbarn.inter.pojo.dto.cars.*;
import com.carbarn.inter.pojo.usercar.Constant;
import com.carbarn.inter.service.CarsSeekService;
import com.carbarn.inter.service.CarsService;
import com.carbarn.inter.utils.AjaxResult;
import io.swagger.annotations.Api;
import org.apache.bcel.generic.AALOAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "短信服务")
@RestController
@RequestMapping("/carbarn/carseek")
public class CarsSeekController {

    @Autowired
    private CarsSeekService carsSeekService;

    @PostMapping("/insertNewCarSeek")
    public AjaxResult insertNewCarSeek(@RequestHeader(name = "language", required = true) String language,
                                       @RequestBody CarsSeekPOJO carsSeekPOJO){

        String user_id = (String) StpUtil.getLoginId();
        carsSeekPOJO.setUser_id(Long.valueOf(user_id));
        carsSeekService.insertNewCarSeek(language, carsSeekPOJO);

        return AjaxResult.success("insert new carseek succeful");
    }

    @PostMapping("/updateCarSeekById")
    public AjaxResult updateCarSeekById(@RequestHeader(name = "language", required = true) String language,
                                        @RequestBody CarsSeekPOJO carsSeekPOJO){
        String user_id = (String) StpUtil.getLoginId();
        carsSeekPOJO.setUser_id(Long.valueOf(user_id));

        carsSeekService.updateCarSeekById(language, carsSeekPOJO);
        return AjaxResult.success("update carseek succeful");
    }

    @PostMapping("/getCarSeekById")
    public AjaxResult getCarSeekById(@RequestHeader(name = "language", required = true) String language,
                                        @RequestBody CarsSeekPOJO carsSeekPOJO){
        return carsSeekService.getCarSeekById(language, carsSeekPOJO);
    }

    @PostMapping("/listUserCarSeek")
    public AjaxResult listUserCarSeek(@RequestHeader(name = "language", required = true) String language,
                                      @RequestBody ListUserCarsSeekSearchDTO listUserCarsSeekSearchDTO){
        int pageNo = listUserCarsSeekSearchDTO.getPageNo();
        int pageSize = listUserCarsSeekSearchDTO.getPageSize();
        if (pageNo < 1) {
            return AjaxResult.error("Missing required parameter: pageNo");
        }
        if (pageSize <= 0) {
            return AjaxResult.error("'pageSize' Must meet the conditions  pageSize > 0");
        } else {
            listUserCarsSeekSearchDTO.setPageStart((pageNo - 1) * pageSize);
        }

        String user_id = (String) StpUtil.getLoginId();
        listUserCarsSeekSearchDTO.setUser_id(Long.valueOf(user_id));
        listUserCarsSeekSearchDTO.setLanguage(language);

        return carsSeekService.listUserCarSeek(listUserCarsSeekSearchDTO);
    }

    @PostMapping("/listUserCarSeekExpire")
    public AjaxResult listUserCarSeekExpire(@RequestHeader(name = "language", required = true) String language,
                                      @RequestBody ListUserCarsSeekSearchDTO listUserCarsSeekSearchDTO){
        int pageNo = listUserCarsSeekSearchDTO.getPageNo();
        int pageSize = listUserCarsSeekSearchDTO.getPageSize();
        if (pageNo < 1) {
            return AjaxResult.error("Missing required parameter: pageNo");
        }
        if (pageSize <= 0) {
            return AjaxResult.error("'pageSize' Must meet the conditions  pageSize > 0");
        } else {
            listUserCarsSeekSearchDTO.setPageStart((pageNo - 1) * pageSize);
        }

        String user_id = (String) StpUtil.getLoginId();
        listUserCarsSeekSearchDTO.setUser_id(Long.valueOf(user_id));
        listUserCarsSeekSearchDTO.setLanguage(language);

        return carsSeekService.listUserCarSeekExpire(listUserCarsSeekSearchDTO);
    }

    @PostMapping("/listUserCarSeekUnExpire")
    public AjaxResult listUserCarSeekUnExpire(@RequestHeader(name = "language", required = true) String language,
                                            @RequestBody ListUserCarsSeekSearchDTO listUserCarsSeekSearchDTO){
        int pageNo = listUserCarsSeekSearchDTO.getPageNo();
        int pageSize = listUserCarsSeekSearchDTO.getPageSize();
        if (pageNo < 1) {
            return AjaxResult.error("Missing required parameter: pageNo");
        }
        if (pageSize <= 0) {
            return AjaxResult.error("'pageSize' Must meet the conditions  pageSize > 0");
        } else {
            listUserCarsSeekSearchDTO.setPageStart((pageNo - 1) * pageSize);
        }

        String user_id = (String) StpUtil.getLoginId();
        listUserCarsSeekSearchDTO.setUser_id(Long.valueOf(user_id));
        listUserCarsSeekSearchDTO.setLanguage(language);

        return carsSeekService.listUserCarSeekUnExpire(listUserCarsSeekSearchDTO);
    }

    @PostMapping("/searchCarSeek")
    public AjaxResult searchCarSeek(@RequestHeader(name = "language", required = true) String language,
                                    @RequestBody SearchCarsSeekDTO searchCarsSeekDTO){
        int pageNo = searchCarsSeekDTO.getPageNo();
        int pageSize = searchCarsSeekDTO.getPageSize();
        if (pageNo < 1) {
            return AjaxResult.error("Missing required parameter: pageNo");
        }
        if (pageSize <= 0) {
            return AjaxResult.error("'pageSize' Must meet the conditions  pageSize > 0");
        } else {
            searchCarsSeekDTO.setPageStart((pageNo - 1) * pageSize);
        }


        searchCarsSeekDTO.setLanguage(language);
        return carsSeekService.searchCarSeek(searchCarsSeekDTO);
    }

    @PostMapping("/deleteCarSeek")
    public AjaxResult deleteCarSeek(@RequestHeader(name = "language", required = true) String language,
                                    @RequestBody CarsSeekPOJO carsSeekPOJO){

        return carsSeekService.deleteCarSeek(carsSeekPOJO);
    }
}
