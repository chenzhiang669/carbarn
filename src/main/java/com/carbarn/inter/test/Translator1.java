package com.carbarn.inter.test;

import com.carbarn.im.translator.VolcTranslator;
import com.volcengine.model.request.translate.TranslateTextRequest;
import com.volcengine.model.response.translate.TranslateTextResponse;
import com.volcengine.service.translate.ITranslateService;
import com.volcengine.service.translate.impl.TranslateServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collections;

public class Translator1 {

        private static final Logger LOGGER = LoggerFactory.getLogger(VolcTranslator.class);

        private ITranslateService translateService;

        public Translator1() {
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
                System.out.println(translateText);
                String result = translateText.getTranslationList().get(0).getTranslation();
                return result;
            } catch (Exception e) {
                LOGGER.error("translate error, text: {}, sourceLang: {}, targetLang: {}", text, sourceLang, targetLang, e);
            }
            return "";
        }

        public static void car_series() throws IOException {
//            Translator1 translator = new Translator1();

            BufferedReader br = new BufferedReader(new FileReader(new File("D:/car_type.csv")));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/car_type-en.csv")));
            String line = null;

            while((line = br.readLine()) != null){
                String[] infos = line.split(",",3);
                String series = infos[1];
                System.out.println(series);
                Translator1 translator = new Translator1();
                String result = translator.translate(series,"zh","en");
                bw.write(infos[0] + ",\"" + result.replaceAll("\"","") + "\"," + infos[2] + "\n");
                bw.flush();
            }
            bw.flush();
            bw.close();
        }
    public static void main(String[] args) throws IOException {
        car_series();

//        Translator1 translator = new Translator1();
//        String result = translator.translate("2011款 620 1.5 雪地版精英型(6.18万)","zh","en");
//        System.out.println(result);
    }
}
