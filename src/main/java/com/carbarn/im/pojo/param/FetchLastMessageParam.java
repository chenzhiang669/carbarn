package com.carbarn.im.pojo.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zoulingxi
 * @description 拉取会话消息参数
 * @date 2025/1/14 23:26
 */
@Data
public class FetchLastMessageParam {
    @NotNull(message = "会话ID不能为空")
    private Long conversationId;
    private Long lastMessageId;
}