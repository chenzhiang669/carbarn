package com.carbarn.inter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.carbarn.im.translator.VolcTranslator;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.AsyncMapper;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.pojo.async.TypeDetailsDTO;
import com.carbarn.inter.service.AsyncService;
import com.carbarn.inter.translate.Translate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncServiceImpl implements AsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

    @Autowired
    private AsyncMapper asyncMapper;

    @Autowired
    private ParamsMapper paramsMapper;

    @Autowired
    private VolcTranslator volcTranslator;

    @Async
    @Override
    public void typeDetailsRealTimeTranslate(int type_id) {
        try{

            long startTime = System.currentTimeMillis();
            TypeDetailsDTO typeDetailsDTO =  asyncMapper.getTypeDetails(type_id, "zh");
            String zh_details = typeDetailsDTO.getDetails();
            if(zh_details == null || "".equals(zh_details)){
                logger.warn("the zh type details is null of type_id:{}", type_id);
                return;
            }

            String translates_languages = paramsMapper.getValue(ParamKeys.async_translate_language);
            JSONArray languages = JSON.parseArray(translates_languages);
            if(languages.size() <= 0){
                return;
            }

            for(int i = 0; i < languages.size();i++){
                String language = languages.getString(i);
                TypeDetailsDTO waiting_translate_typeDetailsDTO = asyncMapper.getTypeDetails(type_id, language);
                if(waiting_translate_typeDetailsDTO == null){
                    continue;
                }
                String details = waiting_translate_typeDetailsDTO.getDetails();
                if(details != null && !"".equals(details)){
                    logger.info("the {} type details is not null of type_id:{}, don't need to translate",language, type_id);
                    continue;
                }

                String translate_details = Translate.translateTypeDetails(volcTranslator, zh_details, "zh", language);
                if(translate_details == null || "".equals(translate_details)){
                    continue;
                }
                logger.info("type_id:{} , language:{}, type translate details: {}", type_id, language, translate_details);

                asyncMapper.updateTypeDetails(type_id, language, translate_details);
            }

            long endTime = System.currentTimeMillis();
            long cost_time = (endTime - startTime) / 1000;
            logger.info("translate type_id:{} type details cost {} seconds", type_id, cost_time);

        }catch (Exception e){
            e.printStackTrace();
            logger.error("something wrong when translate typeDetails of type_id:{}", type_id);
        }
    }
}