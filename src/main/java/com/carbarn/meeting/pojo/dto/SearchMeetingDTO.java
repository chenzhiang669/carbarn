package com.carbarn.meeting.pojo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchMeetingDTO {

    private String language = null;
    private int pageNo = -1;
    private int pageSize = 10;

    private int pageStart = (pageNo - 1) * pageSize;

}
