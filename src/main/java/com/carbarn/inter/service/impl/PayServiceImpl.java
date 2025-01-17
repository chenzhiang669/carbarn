package com.carbarn.inter.service.impl;

import com.carbarn.inter.mapper.PayMapper;
import com.carbarn.inter.pojo.pay.PayCallBackPOJO;
import com.carbarn.inter.pojo.pay.PreOrderPojo;
import com.carbarn.inter.service.PayService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.hzyh.HangZhouYinHangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private PayMapper payMapper;

    @Override
    public AjaxResult preorder() {
        PreOrderPojo preOrderPojo = HangZhouYinHangUtils.http();
        if(preOrderPojo == null){
            return AjaxResult.error("构建预订单失败");
        }
        payMapper.insertNewPreOrder(preOrderPojo);

        Map<String, String> result = new HashMap<String, String>();
        result.put("tokenCode", preOrderPojo.getTokenCode());
        result.put("txnAmt", preOrderPojo.getTxnAmt());
        result.put("tokenCode", preOrderPojo.getTokenCode());

        return AjaxResult.success("构建预订单成功", result);

    }

    @Override
    public String callback(PayCallBackPOJO payCallBackDTO) {

        try{
            if("0000".equals(payCallBackDTO.getRespCode())){
                payMapper.updaterespMsg(payCallBackDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Fail";
        }


        return "Success";
    }
}
