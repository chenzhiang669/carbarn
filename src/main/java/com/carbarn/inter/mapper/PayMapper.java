package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.pay.OrderPOJO;
import com.carbarn.inter.pojo.pay.PayCallBackPOJO;
import org.apache.ibatis.annotations.Param;

public interface PayMapper {
//    void insertNewPreOrder(PreOrderPojo preOrderPojo);

    void insertNewOrder(OrderPOJO orderPojo);

//    void updaterespMsg(PayCallBackPOJO payCallBackDTO);

    void insertPayCallback(PayCallBackPOJO payCallBackDTO);


    OrderPOJO getDefaultOrderInfo();

    String getUserIdByReqsn(@Param("outtrxid") String outtrxid);

    OrderPOJO getOrderInfoByReqsn(@Param("reqsn") String reqsn);
}
