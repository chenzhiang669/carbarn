package com.carbarn.im.service.impl;

import com.carbarn.im.mapper.ConversationMapper;
import com.carbarn.im.mapper.MessageMapper;
import com.carbarn.im.pojo.Message;
import com.carbarn.im.pojo.param.FetchLastMessageParam;
import com.carbarn.im.pojo.param.SendMessageParam;
import com.carbarn.im.pojo.resp.BasePageResp;
import com.carbarn.im.service.IMessageService;
import com.carbarn.im.translator.Translator;
import com.carbarn.im.translator.TranslatorFactory;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.carbarn.im.enums.MessageType.*;
import static com.carbarn.im.translator.TranslatorEnum.VOLC;

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
        message.setSourceLang(sendMessageParam.getSourceLang());
        message.setTargetLang(sendMessageParam.getTargetLang());
        //翻译消息
        String sourceLang = sendMessageParam.getSourceLang();
        String targetLang = sendMessageParam.getTargetLang();
        if (TEXT.getType().equals(sendMessageParam.getType())) {
            if (sourceLang.equals(targetLang)) {
                message.setTranslatedContent(sendMessageParam.getContent());
            } else {
                Translator translator = TranslatorFactory.getTranslator(VOLC.getType());
                String translatedContent = translator.translate(sendMessageParam.getContent(), sendMessageParam.getSourceLang(), sendMessageParam.getTargetLang());
                message.setTranslatedContent(translatedContent);
            }
        }else if(IMAGE.getType().equals(sendMessageParam.getType())){
            message.setTranslatedContent(sendMessageParam.getContent());
        }else if(LINK.getType().equals(sendMessageParam.getType())){
            message.setTranslatedContent(sendMessageParam.getContent());
        }else if(PRODUCT.getType().equals(sendMessageParam.getType())){
            message.setTranslatedContent(sendMessageParam.getContent());
        }else{
            message.setTranslatedContent(sendMessageParam.getContent());
        }

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
        if (CollectionUtils.isNotEmpty(messages)) {
            messageMapper.updateMessageStatus(userId, fetchLastMessageParam.getConversationId());
        }
        return messages;
    }

    @Override
    public BasePageResp<Message> getMessagesByPage(Long userId, Long conversationId, Integer pageNum, Integer pageSize) {
        PageMethod.startPage(pageNum, pageSize);
        List<Message> messages = messageMapper.getAllMessages(userId, conversationId);
        if(CollectionUtils.isNotEmpty(messages)){
            messageMapper.updateMessageStatus(userId, conversationId);
        }
        PageInfo<Message> messagePage = new PageInfo<>(messages);
        BasePageResp<Message> messagePageResp = new BasePageResp<>();
        messagePageResp.setCurrent(messagePage.getPageNum()).setSize(messagePage.getPageSize())
                .setRecords(messagePage.getList()).setTotal(messagePage.getTotal());
        return messagePageResp;
    }

}