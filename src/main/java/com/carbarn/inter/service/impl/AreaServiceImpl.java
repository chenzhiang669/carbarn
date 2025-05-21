package com.carbarn.inter.service.impl;

import com.carbarn.inter.mapper.AreaMapper;
import com.carbarn.inter.pojo.area.AreaPOJO;
import com.carbarn.inter.service.AreaService;
import com.carbarn.inter.utils.AjaxResult;
import org.aspectj.weaver.loadtime.Aj;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService{

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public AjaxResult getProvince() {
        try{
            List<AreaPOJO> areaPOJOS = areaMapper.getProvince();
            return AjaxResult.success("getProvince successful", areaPOJOS);
        }catch (Exception e){
            e.printStackTrace();
        }


        return AjaxResult.error("getProvince failed");
    }

    @Override
    public AjaxResult getCity(String code) {
        try{
            List<AreaPOJO> areaPOJOS = areaMapper.getCity(code);
            return AjaxResult.success("getCity successful", areaPOJOS);
        }catch (Exception e){
            e.printStackTrace();
        }


        return AjaxResult.error("getCity failed");
    }
}