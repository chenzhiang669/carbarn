package com.carbarn.im.service.impl;

import com.carbarn.im.convert.ConversationConverter;
import com.carbarn.im.mapper.ConversationMapper;
import com.carbarn.im.pojo.Conversation;
import com.carbarn.im.pojo.Message;
import com.carbarn.im.pojo.resp.ConversationPageResp;
import com.carbarn.im.pojo.vo.ConversationVo;
import com.carbarn.im.pojo.vo.StartConversationVo;
import com.carbarn.im.service.IConversationService;
import com.carbarn.im.service.IMessageService;
import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.service.UserService;
import com.carbarn.inter.utils.AjaxResult;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.carbarn.im.enums.ConversationStatus.active;

/**
 * @author zoulingxi
 * @description 会话服务实现类
 * @date 2025/1/11 14:41
 */
@Service
public class ConversationSericeImpl implements IConversationService {
    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public StartConversationVo startConversion(Long buyerId, Long sellerId) {
        Conversation conversation = conversationMapper.getConversation(buyerId, sellerId);
        if (conversation == null) {
            conversation = new Conversation();
            conversation.setBuyerId(buyerId);
            conversation.setSellerId(sellerId);
            conversation.setCreateTime(System.currentTimeMillis());
            conversation.setUpdateTime(System.currentTimeMillis());
            conversation.setStatus(active.getStatus());
            conversationMapper.startConversion(conversation);
        }
        return ConversationConverter.INSTANCE.conversationToVo(conversation);
    }

    @Override
    public ConversationPageResp getConversationsByPage(Long userId, Integer pageNum, Integer pageSize) {
        PageMethod.startPage(pageNum, pageSize);
        List<ConversationVo> conversations = conversationMapper.conversationList(userId);
        PageInfo<ConversationVo> conversationPageInfo = new PageInfo<>(conversations);
        int totalUnread = 0;
        for (ConversationVo conversation : conversationPageInfo.getList()) {
            conversation.setBuyer(userService.getUserInfoByID(conversation.getBuyerId()));
            conversation.setSeller(userService.getUserInfoByID(conversation.getSellerId()));
            Integer conversationUnreadCount = messageService.countUnreadMessages(userId, conversation.getId());
            Message message = messageService.fetchLastMessage(conversation.getId());
            if (message != null) {
                conversation.setLastMessageId(message.getId());
                conversation.setLastMessageContent(message.getContent());
                conversation.setLastMessageTime(message.getSendTime());
                conversation.setLastMessageType(message.getType());
            }
            conversation.setUnreadCount(conversationUnreadCount);
            totalUnread += conversationUnreadCount;
        }
        ConversationPageResp conversationPageResp = new ConversationPageResp();
        conversationPageResp.setCurrent(conversationPageInfo.getPageNum());
        conversationPageResp.setSize(conversationPageInfo.getPageSize());
        conversationPageResp.setRecords(conversationPageInfo.getList());
        conversationPageResp.setTotal(conversationPageInfo.getTotal());
        conversationPageResp.setUnreadCount(totalUnread);
        return conversationPageResp;
    }

    @Override
    public void clearUnread(Long userId) {
        // 清除未读消息
        messageService.readMessages(userId);
    }

    @Override
    public AjaxResult conversationUsers(long conversationId) {
        Conversation conversation = conversationMapper.getById(conversationId);
        if(conversation == null){
            return AjaxResult.error("会话id无效");
        }

        long buyer_id = conversation.getBuyerId();
        long seller_id = conversation.getSellerId();

        UserPojo buyer = userService.getUserInfoByID(buyer_id);
        UserPojo seller = userService.getUserInfoByID(seller_id);


        Map<String, UserPojo> userInfos = new HashMap<String, UserPojo>();
        userInfos.put("buyer", buyer);
        userInfos.put("seller", seller);

        return AjaxResult.success("获取会话用户信息成功", userInfos);
    }
}