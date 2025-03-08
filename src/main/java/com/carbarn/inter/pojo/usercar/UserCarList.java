package com.carbarn.inter.pojo.usercar;

import lombok.Data;

import java.util.List;

@Data
public class UserCarList {
    private String key;
    private int count;
    private List<UserCarPOJO> cars;
}
