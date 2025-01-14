package com.carbarn.im.convert;

import com.carbarn.im.pojo.Message;
import com.carbarn.im.pojo.vo.SendMessageVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author zoulingxi
 * @description 消息转换类
 * @date 2025/1/14 22:05
 */
@Mapper
public interface MessageConvert {
    MessageConvert INSTANCE = Mappers.getMapper(MessageConvert.class);

    SendMessageVo messageToVo(Message message);
}