package com.carbarn.inter.controller;

import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.pojo.dto.cars.index.HotBrandDTO;
import com.carbarn.inter.pojo.dto.cars.index.HotIndexDTO;
import com.carbarn.inter.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carbarn/index")
public class IndexController {

    @Autowired
    private IndexService indexService;


    @PostMapping("")
    public List<HashMap<String, Object>>  getIndex(@RequestHeader(name = "language", required = true) String language){
        return indexService.getIndex(language);
    }


    @PostMapping("/brand")
    public List<Object> getBrand(@RequestHeader(name = "language", required = true) String language) {

        return indexService.getBrand(language);
    }


    @PostMapping("/brand/series")
    public List<Object> getSeries(@RequestHeader(name = "language", required = true) String language,
                                         @RequestBody String body) {
        JSONObject jsonObject = (JSONObject) JSONObject.parse(body);
        int brand_id = jsonObject.getInteger("brand_id");
        return indexService.getSeries(language,brand_id);
    }



    @PostMapping("/brand/series/type")
    public List<Object> getType(@RequestHeader(name = "language", required = true) String language,
                                @RequestBody String body) {
        JSONObject jsonObject = (JSONObject) JSONObject.parse(body);
        int brand_id = jsonObject.getInteger("brand_id");
        int series_id = jsonObject.getInteger("series_id");

        return indexService.getType(language,brand_id,series_id);
    }

    @PostMapping("/hot_words")
    public List<HotIndexDTO> getHotWord(@RequestHeader(name = "language", required = true) String language) {

        return indexService.getHotWord(language);
    }


    @PostMapping("/hot_brands")
    public  List<HotBrandDTO>  getHotBrands(@RequestHeader(name = "language", required = true) String language) {

        return indexService.getHotBrand(language);
    }

}
