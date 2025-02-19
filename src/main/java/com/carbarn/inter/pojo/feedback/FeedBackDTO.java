package com.carbarn.inter.pojo.feedback;

import lombok.Data;

@Data
public class FeedBackDTO {
    private int user_id = -1;

    private String description;

    private String url;
}
