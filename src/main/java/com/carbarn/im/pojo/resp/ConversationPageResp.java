package com.carbarn.im.pojo.resp;

import com.carbarn.im.pojo.vo.ConversationVo;
import lombok.Data;

/**
 * @author zoulingxi
 * @description 会话分页类
 * @date 2025/1/14 21:36
 */
@Data
public class ConversationPageResp extends BasePageResp<ConversationVo> {

    private long unreadCount;
}