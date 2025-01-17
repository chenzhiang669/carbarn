package com.carbarn.im.pojo;

import lombok.Data;

/**
 * @author zoulingxi
 * @description 会话实体类
 * @date 2025/1/12 21:03
 */
@Data
public class Conversation {
    private Long id;
    private Long sellerId;
    private Long buyerId;
    // closed active
    private String status;
    private Long createTime;
    private Long updateTime;
    private Integer isDeleted;
}