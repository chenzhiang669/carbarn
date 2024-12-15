package com.carbarn.inter.pojo.dto.cars;

import lombok.Data;

@Data
public class CarsOfUsersDTO {
    private long user_id;

    private String user_name;

    private long carsnum_of_users;

    private long deal_carsnum_of_users;
}
