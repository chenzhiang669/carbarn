package com.carbarn.im.enums;

/**
 * @author zoulingxi
 * @description 会话状态枚举类
 * @date 2025/1/13 22:46
 */
public enum ConversationStatus {
    active("active"), closed("closed");

    private String status;

    ConversationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
