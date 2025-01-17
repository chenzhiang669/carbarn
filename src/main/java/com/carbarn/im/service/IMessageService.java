package com.carbarn.im.service;

import com.carbarn.im.pojo.Message;
import com.carbarn.im.pojo.param.FetchLastMessageParam;
import com.carbarn.im.pojo.param.SendMessageParam;
import com.carbarn.im.pojo.resp.BasePageResp;

import java.util.List;

/**
 * @author zoulingxi
 * @description 消息服务
 * @date 2025/1/13 23:28
 */
public interface IMessageService {
    void readMessages(Long userId);

    Integer countUnreadMessages(Long userId, Long conversationId);

    Message sendMessage(Long senderId, SendMessageParam sendMessageParam);

    Message fetchLastMessage(Long conversationId);

    List<Message> fetchSyncMessage(Long userId, FetchLastMessageParam fetchLastMessageParam);

    BasePageResp<Message> getMessagesByPage(Long userId, Long conversationId, Integer pageNum, Integer pageSize);
}