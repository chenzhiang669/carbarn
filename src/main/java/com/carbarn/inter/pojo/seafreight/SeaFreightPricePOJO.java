package com.carbarn.inter.pojo.seafreight;

import lombok.Data;

@Data
public class SeaFreightPricePOJO {
    private int id;
    private String portLoading;
    private String dockLoading;
    private String portDestination;
    private String dockDestination;
    private String portTransshipment;
    private String carrier;
    private String line;
    private String lineCode;
    private String voyage;
    private String week;
    private String sailingDate;
    private String customsClosingDate;
    private String flight;
    private String billType;
    private double d20gp;
    private double d40gp;
    private double d40hq;
    private double d45hq;
    private double d40nor;
    private String shippingSpace;
    private double total_price;
    private int total_days;
}
