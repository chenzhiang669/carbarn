package com.carbarn.im.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.common.exception.enums.ErrorCode;
import com.carbarn.common.pojo.CommonResult;
import com.carbarn.im.pojo.param.StartConversationParam;
import com.carbarn.im.pojo.resp.ConversationPageResp;
import com.carbarn.im.pojo.vo.StartConversationVo;
import com.carbarn.im.service.IConversationService;
import com.carbarn.inter.helper.UserHelper;
import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.utils.AjaxResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zoulingxi
 */
@Api(tags = "会话服务")
@RestController
@RequestMapping("/carbarn/conversation")
public class ConversationController {

    @Autowired
    private IConversationService conversationService;

    @PostMapping("/start")
    public CommonResult<StartConversationVo> startConversation(@RequestBody StartConversationParam startConversationParam) {
        UserPojo user = UserHelper.nowLoginUser();
        return CommonResult.success(conversationService.startConversion(user.getId(), startConversationParam.getSellerId()));
    }

    @PostMapping("/startBySeller")
    public CommonResult<StartConversationVo> startConversationBySeller(@RequestBody StartConversationParam startConversationParam) {
        if(startConversationParam.getBuyerId() == 0){
            return CommonResult.error(ErrorCode.BAD_REQUEST.getCode(), "Missing required parameter: buyerId");
        }
        UserPojo user = UserHelper.nowLoginUser();
        return CommonResult.success(conversationService.startConversion(startConversationParam.getBuyerId(), user.getId()));
    }

    @GetMapping("/page")
    public CommonResult<ConversationPageResp> conversationList(@RequestParam(defaultValue = "0") Integer pageNum,
                                                               @RequestParam(defaultValue = "10") Integer pageSize) {
        UserPojo user = UserHelper.nowLoginUser();
        return CommonResult.success(conversationService.getConversationsByPage(user.getId(), pageNum, pageSize));
    }

    @PostMapping("/clearUnread")
    public CommonResult<String> clearUnread() {
        conversationService.clearUnread(UserHelper.nowLoginUser().getId());
        return CommonResult.success("清除成功");
    }

    @PostMapping("/users")
    public AjaxResult conversationUsers(@RequestBody String body) {
        JSONObject json = JSON.parseObject(body);
        if (!json.containsKey("conversationId")) {
            return AjaxResult.error("Missing required parameter: conversationId");
        }

        long conversationId = json.getLong("conversationId");
        return conversationService.conversationUsers(conversationId);
    }
}
