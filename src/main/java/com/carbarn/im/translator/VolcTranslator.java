package com.carbarn.im.translator;

import com.volcengine.model.request.translate.LangDetectRequest;
import com.volcengine.model.request.translate.TranslateTextRequest;
import com.volcengine.model.response.translate.LangDetectResponse;
import com.volcengine.model.response.translate.TranslateTextResponse;
import com.volcengine.service.translate.ITranslateService;
import com.volcengine.service.translate.impl.TranslateServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

import static com.carbarn.im.translator.TranslatorEnum.VOLC;

/**
 * 翻译器接口
 *
 * @Author zoulingxi
 * @Date 2025/1/15 21:29
 */
@Component
public class VolcTranslator implements Translator {

    private static final Logger LOGGER = LoggerFactory.getLogger(VolcTranslator.class);

    private ITranslateService translateService;

    @Value("${translate.volc.apiKeyId}")
    private String apiKeyId;
    @Value("${translate.volc.secretAccessKey}")
    private String secretAccessKey;

    @PostConstruct
    public void init() {
        translateService = TranslateServiceImpl.getInstance();
        translateService.setAccessKey(apiKeyId);
        translateService.setSecretKey(secretAccessKey);
    }

    @Override
    public String translate(String text, String sourceLang, String targetLang) {
        try {
            TranslateTextRequest translateTextRequest = new TranslateTextRequest();
            // 不设置表示自动检测
            translateTextRequest.setSourceLanguage(sourceLang);
            translateTextRequest.setTargetLanguage(targetLang);
            translateTextRequest.setTextList(Collections.singletonList(text));
            TranslateTextResponse translateText = translateService.translateText(translateTextRequest);
            return translateText.getTranslationList().get(0).getTranslation();
        } catch (Exception e) {
            LOGGER.error("translate error, text: {}, sourceLang: {}, targetLang: {}", text, sourceLang, targetLang, e);
        }
        return null;
    }

    @Override
    public String detectLang(String text) {
        try {
            LangDetectRequest langDetectRequest = new LangDetectRequest();
            langDetectRequest.setTextList(Collections.singletonList(text));
            LangDetectResponse langDetectResponse = translateService.langDetect(langDetectRequest);
            return langDetectResponse.getDetectedLanguageList().get(0).getLanguage();
        } catch (Exception e) {
            LOGGER.error("detect lang error, text: {}", text, e);
        }
        return null;
    }

    @Override
    public String getType() {
        return VOLC.getType();
    }
}