package com.carbarn.im.convert;

import com.carbarn.im.pojo.Conversation;
import com.carbarn.im.pojo.vo.StartConversationVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author zoulingxi
 * @description 会话实体类转换工具
 * @date 2025/1/13 22:53
 */
@Mapper
public interface ConversationConverter {
    ConversationConverter INSTANCE = Mappers.getMapper(ConversationConverter.class);

    StartConversationVo conversationToVo(Conversation conversation);

}