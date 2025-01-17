package com.carbarn.inter.controller;

import com.carbarn.inter.pojo.pay.PayCallBackPOJO;
import com.carbarn.inter.service.PayService;
import com.carbarn.inter.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carbarn/pay")
public class PayController {

    @Autowired
    private PayService payService;

    // 登录接口
    @PostMapping("/preorder")
    public AjaxResult preorder() {
        return payService.preorder();
    }


    // 登录接口
    @PostMapping("/callback")
    public String callback(@RequestBody PayCallBackPOJO payCallBackDTO) {
        return payService.callback(payCallBackDTO);
    }


}
