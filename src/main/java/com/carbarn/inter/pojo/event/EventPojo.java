package com.carbarn.inter.pojo.event;

import lombok.Data;

@Data
public class EventPojo {
    private long seller_id = -1;
    private long buyer_id = -1;
    private int event_type;
}
