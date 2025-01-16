package com.carbarn.im.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author zoulingxi
 * @description 消息
 * @date 2025/1/13 23:28
 */
@Data
public class Message {

    private Long id;
    private Long conversationId;
    private Long senderId;
    private Long receiverId;
    // text, image, link
    private String type;
    private String content;
    private String sourceLang;
    private String targetLang;
    private String translatedContent;
    private Long sendTime;
    private Long receiveTime;
    private Integer status;
    @JsonIgnore
    private Integer isDeleted;

}