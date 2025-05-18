package com.carbarn.inter.service.impl;

import com.carbarn.inter.service.AsyncService;
import com.carbarn.inter.service.TestService;
import com.carbarn.inter.utils.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

    @Autowired
    private AsyncService asyncService;


    @Override
    public AjaxResult test(int type_id) {
        asyncService.typeDetailsRealTimeTranslate(type_id);
        return AjaxResult.success("test successful");
    }
}