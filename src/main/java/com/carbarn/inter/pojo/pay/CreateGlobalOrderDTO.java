package com.carbarn.inter.pojo.pay;

import lombok.Data;

@Data
public class CreateGlobalOrderDTO {
    private int user_id = -1;

    private String order_type;

    private String contract_id;
}
