package com.carbarn.im.pojo.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BasePageResp<T> {
    private List<T> records;
    private long total;
    private long size;
    private long current;
}
