package com.carbarn.inter.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.pojo.seawall.SeaWallPOJO;
import com.carbarn.inter.pojo.seawall.SeaWallPageDTO;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.service.SeaWallService;
import com.carbarn.inter.service.UserService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.sms.SendSms;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "拓海塘服务")
@RestController
@RequestMapping("/carbarn/seawall")
public class SeaWallController {
    @Autowired
    private SeaWallService seaWallService;
    @Autowired
    private ParamsMapper paramsMapper;

    @PostMapping("/getSeaWall")
    public AjaxResult getSeaWall(@RequestBody SeaWallPageDTO seaWallPageDTO) {
        int pageNo = seaWallPageDTO.getPageNo();
        int pageSize = seaWallPageDTO.getPageSize();
        if (pageNo < 1) {
            return AjaxResult.error("Missing required parameter: pageNo");
        }
        if (pageSize <= 0) {
            return AjaxResult.error("'pageSize' Must meet the conditions  pageSize > 0");
        } else {
            seaWallPageDTO.setPageStart((pageNo - 1) * pageSize);
        }

        List<SeaWallPOJO> seawalls = seaWallService.getSeaWall(seaWallPageDTO);
        return AjaxResult.success("获取拓海塘信息成功", seawalls);
    }

    @PostMapping("/insertNewSeaWall")
    public AjaxResult insertNewSeaWall(@RequestBody SeaWallPOJO seaWallPOJO) {
        String title = seaWallPOJO.getTitle();
        int seaWallType = seaWallPOJO.getSeaWallType();
        String picture = seaWallPOJO.getPicture();
        String url = seaWallPOJO.getUrl();

        if(title == null || seaWallType == -1 || picture == null || url == null){
            return AjaxResult.error("error: required parameter: [title,seaWallType,picture,url]");
        }

        //只有白名单里面的人员才有操作的权限
        if(!isUserOperator()){
            return AjaxResult.error("The current user does not have permission to perform this operation.");
        }



        try{
            seaWallService.insertNewSeaWall(seaWallPOJO);
            return AjaxResult.success("insert seawall success");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("insert seawall fail");
        }
    }

    @PostMapping("/deleteSeaWall")
    public AjaxResult deleteSeaWall(@RequestBody SeaWallPOJO seaWallPOJO) {
        int id = seaWallPOJO.getId();
        if(id == 0){
            return AjaxResult.error("error: required parameter: [id]");
        }

        //只有白名单里面的人员才有操作的权限
        if(!isUserOperator()){
            return AjaxResult.error("The current user does not have permission to perform this operation.");
        }

        try{
            seaWallService.deleteSeaWall(id);
            return AjaxResult.success("delete seawall success");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("delete seawall fail");
        }
    }

    //判断用户是否是我们的运营人员
    private boolean isUserOperator(){
        String operate_whitelist = paramsMapper.getValue(ParamKeys.param_operate_whitelist);
        JSONArray operate_whitelist_array = JSON.parseArray(operate_whitelist);
        Set<Long> whitelist = operate_whitelist_array.stream().map(x -> {
            return Long.valueOf(x.toString());
        }).collect(Collectors.toSet());

        String user_id = (String) StpUtil.getLoginId();
        long user_id_long = Long.valueOf(user_id);

        //只有白名单里面的人员才有操作的权限
        if(!whitelist.contains(user_id_long)){
            return false;
        }else{
            return true;
        }
    }


}
