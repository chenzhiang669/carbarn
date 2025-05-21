package com.carbarn.inter.controller;

import com.carbarn.inter.pojo.area.AreaPOJO;
import com.carbarn.inter.service.AreaService;
import com.carbarn.inter.service.OcrService;
import com.carbarn.inter.utils.AjaxResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "Area服务")
@RestController
@RequestMapping("/carbarn/area")
public class AreaController {
    @Autowired
    private AreaService areaService;

    @PostMapping("/getProvince")
    public AjaxResult getProvince(){
        return areaService.getProvince();
    }

    @PostMapping("/getCity")
    public AjaxResult getCity(@RequestBody AreaPOJO areaPOJO){
        if(areaPOJO.getCode() == null || "".equals(areaPOJO.getCode())){
            return AjaxResult.error("Missing required parameter: code");
        }

        String code = areaPOJO.getCode();
        return areaService.getCity(code);

    }
}
