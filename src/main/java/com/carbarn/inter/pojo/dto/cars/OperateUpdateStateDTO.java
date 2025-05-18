package com.carbarn.inter.pojo.dto.cars;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OperateUpdateStateDTO {
    private int carid;
    private int state = -1;
    private String reason;
}
