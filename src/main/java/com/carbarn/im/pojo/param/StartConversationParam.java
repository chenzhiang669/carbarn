package com.carbarn.im.pojo.param;

import lombok.Data;

/**
 * @author zoulingxi
 * @description 开启会话Req
 * @date 2025/1/11 14:37
 */
@Data
public class StartConversationParam {

    private Long buyerId;
    private Long sellerId;
}