package com.carbarn.im.mapper;

import com.carbarn.im.pojo.Conversation;
import com.carbarn.im.pojo.vo.ConversationVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zoulingxi
 * @description
 * @date 2025/1/12 21:02
 */
@Mapper
public interface ConversationMapper {
    /**
     * 根据买家id和卖家id获取会话
     *
     * @param buyerId
     * @param sellerId
     * @return
     */
    Conversation getConversation(@Param("buyerId") Long buyerId, @Param("sellerId") Long sellerId);

    /**
     * 查询会话
     *
     * @param id 会话id
     * @return
     */
    Conversation getById(@Param("id") Long id);

    /**
     * 查询会话列表
     *
     * @param userId 用户id
     * @return
     */
    List<ConversationVo> conversationList(Long userId);

    /**
     * 开启会话
     *
     * @param conversation 会话
     * @return 会话id
     */
    void startConversion(@Param("conversation") Conversation conversation);
}