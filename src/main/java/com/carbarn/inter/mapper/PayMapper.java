package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.pay.PayCallBackPOJO;
import com.carbarn.inter.pojo.pay.PreOrderPojo;

public interface PayMapper {
    void insertNewPreOrder(PreOrderPojo preOrderPojo);

    void updaterespMsg(PayCallBackPOJO payCallBackDTO);
}
