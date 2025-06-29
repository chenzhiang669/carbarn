package com.carbarn.inter.service.impl;

import com.carbarn.inter.mapper.SeaFreightMapper;
import com.carbarn.inter.pojo.seafreight.PortPOJO;
import com.carbarn.inter.pojo.seafreight.SeaFreightPricePOJO;
import com.carbarn.inter.pojo.seafreight.SeaFreightPriceSearchDTO;
import com.carbarn.inter.pojo.seafreight.ShippingLinePOJO;
import com.carbarn.inter.service.SeaFreightService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeaFreightServiceImpl implements SeaFreightService {

    @Autowired
    private SeaFreightMapper seaFreightMapper;

    @Override
    public AjaxResult getPortInfos(String keywords) {
        if(keywords != null){
            keywords = keywords.toLowerCase();
        }
        List<PortPOJO> portPOJOS = seaFreightMapper.getPortInfos(keywords);
        return AjaxResult.success("get ports successful", portPOJOS);
    }

    @Override
    public AjaxResult search(SeaFreightPriceSearchDTO seaFreightPriceSearchDTO) {
        List<ShippingLinePOJO> shippingLinePOJOS = seaFreightMapper.getShippingLine();
        Map<Integer, ShippingLinePOJO> map = new HashMap<Integer, ShippingLinePOJO>();
        for(ShippingLinePOJO shippingLinePOJO : shippingLinePOJOS){
            map.put(shippingLinePOJO.getId(), shippingLinePOJO);
        }

        long total = Utils.getRandomLong(5l,20l);
        List<SeaFreightPricePOJO> seaFreightPricePOJOS = new ArrayList<SeaFreightPricePOJO>();
        for(long i = 1; i <= total; i++){
            SeaFreightPricePOJO seaFreightPricePOJO = new SeaFreightPricePOJO();
            seaFreightPricePOJO.setPortDestination(seaFreightPriceSearchDTO.getPortDestination());
            seaFreightPricePOJO.setPortLoading(seaFreightPriceSearchDTO.getPortLoading());
            double d20gp = Utils.getRandomLong(100L, 200L);
            double d40gp = Utils.getRandomLong(200L, 300L);
            double total_price = calcTotalPrice(d20gp, d40gp, seaFreightPriceSearchDTO.getCnt());
            int total_days = (int) Utils.getRandomLong(20L, 35L);
            seaFreightPricePOJO.setD20gp(d20gp);
            seaFreightPricePOJO.setD40gp(d40gp);
            seaFreightPricePOJO.setTotal_price(total_price);
            seaFreightPricePOJO.setTotal_days(total_days);
            int shippinglineId = (int) Utils.getRandomLong(2L, 58L);
            ShippingLinePOJO shippingLinePOJO = map.get(shippinglineId);
            seaFreightPricePOJO.setCarrier(shippingLinePOJO.getName());

            seaFreightPricePOJOS.add(seaFreightPricePOJO);
        }

        AjaxResult result = AjaxResult.success("search sea freight successful", seaFreightPricePOJOS);
        return result;
    }

    private double calcTotalPrice(double d20gp, double d40gp, int cnt) {
        int a = cnt / 4;
        int mod = cnt % 4;

        if(mod == 0){
            return (d40gp + 2400) * a;
        }else if(mod == 1){
            return (d40gp + 2400) * a  + (d20gp + 600);
        }else if(mod == 2){
            return (d40gp + 2400) * a  + (d40gp + 1200);
        }else if(mod == 3){
            return (d40gp + 2400) * a  + (d40gp + 1800);
        }else{
            return 0;
        }
    }
}