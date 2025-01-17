package com.carbarn.im.pojo.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author zoulingxi
 * @description 消息发送参数
 * @date 2025/1/14 21:50
 */
@Data
public class SendMessageParam {
    @NotNull(message = "接收者ID不能为空")
    private Long receiverId;
    @NotEmpty(message = "消息类型不能为空")
    private String type;
    @NotEmpty(message = "消息内容不能为空")
    private String content;
    @NotNull(message = "会话ID不能为空")
    private Long conversationId;
    @NotEmpty(message = "源语言不能为空")
    private String sourceLang;
    @NotEmpty(message = "目标语言不能为空")
    private String targetLang;
}