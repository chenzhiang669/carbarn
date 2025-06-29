package com.carbarn.inter.service;


import org.springframework.scheduling.annotation.Async;

public interface AsyncService {

    void typeDetailsRealTimeTranslate(int type_id);

    void translationDescription(long link_id,
                                String link_table,
                                String link_field,
                                String source_language,
                                String source_value);
}
