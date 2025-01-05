package com.carbarn.inter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.carbarn.inter.config.CarbarnConfig;
import com.carbarn.inter.mapper.CarsMapper;
import com.carbarn.inter.mapper.IndexMapper;
import com.carbarn.inter.pojo.CarTypePOJO;
import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.dto.cars.CarsOfUsersDTO;
import com.carbarn.inter.pojo.dto.cars.FirstPageCarsDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import com.carbarn.inter.pojo.dto.cars.index.IndexDTO;
import com.carbarn.inter.pojo.dto.cars.index.TypeMessageDTO;
import com.carbarn.inter.service.CarsService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.Utils;
import com.carbarn.inter.utils.http.JinyutangHttp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CarsServiceImpl implements CarsService {

    @Autowired
    private CarsMapper carsMapper;
    @Autowired
    private IndexMapper indexMapper;

    @Autowired
    private CarbarnConfig carbarnConfig;

    @Override
    public List<FirstPageCarsDTO> getCars() {
        return carsMapper.getCars();
    }

    @Override
    public List<FirstPageCarsDTO> searchCars(SearchCarsDTO searchCarsDTO) {
        return carsMapper.searchCars(searchCarsDTO);
    }

    @Override
    public Map<String, Object> getCarsByID(String language, long carid) {




        CarsPOJO carsPOJO =  carsMapper.getCarsByID(carid);
        if(carsPOJO == null){
            return new HashMap<String, Object>();
        }

        String carsPOJOJsonString = JSON.toJSONString(carsPOJO, SerializerFeature.WriteMapNullValue);
        JSONObject jsonObject = JSON.parseObject(carsPOJOJsonString);
        Map<String, Object> map = jsonObject;

        try{
            int type_id = carsPOJO.getType_id();
            TypeMessageDTO typeMessageDTO = indexMapper.getTypeMessage(language, type_id);
            String brand = typeMessageDTO.getBrand();
            String series = typeMessageDTO.getSeries();
            String type = typeMessageDTO.getType();
            int brand_id = typeMessageDTO.getBrand_id();
            int series_id = typeMessageDTO.getSeries_id();
            if(brand != null && series != null && type != null){
                String name = brand + " " + series + " " + type;
                map.put("brand", brand);
                map.put("brand_id", brand_id);
                map.put("series", series);
                map.put("series_id", series_id);
                map.put("type",type);
                map.put("name", name);
            }else{
                map.clear();
                return map;
            }


            List<IndexDTO> indexes = indexMapper.getIndex(language);
            Map<Integer, IndexDTO> id_mapping = new HashMap<Integer, IndexDTO>();
            Map<String, IndexDTO> field_mapping = new HashMap<String, IndexDTO>();

            for(IndexDTO indexDTO:indexes){
                int is_mapping = indexDTO.getIs_mapping();
                int id = indexDTO.getValue_id();
                String field = indexDTO.getField();

                if(is_mapping == 0){
                    id_mapping.put(id, indexDTO);
                    field_mapping.put(field, indexDTO);
                }
            }

            Class<?> clazz = carsPOJO.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for(Field field:fields){
                field.setAccessible(true);
                String key = field.getName();
                Object real_id = field.get(carsPOJO);

                if(field_mapping.containsKey(key)){

                    if(id_mapping.containsKey(real_id) && key.equals(id_mapping.get(real_id).getField())){
                        String real_name = id_mapping.get(real_id).getValue();
                        map.put(key + "_name", real_name);
                    }else{
                        map.put(key + "_name", null);
                    }

                }
            }



        }catch(Exception e){
            map.clear();
        }


        return map;
    }

    @Override
    public CarsOfUsersDTO getCarsOfUsers(long userid) {
        return carsMapper.getCarsOfUsers(userid);
    }

    @Override
    public Map<String, Object> getByVin(String vin) {
        Map<String, Object> map = new HashMap<String, Object>();
        Random random = new Random();
        boolean is_vin_exist = carsMapper.existsByVin(vin); // 生成0或1

        if (is_vin_exist) {
            map.put("0", true);
            return map;
        }

        List<JSONObject> vin_details = JinyutangHttp.searchVin(vin); //TODO
        if (vin_details == null || vin_details.size() == 0) {
            map.put("1", true);
            return map;
        }

        for(JSONObject message:vin_details){
            String brand = message.getString("brand");
            String series = message.getString("series");
            String brand_first_char = Utils.getFirstLetter(brand);
            String series_first_char = Utils.getFirstLetter(series);
            String type = message.getString("type");
            String language = message.getOrDefault("language","zh").toString();

            String brand_id = indexMapper.getBrandIdByBrand(language, brand);
            if (brand_id == null) {
                System.out.println(String.format("数据库中没有该品牌：%s，插入新品牌", brand));
                brand_id = insertNewBrand("https://www.cctv.com", brand, brand_first_char, language);
            }

            int brandid = Integer.valueOf(brand_id);
            message.put("brand_id",brandid);
            Map<String, Object> brand_map = new HashMap<String, Object>();
            brand_map.put("name", brand);
            brand_map.put("brand_id", brandid);
            map.put("brand", brand_map);


            String series_id = indexMapper.getSeriesIdBySeries(language, series);
            if (series_id == null) {
                System.out.println(String.format("数据库中没有该车系：%s，插入新车系", series));
                series_id = insertNewSeries(brandid, series_first_char, language, series);
            }
            int seriesid = Integer.valueOf(series_id);
            message.put("series_id", seriesid);
            Map<String, Object> series_map = new HashMap<String, Object>();
            series_map.put("name", series);
            series_map.put("series_id", seriesid);
            map.put("series", series_map);

            String type_id = indexMapper.getTypeIdByType(language, type);
            if (type_id == null) {
                System.out.println(String.format("数据库中没有该车型: %s，插入新车型", type));
                CarTypePOJO carTypePOJO = createNewCarTypePOJO(message);
                type_id = insertNewtype(carTypePOJO);
            }

            int typeid = Integer.valueOf(type_id);
            Map<String, Object> type_map = new HashMap<String, Object>();
            type_map.put("name", type);
            type_map.put("type_id", typeid);
            map.put("type", type_map);
        }


        Map<String, Object> manufacture_date_map = new HashMap<String, Object>();
        String manufacture_date = Utils.getYearFromVin(vin);
        manufacture_date_map.put("name", manufacture_date);
        map.put("manufacture_date", manufacture_date_map);
        return map;
    }

    private CarTypePOJO createNewCarTypePOJO(JSONObject message) {
        String language = message.getOrDefault("language", "zh").toString();
        List<IndexDTO> indexes = indexMapper.getIndex(language);

        Map<String, Integer> value_id_map = new HashMap<String, Integer>();
        Set<String> mapping_fields = new HashSet<String>();
        for(IndexDTO indexDTO:indexes){
            int is_mapping = indexDTO.getIs_mapping();
            String value = indexDTO.getValue();
            int value_id = indexDTO.getValue_id();
            String field = indexDTO.getField();
            if(is_mapping == 0){
                value_id_map.put(value, value_id);
                mapping_fields.add(field);
            }
        }

        for(String key:message.keySet()){
            Object value = message.getOrDefault(key, null);
            if(mapping_fields.contains(key)){
                if(value_id_map.containsKey(value)){
                    message.put(key, value_id_map.get(value));
                }else{
                    message.put(key, -1);
                }
            }
        }


        CarTypePOJO carTypePOJO = message.toJavaObject(CarTypePOJO.class);
        return carTypePOJO;
    }

    @Override
    public Map<String, Object> fillMessage(String language, int type_id) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            CarTypePOJO typePOJO = indexMapper.getTypeDefaultValue(language, type_id);
            if (typePOJO == null) {
                typePOJO = new CarTypePOJO();
            }
            List<IndexDTO> indexes = indexMapper.getIndex(language);
            Map<Integer, IndexDTO> id_mapping = new HashMap<Integer, IndexDTO>();
            Map<String, IndexDTO> field_mapping = new HashMap<String, IndexDTO>();

            for (IndexDTO indexDTO : indexes) {
                int id = indexDTO.getValue_id();
                String field = indexDTO.getField();

                id_mapping.put(id, indexDTO);
                field_mapping.put(field, indexDTO);
            }


            if (field_mapping.containsKey("displacement")
                    && field_mapping.containsKey("displacement_type")) {
                double displacement = typePOJO.getDisplacement();
                String displacement_type = typePOJO.getDisplacement_type();
                String displacement_field_name = field_mapping.get("displacement").getIndex();

                HashMap<String, Object> displacement_inmap = new HashMap<String, Object>();
                displacement_inmap.put("field_name", displacement_field_name);
                displacement_inmap.put("default_value", displacement + displacement_type);
                map.put("displacement", displacement_inmap);
            }

            Class<?> clazz = typePOJO.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String key = field.getName();

                if ("displacement".equals(key) || "displacement_type".equals(key)) {
                    continue;
                }

                if (field_mapping.containsKey(key)) {
                    IndexDTO indexDTO = field_mapping.get(key);
                    int is_mapping = indexDTO.getIs_mapping();
                    if (is_mapping == 0) {
                        int default_value = 0;
                        default_value = field.getInt(typePOJO);
                        if (!id_mapping.containsKey(default_value)) {
                            default_value = indexDTO.getValue_id();
                        }
                        String name = id_mapping.get(default_value).getValue();
                        String field_name = indexDTO.getIndex();
                        HashMap<String, Object> inmap = new HashMap<String, Object>();
                        inmap.put("name", name);
                        inmap.put("field_name", field_name);
                        inmap.put("default_value", default_value);
                        map.put(key, inmap);
                    } else if (is_mapping == 1) {
                        String field_name = indexDTO.getIndex();
                        Object default_value = field.get(typePOJO);
                        HashMap<String, Object> inmap = new HashMap<String, Object>();
                        inmap.put("field_name", field_name);
                        inmap.put("default_value", default_value);
                        map.put(key, inmap);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    @Override
    public AjaxResult insertNewCar(CarsPOJO carsPOJO) {
        boolean bool = carsMapper.existsByVin(carsPOJO.getVin());
        if(bool){
            return AjaxResult.error("车架号所属汽车已经上传过，请不要重复上传");
        }
        carsMapper.insertNewCar(carsPOJO);
        return AjaxResult.success("上传汽车成功");
    }




    public synchronized String insertNewBrand(String logo,
                                              String brand,
                                              String first_char,
                                              String language) {

        String brand_id = indexMapper.getBrandIdByBrand(language, brand);
        if (brand_id == null) {
            indexMapper.insertNewBrand(logo, brand, first_char, language);
            brand_id = indexMapper.getBrandIdByBrand(language, brand);
        }

        return brand_id;
    }


    public synchronized String insertNewSeries(int brand_id,
                                               String first_char,
                                               String language,
                                               String series) {

        String series_id = indexMapper.getSeriesIdBySeries(language, series);
        if (series_id == null) {
            indexMapper.insertNewSeries(brand_id, first_char, language, series);
            series_id = indexMapper.getSeriesIdBySeries(language, series);
        }

        return series_id;
    }

    private synchronized String insertNewtype(CarTypePOJO carTypePOJO) {
        String type = carTypePOJO.getType();
        String language = carTypePOJO.getLanguage();
        String type_id = indexMapper.getTypeIdByType(language, type);
        if (type_id == null) {
            indexMapper.insertNewType(carTypePOJO);
            type_id = indexMapper.getTypeIdByType(language, type);
        }

        return type_id;
    }

}
