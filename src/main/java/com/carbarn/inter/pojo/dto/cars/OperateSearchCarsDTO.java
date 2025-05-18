package com.carbarn.inter.pojo.dto.cars;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OperateSearchCarsDTO {
    private String language = null;
    private int pageNo = -1;
    private int pageSize = 10;

    private int pageStart = (pageNo - 1) * pageSize;

    private List<Integer> states = new ArrayList<Integer>(); //颜色
}
