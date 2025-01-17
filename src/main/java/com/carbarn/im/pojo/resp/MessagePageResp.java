package com.carbarn.im.pojo.resp;

import com.carbarn.im.pojo.vo.ConversationVo;

/**
 * @author zoulingxi
 * @description 消息分页返回类
 * @date 2025/1/14 23:40
 */
public class MessagePageResp extends BasePageResp<ConversationVo> {

    private long unreadCount;
}