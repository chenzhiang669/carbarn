package com.carbarn.im.pojo.vo;

import com.carbarn.inter.pojo.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author zoulingxi
 * @description 会话vo
 * @date 2025/1/13 23:22
 */
@Data
public class ConversationVo {
    private Long id;
    private String name;
    @JsonIgnore
    private Long sellerId;
    @JsonIgnore
    private Long buyerId;
    private Integer unreadCount;
    private User seller;
    private User buyer;
    private Long lastMessageId;
    // 0:文本 1:图片 2:链接
    private String lastMessageType;
    private String lastMessageContent;
    private Long lastMessageTime;
    // 0:未读 1:已读
    private String status;
    private Long createTime;
    private Long updateTime;
    private Integer isDeleted;
}