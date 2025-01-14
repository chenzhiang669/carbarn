package com.carbarn.im.pojo.param;

import lombok.Data;

/**
 * @author zoulingxi
 * @description 消息发送参数
 * @date 2025/1/14 21:50
 */
@Data
public class SendMessageParam {
    private Long receiverId;
    private String type;
    private String content;
    private Long conversationId;
    private String sourceLanguage;
    private String targetLanguage;
}