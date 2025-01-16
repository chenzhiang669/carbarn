package com.carbarn.im.translator;

/**
 * 翻译器接口
 *
 * @Author zoulingxi
 * @Date 2025/1/15 21:29
 */
public interface Translator {

    default Translator getInstance() {
        return new VolcTranslator();
    }

    /**
     * 翻译文本
     *
     * @param text       原始文本
     * @param sourceLang 源语言代码，如 "en" 表示英文
     * @param targetLang 目标语言代码，如 "zh" 表示中文
     * @return 翻译后的文本
     */
    String translate(String text, String sourceLang, String targetLang);

    /**
     * 检测文本语言
     *
     * @param text 文本
     * @return 语言代码
     */
    String detectLang(String text);

    /**
     * 获取翻译器类型
     *
     * @return
     * @author zoulingxi
     * @date 2025/1/15 21:29
     */
    String getType();
}