package com.carbarn.inter.pojo.pay;

import lombok.Data;

@Data
public class CreateOrderDTO {
    private int user_id = -1;

    private String pay_type;

    private String order_type;
}
