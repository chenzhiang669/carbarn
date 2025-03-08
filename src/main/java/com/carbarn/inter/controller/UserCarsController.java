package com.carbarn.inter.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.service.UserCarsService;
import com.carbarn.inter.service.UserService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.sms.SendSms;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户车辆信息服务")
@RestController
@RequestMapping("/carbarn/usercar")
public class UserCarsController {
    @Autowired
    private UserCarsService userCarsService;

    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody String body) {
        JSONObject body_json = JSON.parseObject(body);
        if (!body_json.containsKey("carid")) {
            return AjaxResult.error("Missing required parameter: carid");
        }

        int carid = body_json.getInteger("carid");

        String user_id = (String) StpUtil.getLoginId();
        long userid = Long.valueOf(user_id);

        return userCarsService.deleteCars(carid, userid);
    }

    @PostMapping("/unreview")
    public AjaxResult unreview(@RequestBody String body) {
        JSONObject body_json = JSON.parseObject(body);
        if (!body_json.containsKey("carid")) {
            return AjaxResult.error("Missing required parameter: carid");
        }

        int carid = body_json.getInteger("carid");

        String user_id = (String) StpUtil.getLoginId();
        long userid = Long.valueOf(user_id);

        return userCarsService.unreviewCars(carid, userid);
    }


    @PostMapping("/delist")
    public AjaxResult delist(@RequestBody String body) {
        JSONObject body_json = JSON.parseObject(body);
        if (!body_json.containsKey("carid")) {
            return AjaxResult.error("Missing required parameter: carid");
        }

        int carid = body_json.getInteger("carid");

        String user_id = (String) StpUtil.getLoginId();
        long userid = Long.valueOf(user_id);

        return userCarsService.delist(carid, userid);
    }


    @PostMapping("/list")
    public AjaxResult list(@RequestBody String body) {
        JSONObject body_json = JSON.parseObject(body);
        if (!body_json.containsKey("state")) {
            return AjaxResult.error("Missing required parameter: state");
        }

        int state = body_json.getInteger("state");

        String user_id = (String) StpUtil.getLoginId();
        long userid = Long.valueOf(user_id);

        return userCarsService.selectUserCars(userid, state);
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody String body) {
        JSONObject body_json = JSON.parseObject(body);
        if (!body_json.containsKey("carid")) {
            return AjaxResult.error("Missing required parameter: carid");
        }
        int carid = body_json.getInteger("carid");

        String user_id = (String) StpUtil.getLoginId();
        long userid = Long.valueOf(user_id);

        return userCarsService.edit(carid, userid);
    }


    @PostMapping("/statecount")
    public AjaxResult statecount() {
        String user_id = (String) StpUtil.getLoginId();
        long userid = Long.valueOf(user_id);
        return userCarsService.selectStateCount(userid);
    }


}
