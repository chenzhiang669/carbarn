package com.carbarn.inter.pojo.seafreight;

import lombok.Data;

@Data
public class SeaFreightPriceSearchDTO {
    private int size;
    private int page;
    private int cnt;
    private String portDestination;
    private String portLoading;
    private String order_field;
    private String order_type;
    private String portTransshipment;
    private String portTransshipmentStatus;
    private String carrier;
    private String country;
    private String effectiveDateS;
    private String effectiveDateE;
    private String effectiveEndDateS;
    private String effectiveEndDateE;
    private String flight;
    private int isDg;
    private String line;
    private String lineCode;
}
