package com.carbarn.im.controller;


import com.carbarn.common.pojo.CommonResult;
import com.carbarn.im.convert.MessageConvert;
import com.carbarn.im.pojo.Message;
import com.carbarn.im.pojo.param.FetchLastMessageParam;
import com.carbarn.im.pojo.param.SendMessageParam;
import com.carbarn.im.pojo.resp.BasePageResp;
import com.carbarn.im.pojo.vo.SendMessageVo;
import com.carbarn.im.service.IMessageService;
import com.carbarn.inter.helper.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carbarn/message")
public class MessageController {

    @Autowired
    private IMessageService messageService;


    @PostMapping("/send")
    public CommonResult<SendMessageVo> sendMessage(@RequestBody SendMessageParam sendMessageParam) {
        Long senderId = UserHelper.nowLoginUser().getId();
        Message message = messageService.sendMessage(senderId, sendMessageParam);
        return CommonResult.success(MessageConvert.INSTANCE.messageToVo(message));
    }

    @PostMapping("/syncLatest")
    public CommonResult<List<Message>> fetchLastMessage(@RequestBody FetchLastMessageParam fetchLastMessageParam) {
        Long userId = UserHelper.nowLoginUser().getId();
        List<Message> messages = messageService.fetchSyncMessage(userId, fetchLastMessageParam);
        return CommonResult.success(messages);
    }

    @GetMapping("/page")
    public CommonResult<BasePageResp<Message>> getMessagesByPage(@RequestParam Long conversationId,
                                                                 @RequestParam(defaultValue = "0") Integer pageNum,
                                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserHelper.nowLoginUser().getId();
        BasePageResp<Message> messagePage = messageService.getMessagesByPage(userId, conversationId, pageNum, pageSize);
        return CommonResult.success(messagePage);
    }
}
