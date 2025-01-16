package com.carbarn.im.translator;

/**
 * @author zoulingxi
 * @description 翻译器类型枚举类
 * @date 2025/1/16 21:29
 */
public enum TranslatorEnum {
    VOLC("volc"),
    MICROSOFT("microSoft"),
    GOOGLE("google");

    private final String type;

    TranslatorEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
