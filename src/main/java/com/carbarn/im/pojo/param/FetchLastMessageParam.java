package com.carbarn.im.pojo.param;

import lombok.Data;

/**
 * @author zoulingxi
 * @description 拉取会话消息参数
 * @date 2025/1/14 23:26
 */
@Data
public class FetchLastMessageParam {
    private Long conversationId;
    private Long lastMessageId;
}