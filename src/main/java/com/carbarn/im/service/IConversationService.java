package com.carbarn.im.service;

import com.carbarn.im.pojo.param.StartConversationParam;
import com.carbarn.im.pojo.resp.ConversationPageResp;
import com.carbarn.im.pojo.vo.StartConversationVo;

/**
 * @author zoulingxi
 * @description 会话服务
 * @date 2025/1/11 14:39
 */
public interface IConversationService {

    StartConversationVo startConversion(StartConversationParam startConversationParam);

    ConversationPageResp getConversationsByPage(Long userId, Integer pageNum, Integer pageSize);

    void clearUnread(Long userId);
}