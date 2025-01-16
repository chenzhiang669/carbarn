package com.carbarn.im.translator;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zoulingxi
 * @description 翻译器工厂类
 * @date 2025/1/16 21:25
 */
@Component
public class TranslatorFactory implements BeanPostProcessor {

    private static final Map<String, Translator> translatorMap = new HashMap<>();

    public static Translator getTranslator(String type) {
        return translatorMap.get(type);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof Translator) {
            // 初始化翻译器
            translatorMap.put(((Translator) bean).getType(), (Translator) bean);
        }
        return bean;
    }
}