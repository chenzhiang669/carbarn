package com.carbarn.inter.test;

import ch.qos.logback.classic.Level;
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
            translateService.setAccessKey("AKLTMDY3MGI0YzI0Zjg0NDdiODkzZDJjNGQ1ODZhZDMzZGE");
            translateService.setSecretKey("TXpnd056aGtOV0V3WmpBd05EWmtZV0ptTXpSa1pUQTJNRFUxT1RRNVpqUQ==");
        }

        public String translate(String text, String sourceLang, String targetLang) {
            try {
                TranslateTextRequest translateTextRequest = new TranslateTextRequest();
                // 不设置表示自动检测
                translateTextRequest.setSourceLanguage(sourceLang);
                translateTextRequest.setTargetLanguage(targetLang);
                translateTextRequest.setTextList(Collections.singletonList(text));
                TranslateTextResponse translateText = translateService.translateText(translateTextRequest);
//                System.out.println(translateText);
                String result = translateText.getTranslationList().get(0).getTranslation();
                return result;
            } catch (Exception e) {
                return "";
//                LOGGER.error("translate error, text: {}, sourceLang: {}, targetLang: {}", text, sourceLang, targetLang);
            }
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
//        car_series();

        ch.qos.logback.classic.Logger wireLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.apache.http.wire");
        wireLogger.setLevel(Level.OFF); // 禁用日志输出

        ch.qos.logback.classic.Logger headersLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.apache.http.headers");
        headersLogger.setLevel(Level.OFF); // 禁用日志输出
        ch.qos.logback.classic.Logger headersimpl = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.apache.http.impl");
        headersimpl.setLevel(Level.OFF); // 禁用日志输出
        ch.qos.logback.classic.Logger headersclient = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.apache.http.client");
        headersclient.setLevel(Level.OFF); // 禁用日志输出

//        BufferedReader br = new BufferedReader(new FileReader(new File("D:/carbarn/翻译/car_type_zh.csv")));
//        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/carbarn/翻译/car_type-en.csv")));

//        String line = null;
        Translator1 translator = new Translator1();
        System.out.println(translator.translate("新车", "zh", "ru"));
//        while((line = br.readLine()) != null){
//            String[] infos = line.split(",");
//            String brand = infos[0];
//            String id = infos[1];
//            String value = translator.translate(brand,"zh","en");
//            System.out.println(value + "\t" + id);
//            bw.write(value + "\t" + id + "\n");
//
//        }
//        bw.flush();
//        bw.close();




//        Translator1 translator = new Translator1();
//        String result = translator.translate("2011款 620 1.5 雪地版精英型(6.18万)","zh","en");
//        System.out.println(result);
    }
}
