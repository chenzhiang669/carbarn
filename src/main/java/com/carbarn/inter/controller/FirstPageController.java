package com.carbarn.inter.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.carbarn.inter.pojo.area.AreaPOJO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import com.carbarn.inter.pojo.firstpage.FirstPageHotCarsSearchDTO;
import com.carbarn.inter.service.AreaService;
import com.carbarn.inter.service.FirstPageService;
import com.carbarn.inter.utils.AjaxResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "国内首页服务")
@RestController
@RequestMapping("/carbarn/firstPage")
public class
FirstPageController {
    @Autowired
    private FirstPageService firstPageService;

    @PostMapping("/countInfo")
    public AjaxResult countInfo(){
        String user_id = (String) StpUtil.getLoginId();
        return firstPageService.countInfo(Long.valueOf(user_id));
    }


    @PostMapping("/hotCars")
    public AjaxResult hotCars(@RequestHeader(name = "language", required = true) String language,
                              @RequestBody FirstPageHotCarsSearchDTO firstPageHotCarsSearchDTO){
        firstPageHotCarsSearchDTO.setLanguage(language);
        return firstPageService.hotCars(firstPageHotCarsSearchDTO);
    }

    @PostMapping("/contractDeal")
    public AjaxResult contractDeal(@RequestHeader(name = "language", required = true) String language){
        return firstPageService.contractDeal(language);
    }

}
