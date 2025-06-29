package com.carbarn.inter.pojo.user.dto;

import lombok.Data;

@Data
public class TransferSubUsersDTO {
    private String original_phone_num;
    private String original_area_code;
    private long original_sub_user_id;
    private String target_phone_num;
    private String target_area_code;
    private String veri_code;
}
