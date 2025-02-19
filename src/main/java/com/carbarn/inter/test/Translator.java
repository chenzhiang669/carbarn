package com.carbarn.inter.test;

import com.carbarn.im.translator.TranslatorFactory;
import com.carbarn.im.translator.VolcTranslator;
import com.volcengine.model.request.translate.TranslateTextRequest;
import com.volcengine.model.response.translate.TranslateTextResponse;
import com.volcengine.service.translate.ITranslateService;
import com.volcengine.service.translate.impl.TranslateServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Collections;

import static com.carbarn.im.translator.TranslatorEnum.VOLC;

public class Translator {

        private static final Logger LOGGER = LoggerFactory.getLogger(com.carbarn.im.translator.VolcTranslator.class);

        private ITranslateService translateService;

        public Translator() {
            translateService = TranslateServiceImpl.getInstance();
            translateService.setAccessKey("AKLTZGE4NGY2ZGM4MzAyNGQzMmExZWNmZWU3YjU0MDQ3NmU");
            translateService.setSecretKey("T0RobVlXTXhPR1ZpWVRRNE5EQmlZbUl5TlRZMFlqY3paall5T1RWbFptRQ==");
        }

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
            return "";
        }

        public static void car_series() throws IOException {
            Translator translator = new Translator();

            BufferedReader br = new BufferedReader(new FileReader(new File("D:/car_series.csv")));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/car_series-en.csv")));
            String line = null;

            while((line = br.readLine()) != null){
                String[] infos = line.split(",",4);
                String series = infos[2];
                String result = translator.translate(series,"zh","en");
                bw.write(infos[0] + "," + infos[1]+",\"" + result + "\"," + infos[3] + "\n");
                bw.flush();
            }
            bw.flush();
            bw.close();
        }
    public static void main(String[] args) throws IOException {
        car_series();
    }
}
