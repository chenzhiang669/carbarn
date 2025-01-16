package com.carbarn.im.controller;

import com.carbarn.common.pojo.CommonResult;
import com.carbarn.im.translator.Language;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zoulingxi
 * @description 翻译接口
 * @date 2025/1/16 23:03
 */
@RestController
@RequestMapping("/carbarn/translation")
public class TranslateController {


    @GetMapping("/languages")
    public CommonResult<List<Language>> getLangs() {
        return CommonResult.success(Language.getLanguages());
    }
}