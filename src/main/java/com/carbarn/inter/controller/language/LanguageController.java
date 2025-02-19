package com.carbarn.inter.controller.language;

import cn.dev33.satoken.stp.StpUtil;
import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.language.LanguageDTO;
import com.carbarn.inter.service.LanguageService;
import com.carbarn.inter.service.impl.LanguageServiceImpl;
import com.carbarn.inter.service.impl.UserServiceImpl;
import com.carbarn.inter.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/carbarn")
public class LanguageController {
    @Autowired
    private LanguageService languageService;

    // 登录接口
    @PostMapping("/language")
    public AjaxResult language() {
        List<LanguageDTO> languageDto = languageService.getLanguage();
        return AjaxResult.success("获取语言成功",languageDto);
    }
}
