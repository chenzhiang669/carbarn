package com.carbarn.im.translator;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.stereotype.Component;

import static com.carbarn.im.translator.TranslatorEnum.GOOGLE;

@Component
public class GoogleTranslator implements Translator {
    private final Translate translate;

    public GoogleTranslator() {
        this.translate = TranslateOptions.newBuilder().setApiKey("YOUR_API_KEY").build().getService();
    }

    @Override
    public String translate(String text, String sourceLang, String targetLang) {
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(sourceLang),
                Translate.TranslateOption.targetLanguage(targetLang)
        );
        return translation.getTranslatedText();
    }

    @Override
    public String detectLang(String text) {
        return translate.detect(text).getLanguage();
    }

    @Override
    public String getType() {
        return GOOGLE.getType();
    }
}