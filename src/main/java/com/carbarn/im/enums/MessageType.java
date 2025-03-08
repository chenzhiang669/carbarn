package com.carbarn.im.enums;

/**
 * @author zoulingxi
 * @description 消息类型
 * @date 2025/1/17 22:14
 */
public enum MessageType {
    TEXT("text"),
    IMAGE("image"),
    LINK("link"),
    ;

    private String type;

    MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
