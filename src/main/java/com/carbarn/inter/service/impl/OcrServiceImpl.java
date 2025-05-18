package com.carbarn.inter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.mapper.UserMapper;
import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.pojo.user.pojo.UserViewCountPojo;
import com.carbarn.inter.service.OcrService;
import com.carbarn.inter.service.UserService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.Utils;
import com.carbarn.inter.utils.alibaba.VinOcr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OcrServiceImpl implements OcrService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ParamsMapper paramsMapper;

    @Override
    public AjaxResult vinOcr(MultipartFile file) {
        try{
            String param_vin_ocr = paramsMapper.getValue(ParamKeys.param_vin_ocr);
            JSONObject json = JSON.parseObject(param_vin_ocr);
            String host = json.getString("host");
            String path = json.getString("path");
            String appcode = json.getString("appcode");

            byte[] bytes = file.getBytes();
            String vin = VinOcr.vinOcr(host, path, appcode, bytes);
            if(vin != null && !"".equals(vin)){
                Map<String, String> result = new HashMap<String,String>();
                result.put("vin", vin);
                return AjaxResult.success("ocr vin successfully", result);
            }
        }catch (Exception e){

        }

        return AjaxResult.error("ocr vin failed");
    }
}