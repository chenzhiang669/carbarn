package com.carbarn.im.controller;

import com.carbarn.common.pojo.CommonResult;
import com.carbarn.im.pojo.param.StartConversationParam;
import com.carbarn.im.pojo.resp.ConversationPageResp;
import com.carbarn.im.pojo.vo.StartConversationVo;
import com.carbarn.im.service.IConversationService;
import com.carbarn.inter.helper.UserHelper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zoulingxi
 */
@Api(tags = "im", value = "会话服务")
@RestController
@RequestMapping("/carbarn/conversation")
public class ConversationController {

    @Autowired
    private IConversationService conversationService;

    @PostMapping("/start")
    public CommonResult<StartConversationVo> startConversation(@RequestBody StartConversationParam startConversationParam) {
        return CommonResult.success(conversationService.startConversion(startConversationParam));
    }

    @GetMapping("/page")
    public CommonResult<ConversationPageResp> conversationList(@RequestParam(defaultValue = "0") Integer pageNum,
                                                               @RequestParam(defaultValue = "10") Integer pageSize) {
        return CommonResult.success(conversationService.getConversationsByPage(UserHelper.nowLoginUser().getId(), pageNum, pageSize));
    }

    @PostMapping("/clearUnread")
    public CommonResult<String> clearUnread() {
        conversationService.clearUnread(UserHelper.nowLoginUser().getId());
        return CommonResult.success("清除成功");
    }
}
