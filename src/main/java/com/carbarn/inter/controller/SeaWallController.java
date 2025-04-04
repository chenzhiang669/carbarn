package com.carbarn.inter.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
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

@Api(tags = "拓海塘服务")
@RestController
@RequestMapping("/carbarn/seawall")
public class SeaWallController {
    @Autowired
    private SeaWallService seaWallService;

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
}
