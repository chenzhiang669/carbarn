package com.carbarn.inter.controller;

import com.carbarn.inter.pojo.seafreight.SeaFreightPriceSearchDTO;
import com.carbarn.inter.pojo.seafreight.SeaFreightPortSearchDTO;
import com.carbarn.inter.service.SeaFreightService;
import com.carbarn.inter.utils.AjaxResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "海运查询服务")
@RestController
@RequestMapping("/carbarn/seaFreight")
public class SeaFreightController {
    @Autowired
    private SeaFreightService seaFreightService;

    @PostMapping("/ports")
    public AjaxResult ports(@RequestBody SeaFreightPortSearchDTO seaFreightPortSearchDTO){
        return seaFreightService.getPortInfos(seaFreightPortSearchDTO.getKeywords());
    }


    @PostMapping("/search")
    public AjaxResult search(@RequestBody SeaFreightPriceSearchDTO seaFreightPriceSearchDTO){
        if(seaFreightPriceSearchDTO.getCnt() == 0){
            return AjaxResult.error("Missing required parameter: 'cnt'");
        }

        if(seaFreightPriceSearchDTO.getPortDestination() == null || seaFreightPriceSearchDTO.getPortLoading() == null){
            return AjaxResult.error("Missing required parameter: 'portDestination' or 'portLoading'");
        }

        return seaFreightService.search(seaFreightPriceSearchDTO);
    }
}
