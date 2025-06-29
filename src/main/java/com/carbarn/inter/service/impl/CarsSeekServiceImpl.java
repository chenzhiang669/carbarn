package com.carbarn.inter.service.impl;

import com.carbarn.inter.mapper.CarsSeekMapper;
import com.carbarn.inter.mapper.IndexMapper;
import com.carbarn.inter.pojo.carseek.CarsSeekPOJO;
import com.carbarn.inter.pojo.carseek.ListUserCarsSeekSearchDTO;
import com.carbarn.inter.pojo.carseek.SearchCarsSeekDTO;
import com.carbarn.inter.pojo.carseek.UserCarsSeekDTO;
import com.carbarn.inter.pojo.dto.cars.CarNameDTO;
import com.carbarn.inter.pojo.dto.cars.index.IndexDTO;
import com.carbarn.inter.service.AsyncService;
import com.carbarn.inter.service.CarsSeekService;
import com.carbarn.inter.service.CarsService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarsSeekServiceImpl implements CarsSeekService {

    @Autowired
    private CarsSeekMapper carsSeekMapper;

    @Autowired
    private IndexMapper indexMapper;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private CarsService carsService;

    @Override
    public void insertNewCarSeek(String language, CarsSeekPOJO carsSeekPOJO) {
        String randomString = getRandomString();
        carsSeekPOJO.setRandomString(randomString);
        carsSeekMapper.insertNewCarSeek(carsSeekPOJO);


        long id = carsSeekMapper.getCarSeekidByRandomString(randomString);
        String link_table = "cars_seek";
        String link_field = "description";
        String source_value = carsSeekPOJO.getDescription();
        asyncService.translationDescription(id, link_table, link_field, language, source_value);
    }

    @Override
    public void updateCarSeekById(String language, CarsSeekPOJO carsSeekPOJO) {
        carsSeekMapper.updateCarSeekById(carsSeekPOJO);

        long link_id = carsSeekPOJO.getId();
        String link_table = "cars_seek";
        String link_field = "description";
        String source_value = carsSeekPOJO.getDescription();
        asyncService.translationDescription(link_id, link_table, link_field, language, source_value);
    }

    @Override
    public AjaxResult listUserCarSeek(ListUserCarsSeekSearchDTO listUserCarsSeekSearchDTO) {
        LocalDate today_ = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = today_.format(formatter);
        listUserCarsSeekSearchDTO.setToday(today);

        List<UserCarsSeekDTO> userCarsSeekDTOS = carsSeekMapper.listUserCarSeek(listUserCarsSeekSearchDTO);
        transform(listUserCarsSeekSearchDTO.getLanguage(), userCarsSeekDTOS);
        return AjaxResult.success("list user carseek successful", userCarsSeekDTOS);
    }

    @Override
    public AjaxResult listUserCarSeekExpire(ListUserCarsSeekSearchDTO listUserCarsSeekSearchDTO) {
        LocalDate today_ = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = today_.format(formatter);
        listUserCarsSeekSearchDTO.setToday(today);

        List<UserCarsSeekDTO> userCarsSeekDTOS = carsSeekMapper.listUserCarSeekExpire(listUserCarsSeekSearchDTO);
        transform(listUserCarsSeekSearchDTO.getLanguage(), userCarsSeekDTOS);
        return AjaxResult.success("list user carseek successful", userCarsSeekDTOS);
    }

    @Override
    public AjaxResult listUserCarSeekUnExpire(ListUserCarsSeekSearchDTO listUserCarsSeekSearchDTO) {
        LocalDate today_ = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = today_.format(formatter);
        listUserCarsSeekSearchDTO.setToday(today);

        List<UserCarsSeekDTO> userCarsSeekDTOS = carsSeekMapper.listUserCarSeekUnExpire(listUserCarsSeekSearchDTO);
        transform(listUserCarsSeekSearchDTO.getLanguage(), userCarsSeekDTOS);
        return AjaxResult.success("list user carseek successful", userCarsSeekDTOS);
    }

    @Override
    public AjaxResult getCarSeekById(String language, CarsSeekPOJO carsSeekPOJO) {
        if(carsSeekPOJO.getId() == 0){
            return AjaxResult.error("Missing required parameter: id");
        }

        UserCarsSeekDTO userCarsSeekDTO = carsSeekMapper.getCarSeekById(language, carsSeekPOJO.getId());
        if(userCarsSeekDTO == null){
            return AjaxResult.error("there is no such carseek of id: " + carsSeekPOJO.getId());
        }

        transform(language, userCarsSeekDTO);
        return AjaxResult.success("getCarSeekById success", userCarsSeekDTO);
    }

    @Override
    public AjaxResult searchCarSeek(SearchCarsSeekDTO searchCarsSeekDTO) {

        LocalDate today_ = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = today_.format(formatter);
        searchCarsSeekDTO.setToday(today);
        List<UserCarsSeekDTO> userCarsSeekDTOS = carsSeekMapper.searchCarSeek(searchCarsSeekDTO);
        transform(searchCarsSeekDTO.getLanguage(), userCarsSeekDTOS);
        return AjaxResult.success("search carseek successful", userCarsSeekDTOS);
    }

    @Override
    public AjaxResult deleteCarSeek(CarsSeekPOJO carsSeekPOJO) {
        carsSeekMapper.deleteCarSeek(carsSeekPOJO.getId());
        return AjaxResult.success("deleteCarSeek successful");
    }

    private void transform(String language,
                           List<UserCarsSeekDTO> userCarsSeekDTOS) {
        List<IndexDTO> indexes = indexMapper.getIndex(language);
        Map<String, IndexDTO> id_mapping = new HashMap<String, IndexDTO>();

        for (IndexDTO indexDTO : indexes) {
            int is_mapping = indexDTO.getIs_mapping();
            int id = indexDTO.getValue_id();
            String field = indexDTO.getField();

            if (is_mapping == 0) {
                id_mapping.put(field + id, indexDTO);
            }
        }

        for (UserCarsSeekDTO userCarsSeekDTO : userCarsSeekDTOS) {

            CarNameDTO carNameDTO = new CarNameDTO();
            carNameDTO.setBrand(userCarsSeekDTO.getBrand());
            carNameDTO.setSeries(userCarsSeekDTO.getSeries());
            carNameDTO.setType(userCarsSeekDTO.getType());
            String title = carsService.getCarName(carNameDTO, null);
            userCarsSeekDTO.setTitle(title);

            int color = userCarsSeekDTO.getColor();
            String key = "color" + color;
            if (id_mapping.containsKey(key)) {
                String color_name = id_mapping.get(key).getValue();
                userCarsSeekDTO.setColor_name(color_name);
            }
        }
    }

    private void transform(String language,
                           UserCarsSeekDTO userCarsSeekDTO) {
        List<IndexDTO> indexes = indexMapper.getIndex(language);
        Map<String, IndexDTO> id_mapping = new HashMap<String, IndexDTO>();

        for (IndexDTO indexDTO : indexes) {
            int is_mapping = indexDTO.getIs_mapping();
            int id = indexDTO.getValue_id();
            String field = indexDTO.getField();

            if (is_mapping == 0) {
                id_mapping.put(field + id, indexDTO);
            }
        }


        CarNameDTO carNameDTO = new CarNameDTO();
        carNameDTO.setBrand(userCarsSeekDTO.getBrand());
        carNameDTO.setSeries(userCarsSeekDTO.getSeries());
        carNameDTO.setType(userCarsSeekDTO.getType());
        String title = carsService.getCarName(carNameDTO, null);
        userCarsSeekDTO.setTitle(title);

        int color = userCarsSeekDTO.getColor();
        String key = "color" + color;
        if (id_mapping.containsKey(key)) {
            String color_name = id_mapping.get(key).getValue();
            userCarsSeekDTO.setColor_name(color_name);
        }
    }

    public String getRandomString() {
        LocalDateTime localDateTime = LocalDateTime.now();

        String time = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomChar = Utils.getRandomChar(10);
        return time + randomChar;
    }
}
