package com.carbarn.inter.service.impl;

import com.carbarn.contract.mapper.ContractMapper;
import com.carbarn.inter.mapper.AreaMapper;
import com.carbarn.inter.mapper.EventMapper;
import com.carbarn.inter.mapper.UserMapper;
import com.carbarn.inter.pojo.area.AreaPOJO;
import com.carbarn.inter.pojo.dto.cars.CarNameDTO;
import com.carbarn.inter.pojo.firstpage.FirstPageContractDealDTO;
import com.carbarn.inter.pojo.firstpage.FirstPageCountInfoDTO;
import com.carbarn.inter.pojo.firstpage.FirstPageHotCarsDTO;
import com.carbarn.inter.pojo.firstpage.FirstPageHotCarsSearchDTO;
import com.carbarn.inter.pojo.user.pojo.UserViewCountPojo;
import com.carbarn.inter.service.AreaService;
import com.carbarn.inter.service.CarsService;
import com.carbarn.inter.service.FirstPageService;
import com.carbarn.inter.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

@Service
public class FirstPageServiceImpl implements FirstPageService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private CarsService carsService;

    @Override
    public AjaxResult countInfo(long user_id) {
        FirstPageCountInfoDTO firstPageCountInfoDTO = new FirstPageCountInfoDTO();
        UserViewCountPojo userViewCountPojo = userMapper.userViewCount(user_id);
        if(userViewCountPojo == null){
            firstPageCountInfoDTO.setPv(0);
        }else {
            firstPageCountInfoDTO.setPv(userViewCountPojo.getViewed_count());
        }

        double income = contractMapper.getIncomeBySellerId(user_id);
        int orders = contractMapper.getOrderNumBySellerId(user_id);

        firstPageCountInfoDTO.setIncome(income);
        firstPageCountInfoDTO.setOrders(orders);


        return AjaxResult.success("get user countInfo successful", firstPageCountInfoDTO);
    }

    @Override
    public AjaxResult hotCars(FirstPageHotCarsSearchDTO firstPageHotCarsSearchDTO) {
        if(firstPageHotCarsSearchDTO.getDays() == -1){
            return AjaxResult.error("Missing required parameter 'days'");
        }

        long days = firstPageHotCarsSearchDTO.getDays();
        String language = firstPageHotCarsSearchDTO.getLanguage();
        String from_date = LocalDate.now().minus(days, ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<FirstPageHotCarsDTO> firstPageHotCarsDTOS = eventMapper.getHotCars(language, from_date);
        for (FirstPageHotCarsDTO firstPageHotCarsDTO : firstPageHotCarsDTOS){
            CarNameDTO carNameDTO = new CarNameDTO();
            carNameDTO.setBrand(firstPageHotCarsDTO.getBrand());
            carNameDTO.setSeries(firstPageHotCarsDTO.getSeries());
            carNameDTO.setType(firstPageHotCarsDTO.getType());
            String name = carsService.getCarName(carNameDTO, language);
            firstPageHotCarsDTO.setName(name);
        }
        return AjaxResult.success("get hotcars successful", firstPageHotCarsDTOS);
    }

    @Override
    public AjaxResult contractDeal(String language) {
        List<FirstPageContractDealDTO> firstPageContractDealDTOS = contractMapper.contractDeal(language);
        for (FirstPageContractDealDTO firstPageContractDealDTO : firstPageContractDealDTOS){
            CarNameDTO carNameDTO = new CarNameDTO();
            carNameDTO.setBrand(firstPageContractDealDTO.getBrand());
            carNameDTO.setSeries(firstPageContractDealDTO.getSeries());
            carNameDTO.setType(firstPageContractDealDTO.getType());
            String name = carsService.getCarName(carNameDTO, language);
            firstPageContractDealDTO.setName(name);
        }
        return AjaxResult.success("get contractDeal success", firstPageContractDealDTOS);
    }
}