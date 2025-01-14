package com.carbarn.im.service.impl;

import com.carbarn.im.mapper.ConversationMapper;
import com.carbarn.im.mapper.MessageMapper;
import com.carbarn.im.pojo.Message;
import com.carbarn.im.pojo.param.FetchLastMessageParam;
import com.carbarn.im.pojo.param.SendMessageParam;
import com.carbarn.im.pojo.resp.BasePageResp;
import com.carbarn.im.service.IMessageService;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zoulingxi
 * @description 消息服务实现类
 * @date 2025/1/13 23:28
 */
@Service
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private ConversationMapper conversationMapper;

    @Override
    public void readMessages(Long userId) {
        messageMapper.updateMessageStatus(userId, null);
    }

    @Override
    public Integer countUnreadMessages(Long userId, Long conversationId) {
        return messageMapper.unreadMessageCount(userId, conversationId);
    }

    @Override
    public Message sendMessage(Long senderId, SendMessageParam sendMessageParam) {
        //检验会话是否存在
        Preconditions.checkArgument(conversationMapper.getById(sendMessageParam.getConversationId()) != null, "会话不存在");
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(sendMessageParam.getReceiverId());
        message.setConversationId(sendMessageParam.getConversationId());
        message.setContent(sendMessageParam.getContent());
        message.setType(sendMessageParam.getType());
        message.setSendTime(System.currentTimeMillis());
        message.setStatus(0);
        messageMapper.insertMessage(message);
        return message;
    }

    @Override
    public Message fetchLastMessage(Long conversationId) {
        return messageMapper.getLastMessage(conversationId);
    }

    @Override
    public List<Message> fetchSyncMessage(Long userId, FetchLastMessageParam fetchLastMessageParam) {
        List<Message> messages = messageMapper.getUnreadMessages(userId, fetchLastMessageParam.getConversationId());
        messageMapper.updateMessageStatus(userId, fetchLastMessageParam.getConversationId());
        return messages;
    }

    @Override
    public BasePageResp<Message> getMessagesByPage(Long userId, Long conversationId, Integer pageNum, Integer pageSize) {
        PageMethod.startPage(pageNum, pageSize);
        List<Message> messages = messageMapper.getAllMessages(userId, conversationId);
        PageInfo<Message> messagePage = new PageInfo<>(messages);
        BasePageResp<Message> messagePageResp = new BasePageResp<>();
        messagePageResp.setCurrent(messagePage.getPageNum()).setSize(messagePage.getPageSize())
                .setRecords(messagePage.getList()).setTotal(messagePage.getTotal());
        return messagePageResp;
    }

}