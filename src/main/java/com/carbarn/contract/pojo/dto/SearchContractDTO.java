package com.carbarn.contract.pojo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class SearchContractDTO {

    private String language = null;
    private int pageNo = -1;
    private int pageSize = 10;

    private int pageStart = (pageNo - 1) * pageSize;

    private long buyer_id;

    private long seller_id;

    private List<Integer> buyer_states = new ArrayList<Integer>();

    private List<Integer> seller_states = new ArrayList<Integer>();


}
