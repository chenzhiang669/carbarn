package com.carbarn.im.mapper;

import com.carbarn.im.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zoulingxi
 * @description
 * @date 2025/1/13 23:30
 */
@Mapper
public interface MessageMapper {
    Integer unreadMessageCount(@Param("userId") Long userId, @Param("conversationId") Long conversationId);

    void insertMessage(@Param("message") Message message);

    Message getLastMessage(@Param("conversationId") Long conversationId);

    List<Message> getUnreadMessages(@Param("userId") Long userId, @Param("conversationId") Long conversationId);

    void updateMessageStatus(@Param("userId") Long userId, @Param("conversationId") Long conversationId);

    List<Message> getAllMessages(@Param("userId") Long userId, @Param("conversationId") Long conversationId);
}