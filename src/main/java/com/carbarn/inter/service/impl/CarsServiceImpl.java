package com.carbarn.inter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.carbarn.inter.config.CarbarnConfig;
import com.carbarn.inter.mapper.CarsMapper;
import com.carbarn.inter.mapper.IndexMapper;
import com.carbarn.inter.mapper.UserMapper;
import com.carbarn.inter.pojo.CarTypePOJO;
import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.dto.cars.*;
import com.carbarn.inter.pojo.dto.cars.index.IndexDTO;
import com.carbarn.inter.pojo.dto.cars.index.TypeMessageDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.pojo.usercar.Constant;
import com.carbarn.inter.pojo.vin.VinPOJO;
import com.carbarn.inter.service.AsyncService;
import com.carbarn.inter.service.CarsService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.Utils;
import com.carbarn.inter.utils.qixiubao.QixiubaoHttp;
import com.carbarn.inter.utils.qixiubao.Transform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarsServiceImpl implements CarsService {

    public static int newcar = 0; //新车(有增值税发票)
    public static int usedcar_with_invoice = 1; //二手车(有增值税发票)
    public static int usedcar_without_invoice = 2; //二手车(没有增值税发票)

    public static int operate_save = 0; //操作: 保存
    public static int operate_deploy = 1; //操作: 发布

    @Autowired
    private CarsMapper carsMapper;
    @Autowired
    private IndexMapper indexMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CarbarnConfig carbarnConfig;

    @Autowired
    private AsyncService asyncService;

    public static HashMap<String, String> multi_values_fields = new HashMap<String, String>();

    static {
        multi_values_fields.put("label_string", "label");
        multi_values_fields.put("car_condition_string", "car_condition");
        multi_values_fields.put("coating_string", "coating");
        multi_values_fields.put("component_string", "component");
        multi_values_fields.put("engine_condition_string", "engine_condition");
        multi_values_fields.put("transmission_condition_string", "transmission_condition");
        multi_values_fields.put("number_of_transfers_string", "number_of_transfers");
        multi_values_fields.put("mileage_contition_string", "mileage_contition");
    }

    @Override
    public List<FirstPageCarsDTO> getCars() {
        return carsMapper.getCars();
    }

    @Override
    public List<FirstPageCarsDTO> searchCars(SearchCarsDTO searchCarsDTO) {
        List<FirstPageCarsDTO> firstPageCarsDTOS = carsMapper.searchCars(searchCarsDTO);
        for (FirstPageCarsDTO firstPageCarsDTO : firstPageCarsDTOS) {
            firstPageCarsDTO.setName(getCarName(firstPageCarsDTO.getBrand(), firstPageCarsDTO.getSeries(), firstPageCarsDTO.getType()));
        }
        return firstPageCarsDTOS;
    }

    @Override
    public Map<String, Object> getCarsByID(String language, long carid) {

        CarsPOJO carsPOJO = carsMapper.getCarsByID(carid);
        if (carsPOJO == null) {
            return new HashMap<String, Object>();
        }

        UserPojo userPojo = userMapper.getUserInfoByID(carsPOJO.getUser_id());

        String carsPOJOJsonString = JSON.toJSONString(carsPOJO, SerializerFeature.WriteMapNullValue);
        JSONObject jsonObject = JSON.parseObject(carsPOJOJsonString);
        Map<String, Object> map = jsonObject;

        Map<String, String> user_info = new HashMap<String, String>();
        user_info.put("phone_num", userPojo.getPhone_num());
        user_info.put("avatar", userPojo.getAvatar());
        user_info.put("nickname", userPojo.getNickname());
        user_info.put("address", userPojo.getAddress());
        user_info.put("car_dealership", userPojo.getCar_dealership());
        user_info.put("real_name", userPojo.getReal_name());
        map.put("user_info", user_info);

        try {
            int type_id = carsPOJO.getType_id();
            if (type_id == -1) {
                map.put("brand", null);
                map.put("brand_id", -1);
                map.put("series", null);
                map.put("series_id", -1);
                map.put("type", null);
                map.put("name", null);
            } else {
                TypeMessageDTO typeMessageDTO = indexMapper.getTypeMessage(language, type_id);
                String brand = typeMessageDTO.getBrand();
                String series = typeMessageDTO.getSeries();
                String type = typeMessageDTO.getType();
                int brand_id = typeMessageDTO.getBrand_id();
                int series_id = typeMessageDTO.getSeries_id();
                if (brand != null && series != null && type != null) {
//                    String name = brand + " " + series + " " + type;
//                    String name = brand + " " + type;
                    String name = getCarName(brand, series, type);
                    map.put("brand", brand);
                    map.put("brand_id", brand_id);
                    map.put("series", series);
                    map.put("series_id", series_id);
                    map.put("type", type);
                    map.put("name", name);
                } else {
                    map.clear();
                    return map;
                }
            }


            List<IndexDTO> indexes = indexMapper.getIndex(language);
            Map<String, IndexDTO> id_mapping = new HashMap<String, IndexDTO>();
            Map<String, IndexDTO> field_mapping = new HashMap<String, IndexDTO>();

            for (IndexDTO indexDTO : indexes) {
                int is_mapping = indexDTO.getIs_mapping();
                int id = indexDTO.getValue_id();
                String field = indexDTO.getField();

                if (is_mapping == 0) {
                    id_mapping.put(field + id, indexDTO);
                    field_mapping.put(field, indexDTO);
                }
            }

            Class<?> clazz = carsPOJO.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String key = field.getName();

                if (multi_values_fields.containsKey(key)) {
                    String value_name = multi_values_fields.get(key);
                    String[] real_ids = ((String) field.get(carsPOJO)).split(",");
                    List<Integer> ids = Arrays.stream(real_ids).map(x -> {
                        return Integer.valueOf(x);
                    }).filter(x -> {
                        return x != -1;
                    }).collect(Collectors.toList());
                    map.put(value_name, ids);

                } else if (multi_values_fields.values().contains(key)) {
                    continue;
                } else {
                    if (field_mapping.containsKey(key)) {
                        Object real_id = field.get(carsPOJO);

                        if (id_mapping.containsKey(key + real_id)) {
                            String real_name = id_mapping.get(key + real_id).getValue();
                            map.put(key + "_name", real_name);
                        } else {
                            map.put(key + "_name", null);
                        }

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.clear();
        }


        return map;
    }

    public AjaxResult getCarsByID_New(String language, long carid) {

        CarsPOJO carsPOJO = carsMapper.getCarsByID(carid);
        if (carsPOJO == null) {
            return AjaxResult.error("the carid: " + carid + " don't exist");
        }

        CarDetailsDTO carDetailsDTO = new CarDetailsDTO();
        carDetailsDTO.setId(carid);
        carDetailsDTO.setUser_id(carsPOJO.getUser_id());
        carDetailsDTO.setHeader_picture(carsPOJO.getHeader_picture());
        carDetailsDTO.setAll_pictures(carsPOJO.getAll_pictures());
        carDetailsDTO.setPrice(carsPOJO.getPrice());
        carDetailsDTO.setGuide_price(carsPOJO.getGuide_price());
        carDetailsDTO.setFloor_price(carsPOJO.getFloor_price());
        carDetailsDTO.setVin(carsPOJO.getVin());
        carDetailsDTO.setProof(carsPOJO.getProof());
        carDetailsDTO.setVehicleType(carsPOJO.getVehicleType());
        carDetailsDTO.setInspection_report(carsPOJO.getInspection_report());

        UserPojo userPojo = userMapper.getUserInfoByID(carsPOJO.getUser_id());
        CarUserInfoDTO carUserInfoDTO = CarUserInfoDTO.getCarUserInfoDTO(userPojo);
        carDetailsDTO.setUser_info(carUserInfoDTO);  //卖家用户信息

        String carsPOJOJsonString = JSON.toJSONString(carsPOJO, SerializerFeature.WriteMapNullValue);
        JSONObject jsonObject = JSON.parseObject(carsPOJOJsonString);
        Map<String, Object> map = jsonObject;


        try {
            int type_id = carsPOJO.getType_id();
            if (type_id == -1) {
                return AjaxResult.error("the carid: " + carid + " don't exist");
            } else {
                TypeMessageDTO typeMessageDTO = indexMapper.getTypeMessage(language, type_id);
                String brand = typeMessageDTO.getBrand();
                String series = typeMessageDTO.getSeries();
                String type = typeMessageDTO.getType();
                int brand_id = typeMessageDTO.getBrand_id();
                int series_id = typeMessageDTO.getSeries_id();
                if (brand != null && series != null && type != null) {
                    String name = getCarName(brand, series, type);
                    carDetailsDTO.setBrand_id(brand_id);
                    carDetailsDTO.setBrand(brand);
                    carDetailsDTO.setSeries_id(series_id);
                    carDetailsDTO.setSeries(series);
                    carDetailsDTO.setType_id(type_id);
                    carDetailsDTO.setType(type);
                    carDetailsDTO.setName(name);
                } else {
                    return AjaxResult.error("the carid: " + carid + " don't exist");
                }
            }


            List<IndexDTO> indexDTOS = indexMapper.getIndex(language);
            Map<String, IndexDTO> field_id_Index_Map = new HashMap<String, IndexDTO>();
//            Map<String, IndexDTO> field_mapping = new HashMap<String, IndexDTO>();

            for (IndexDTO indexDTO : indexDTOS) {
                int is_mapping = indexDTO.getIs_mapping();
                int id = indexDTO.getValue_id();
                String field = indexDTO.getField();

                if (is_mapping == 0) {
                    field_id_Index_Map.put(field + id, indexDTO);
//                    field_mapping.put(field, indexDTO);
                }
            }

            Class<?> clazz = carsPOJO.getClass();
            Field[] fields = clazz.getDeclaredFields();

            List<CarInfosDTO> car_infos = new ArrayList<CarInfosDTO>();

            for (Field field : fields) {
                field.setAccessible(true);
                String key = field.getName();
                Object real_id = field.get(carsPOJO);
                String field_id = key + real_id;

                if (multi_values_fields.containsKey(key)) {
                    String value_name = multi_values_fields.get(key);
                    String[] real_ids = ((String) field.get(carsPOJO)).split(",");
                    List<Integer> ids = Arrays.stream(real_ids).map(x -> {
                        return Integer.valueOf(x);
                    }).filter(x -> {
                        return x != -1;
                    }).collect(Collectors.toList());
                    map.put(value_name, ids);

                } else if (multi_values_fields.values().contains(key)) {
                    continue;
                } else {
                    if (field_id_Index_Map.containsKey(field_id)) {
                        IndexDTO indexDTO = field_id_Index_Map.get(field_id);
                        CarInfosDTO carInfosDTO = new CarInfosDTO();
                        carInfosDTO.setKey(indexDTO.getIndex());
                        carInfosDTO.setValue(indexDTO.getValue());
                        carInfosDTO.setValue_id(indexDTO.getValue_id());
                        car_infos.add(carInfosDTO);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return AjaxResult.success("get cardetails success", carDetailsDTO);
    }

    @Override
    public CarsOfUsersDTO getCarsOfUsers(long userid) {
        return carsMapper.getCarsOfUsers(userid);
    }

//    @Override
//    public Map<String, Object> getByVin(String vin) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        Random random = new Random();
//        boolean is_vin_exist = carsMapper.existsByVin(vin); // 生成0或1
//
//        if (is_vin_exist) {
//            map.put("0", true);
//            return map;
//        }
//
//        String responseString = null;
//        VinPOJO vinPOJO = carsMapper.getVinInfos(vin); //先从自有vin库中查询是否有这个车架号
//        if (vinPOJO == null) {
//            responseString = QixiubaoHttp.searchVin(vin);
//            if (responseString == null) {
//                map.put("1", true);
//                return map;
//            } else {
//                vinPOJO = new VinPOJO();
//                vinPOJO.setVin(vin);
//                vinPOJO.setInfos(responseString);
//                carsMapper.insertVin(vinPOJO);
//            }
//        } else {
//            responseString = vinPOJO.getInfos();
//        }
//
//        List<JSONObject> vin_details = Qixiubao.parseResponse(responseString);
//
////        List<JSONObject> vin_details = JinyutangHttp.searchVin(vin); //TODO
////        String responseString = Qixiubao.searchVin(vin);
////        List<JSONObject> vin_details = Qixiubao.parseResponse(responseString);
////        if (vin_details == null || vin_details.size() == 0) {
////            map.put("1", true);
////            return map;
////        }
//
//        for (JSONObject message : vin_details) {
//            String brand = message.getString("brand");
//            String series = message.getString("series");
//            String brand_first_char = Utils.getFirstLetter(brand);
//            String series_first_char = Utils.getFirstLetter(series);
//            String type = message.getString("type");
//            String language = message.getOrDefault("language", "zh").toString();
//
//            String brand_id = indexMapper.getBrandIdByBrand(language, brand);
//            if (brand_id == null) {
//                System.out.println(String.format("数据库中没有该品牌：%s，插入新品牌", brand));
//                brand_id = insertNewBrand("https://www.cctv.com", brand, brand_first_char, language);
//            }
//
//            int brandid = Integer.valueOf(brand_id);
//            message.put("brand_id", brandid);
//            Map<String, Object> brand_map = new HashMap<String, Object>();
//            brand_map.put("name", brand);
//            brand_map.put("brand_id", brandid);
//            map.put("brand", brand_map);
//
//
//            String series_id = indexMapper.getSeriesIdBySeries(language, series);
//            if (series_id == null) {
//                System.out.println(String.format("数据库中没有该车系：%s，插入新车系", series));
//                series_id = insertNewSeries(brandid, series_first_char, language, series);
//            }
//            int seriesid = Integer.valueOf(series_id);
//            message.put("series_id", seriesid);
//            Map<String, Object> series_map = new HashMap<String, Object>();
//            series_map.put("name", series);
//            series_map.put("series_id", seriesid);
//            map.put("series", series_map);
//
//            String type_id = indexMapper.getTypeIdByType(language, type);
//            if (type_id == null) {
//                System.out.println(String.format("数据库中没有该车型: %s，插入新车型", type));
//                CarTypePOJO carTypePOJO = createNewCarTypePOJO(message);
//                type_id = insertNewtype(carTypePOJO);
//            }
//
//            int typeid = Integer.valueOf(type_id);
//            Map<String, Object> type_map = new HashMap<String, Object>();
//            type_map.put("name", type);
//            type_map.put("type_id", typeid);
//            map.put("type", type_map);
//            map.put("name", getCarName(brand, series, type));
//
//            Map<String, Object> manufacture_date_map = new HashMap<String, Object>();
//            if (message.containsKey("manufacture_date") && message.get("manufacture_date") != null) {
//                String manufacture_date = message.get("manufacture_date").toString();
//                manufacture_date_map.put("name", manufacture_date);
//                map.put("manufacture_date", manufacture_date_map);
//            } else {
//                String manufacture_date = Utils.getYearFromVin(vin);
//                manufacture_date_map.put("name", manufacture_date);
//                map.put("manufacture_date", manufacture_date_map);
//            }
//        }
//
//        return map;
//    }


    @Override
    public AjaxResult getByVin_new(String vin, int user_id) {
        Map<String, Object> map = new HashMap<String, Object>();
//        boolean is_vin_exist = carsMapper.existsByVin(vin); // 生成0或1
        int is_vin_exist = isVinExisted(vin, user_id);

        if (is_vin_exist == 1) {
            return AjaxResult.error("你已发布过该车架号的车辆，不允许重复发布");
        }else if(is_vin_exist == 2){
            return AjaxResult.error("车辆被其他车商抢先发布，如需继续发布，需准备证明材料并联系客服下架车辆后再发布");
        }

        String responseString = null;
        VinPOJO vinPOJO = carsMapper.getVinInfos(vin); //先从自有vin库中查询是否有这个车架号
        if (vinPOJO == null) {
            responseString = QixiubaoHttp.searchVin(vin);
            if (responseString == null) {
                return AjaxResult.error("未找到该车架号");
            } else {
                vinPOJO = new VinPOJO();
                vinPOJO.setVin(vin);
                vinPOJO.setInfos(responseString);
                carsMapper.insertVin(vinPOJO);
            }
        } else {
            responseString = vinPOJO.getInfos();
        }

        JSONObject json = JSON.parseObject(responseString);
        if (!json.containsKey("result")) {
            map.put("1", true);
            return AjaxResult.error("未找到该车架号");
        }

        JSONObject result = json.getJSONObject("result");
        //获取出厂日期
        String manufacture_date = "";
        if (result.containsKey("prod_time")) {
            String prod_time = result.getString("prod_time");
            manufacture_date = prod_time.substring(0, 4);
        } else {
            manufacture_date = Utils.getYearFromVin(vin);
        }


        if (!result.containsKey("models")) {
            map.put("1", true);
            return AjaxResult.error("未找到该车架号");
        }

        JSONArray models = result.getJSONArray("models");
//        List<JSONObject> vin_details = Qixiubao.parseResponse(responseString);
        for (int i = 0; i < models.size(); i++) {
            JSONObject model = models.getJSONObject(i);

            String brand = model.getString("brand");
            String series = model.getString("series");
            String type = series + " " + model.getString("model_name");
            int brand_id = model.getInteger("brand_id");
            int series_id = model.getInteger("series_id");
            int type_id = model.getInteger("model_id");

            boolean is_brandid_exist = indexMapper.isBrandIdExisted("zh", brand_id);
            if (!is_brandid_exist) {
                System.out.println("info: there is no brand_id " + brand_id + " => brand:" + brand + ", insert it to car_brand");
                String brand_first_char = Utils.getFirstLetter(brand);
                updateNewBrand("", brand, brand_id, brand_first_char, "zh");
            }


            boolean is_seriesid_exist = indexMapper.isSeriesIdExisted("zh", series_id);
            if (!is_seriesid_exist) {
                System.out.println("info: there is no series_id " + series_id + " => series:" + series + ", insert it to car_series");
                String first_char = Utils.getFirstLetter(series);
                updateNewSeries(brand_id, series_id, first_char, "zh", series);
            }

            boolean is_typeid_exist = indexMapper.isTypeIdExisted("zh", type_id);
            if (!is_typeid_exist) {
                System.out.println("info: there is no type_id " + type_id + " => type:" + type + ", insert it to car_type");
                JSONObject standard_data = Transform.standard_data(model);
                JSONObject details_json = QixiubaoHttp.searchTypeDetails(String.valueOf(type_id));
                String details = details_json.getString("params");
                CarTypePOJO carTypePOJO = createNewCarTypePOJO(standard_data);
                JSONObject battery_infos = Transform.getBatteryInfosFromDetails(details);
                double battery_capacity = battery_infos.getDouble("battery_capacity");
                double pure_electric_range = battery_infos.getDouble("pure_electric_range");
                carTypePOJO.setDetails(details);
                carTypePOJO.setBattery_capacity(battery_capacity);
                carTypePOJO.setPure_electric_range(pure_electric_range);
                carTypePOJO.setLanguage("zh");
                carTypePOJO.setBrand_id(brand_id);
                carTypePOJO.setSeries_id(series_id);
                carTypePOJO.setType_id(type_id);
                updateNewType(carTypePOJO);
            }

            Map<String, Object> brand_map = new HashMap<String, Object>();
            brand_map.put("name", brand);
            brand_map.put("brand_id", brand_id);
            map.put("brand", brand_map);

            Map<String, Object> series_map = new HashMap<String, Object>();
            series_map.put("name", series);
            series_map.put("series_id", series_id);
            map.put("series", series_map);

            Map<String, Object> type_map = new HashMap<String, Object>();
            type_map.put("name", type);
            type_map.put("type_id", type_id);
            map.put("type", type_map);
            map.put("name", getCarName(brand, series, type));

            Map<String, Object> manufacture_date_map = new HashMap<String, Object>();
            manufacture_date_map.put("name", manufacture_date);
            map.put("manufacture_date", manufacture_date_map);

        }

        return AjaxResult.success("成功找到车架号", map);
    }

    private int isVinExisted(String vin, int user_id) {
        VinExistedDTO vinExistedDTO = carsMapper.isVinExisted(vin);
        if (vinExistedDTO == null) {
            return 0;
        } else {
            int user_id_of_vin = vinExistedDTO.getUser_id();
            if (user_id == user_id_of_vin) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    private CarTypePOJO createNewCarTypePOJO(JSONObject message) {
        String language = message.getOrDefault("language", "zh").toString();
        List<IndexDTO> indexes = indexMapper.getIndex(language);

        Map<String, Integer> value_id_map = new HashMap<String, Integer>();
        Set<String> mapping_fields = new HashSet<String>();
        for (IndexDTO indexDTO : indexes) {
            int is_mapping = indexDTO.getIs_mapping();
            String value = indexDTO.getValue();
            int value_id = indexDTO.getValue_id();
            String field = indexDTO.getField();
            String field_value = field + value;
            if (is_mapping == 0) {
//                value_id_map.put(value, value_id);
                value_id_map.put(field_value, value_id);
                mapping_fields.add(field);
            }
        }

        for (String key : message.keySet()) {
            Object value = message.getOrDefault(key, null);
            String field_value = key + value;
            if (mapping_fields.contains(key)) {
                if (value_id_map.containsKey(field_value)) {
                    message.put(key, value_id_map.get(field_value));
                } else {
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
        if (bool) {
            return AjaxResult.error("车架号所属汽车已经上传过，请不要重复上传");
        }

        carsPOJO.setVehicleType(2); //默认是二手车无增值税发票

        if (carsPOJO.getLabel().size() > 0) {
            String label_string = carsPOJO.getLabel().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setLabel_string(label_string);
        }

        if (carsPOJO.getCar_condition().size() > 0) {
            String car_condition_string = carsPOJO.getCar_condition().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setCar_condition_string(car_condition_string);
        }

        if (carsPOJO.getCoating().size() > 0) {
            String coating_string = carsPOJO.getCoating().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setCoating_string(coating_string);
        }

        if (carsPOJO.getComponent().size() > 0) {
            String component_string = carsPOJO.getComponent().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setComponent_string(component_string);
        }

        if (carsPOJO.getEngine_condition().size() > 0) {
            String engine_condition_string = carsPOJO.getEngine_condition().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setEngine_condition_string(engine_condition_string);
        }

        if (carsPOJO.getTransmission_condition().size() > 0) {
            String transmission_condition_string = carsPOJO.getTransmission_condition().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setTransmission_condition_string(transmission_condition_string);
        }

        if (carsPOJO.getNumber_of_transfers().size() > 0) {
            String number_of_transfers_string = carsPOJO.getNumber_of_transfers().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setNumber_of_transfers_string(number_of_transfers_string);
        }

        if (carsPOJO.getMileage_contition().size() > 0) {
            String mileage_contition_string = carsPOJO.getMileage_contition().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setMileage_contition_string(mileage_contition_string);
        }

        carsMapper.insertNewCar(carsPOJO);
        return AjaxResult.success("上传汽车成功");
    }


    @Override
    public AjaxResult uploadNewCar(CarsPOJO carsPOJO, int user_id) {
        int vehicleType = carsPOJO.getVehicleType();
        //如果是二手车，需要校验车架号是否上传过
        if (vehicleType == usedcar_with_invoice || vehicleType == usedcar_without_invoice) {

            String vin = carsPOJO.getVin();
            if (vin == null) {
                return AjaxResult.error("Missing required parameter: vin");
            } else {

                int is_vin_exist = isVinExisted(vin, user_id);

                if (is_vin_exist == 1) {
                    return AjaxResult.error("你已发布过该车辆，不允许重复发布");
                }else if(is_vin_exist == 2){
                    return AjaxResult.error("车辆被其他车商抢先发布，如需继续发布，需准备证明材料并联系客服下架车辆后再发布");
                }

//                boolean bool = carsMapper.existsByVin(carsPOJO.getVin());
//                if (bool) {
//                    return AjaxResult.error("车架号所属汽车已经上传过，请不要重复上传");
//                }
            }

        } else if (vehicleType == newcar) {
            carsPOJO.setVin("");
        } else {
            return AjaxResult.error("error: vehicleType should be one of [0,1,2]");
        }

        int operate = carsPOJO.getOperate();
        //判断operate操作是否合法
        if (operate == operate_save) {
            carsPOJO.setState(Constant.STATE_ON_DRAFT);
        } else if (operate == operate_deploy) {
//            carsPOJO.setState(Constant.STATE_ON_REVIEW);
            carsPOJO.setState(Constant.STATE_ON_SALE);
        } else {
            return AjaxResult.error("error: operate should be one of [0,1]");
        }

        dealWithSpecialFields(carsPOJO);
        String randomString = getRandomString();
        carsPOJO.setRandomString(randomString);

        carsMapper.insertNewCar(carsPOJO);
        long id = carsMapper.getCaridByRandomString(randomString);
        HashMap<String, Long> result = new HashMap<String, Long>();
        result.put("id", id);

        //异步翻译车型详细信息
        int type_id = carsPOJO.getType_id();
        asyncService.typeDetailsRealTimeTranslate(type_id);
        return AjaxResult.success("上传新车辆成功", result);
    }

    @Override
    public AjaxResult updateCar(CarsPOJO carsPOJO) {
        int vehicleType = carsPOJO.getVehicleType();
        //校验vehicleType是否合法
        if (vehicleType != usedcar_with_invoice
                && vehicleType != usedcar_without_invoice
                && vehicleType != newcar) {
            return AjaxResult.error("error: vehicleType should be one of [0,1,2]");
        }

        int operate = carsPOJO.getOperate();
        //判断operate操作是否合法
        if (operate == operate_save) {
            carsPOJO.setState(Constant.STATE_ON_DRAFT);
        } else if (operate == operate_deploy) {
//            carsPOJO.setState(Constant.STATE_ON_REVIEW);
            carsPOJO.setState(Constant.STATE_ON_SALE);
        } else {
            return AjaxResult.error("error: operate should be one of [0,1]");
        }

        dealWithSpecialFields(carsPOJO);
        carsMapper.updateCarInfo(carsPOJO);
        return AjaxResult.success("更新汽车信息成功");
    }

    //几个标签字段值需要做单独处理
    private void dealWithSpecialFields(CarsPOJO carsPOJO) {
        if (carsPOJO.getLabel().size() > 0) {
            String label_string = carsPOJO.getLabel().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setLabel_string(label_string);
        }

        if (carsPOJO.getCar_condition().size() > 0) {
            String car_condition_string = carsPOJO.getCar_condition().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setCar_condition_string(car_condition_string);
        }

        if (carsPOJO.getCoating().size() > 0) {
            String coating_string = carsPOJO.getCoating().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setCoating_string(coating_string);
        }

        if (carsPOJO.getComponent().size() > 0) {
            String component_string = carsPOJO.getComponent().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setComponent_string(component_string);
        }

        if (carsPOJO.getEngine_condition().size() > 0) {
            String engine_condition_string = carsPOJO.getEngine_condition().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setEngine_condition_string(engine_condition_string);
        }

        if (carsPOJO.getTransmission_condition().size() > 0) {
            String transmission_condition_string = carsPOJO.getTransmission_condition().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setTransmission_condition_string(transmission_condition_string);
        }

        if (carsPOJO.getNumber_of_transfers().size() > 0) {
            String number_of_transfers_string = carsPOJO.getNumber_of_transfers().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setNumber_of_transfers_string(number_of_transfers_string);
        }

        if (carsPOJO.getMileage_contition().size() > 0) {
            String mileage_contition_string = carsPOJO.getMileage_contition().stream().map(String::valueOf).collect(Collectors.joining(","));
            carsPOJO.setMileage_contition_string(mileage_contition_string);
        }
    }

    @Override
    public List<FirstPageCarsDTO> searchCarsByKeywords(SearchCarsDTO searchCarsDTO) {
        List<Integer> brand_ids = indexMapper.getBrandIdByKeywords(searchCarsDTO.getLanguage(), searchCarsDTO.getKeywords());
        List<Integer> series_ids = indexMapper.getSeriesIdByKeywords(searchCarsDTO.getLanguage(), searchCarsDTO.getKeywords());

//        if(brand_ids != null){
//            for(Integer brandid : brand_ids){
//                System.out.println(brandid);
//            }
//        }
//
//
//        if(series_ids != null){
//            for(Integer seriesid : series_ids){
//                System.out.println(seriesid);
//            }
//        }


        if ((brand_ids == null || brand_ids.size() == 0)
                && (series_ids == null || series_ids.size() == 0)) {
            return new ArrayList<FirstPageCarsDTO>();
        } else {
            List<FirstPageCarsDTO> firstPageCarsDTOS = carsMapper.searchCarsByKeywords(searchCarsDTO, brand_ids, series_ids);
            for (FirstPageCarsDTO firstPageCarsDTO : firstPageCarsDTOS) {
                firstPageCarsDTO.setName(getCarName(firstPageCarsDTO.getBrand(), firstPageCarsDTO.getSeries(), firstPageCarsDTO.getType()));
            }
            return firstPageCarsDTOS;
        }
    }

    @Override
    public String getCarTypeDetails(int type_id, String language) {
        return carsMapper.getCarTypeDetails(type_id, language);
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

    public synchronized void updateNewBrand(String logo,
                                            String brand,
                                            int brand_id,
                                            String first_char,
                                            String language) {

        indexMapper.updateNewBrand(logo, brand, brand_id, first_char, language);
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

    public synchronized void updateNewSeries(int brand_id,
                                             int series_id,
                                             String first_char,
                                             String language,
                                             String series) {
        indexMapper.updateNewSeries(brand_id, series_id, first_char, language, series);
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


    private synchronized void updateNewType(CarTypePOJO carTypePOJO) {
        indexMapper.updateNewType(carTypePOJO);
    }

    @Override
    public String getCarName(CarNameDTO carNameDTO, String language) {
        return getCarName(carNameDTO.getBrand(), carNameDTO.getSeries(), carNameDTO.getType());
    }

    @Override
    public void updateCarState(OperateUpdateStateDTO operateUpdateStateDTO) {
        carsMapper.updateCarState(operateUpdateStateDTO);
    }

    @Override
    public AjaxResult getStateCars(OperateSearchCarsDTO operateSearchCarsDTO) {
        try {
            List<OperateCarsDTO> carsPOJOS = carsMapper.getStateCars(operateSearchCarsDTO);

            List<IndexDTO> indexes = indexMapper.getIndex("zh");
            Map<String, IndexDTO> id_mapping = new HashMap<String, IndexDTO>();
            Map<String, IndexDTO> field_mapping = new HashMap<String, IndexDTO>();
            for (IndexDTO indexDTO : indexes) {
                int is_mapping = indexDTO.getIs_mapping();
                int id = indexDTO.getValue_id();
                String field = indexDTO.getField();

                if (is_mapping == 0) {
                    id_mapping.put(field + id, indexDTO);
                    field_mapping.put(field, indexDTO);
                }
            }


            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            for (OperateCarsDTO car : carsPOJOS) {
                String carsPOJOJsonString = JSON.toJSONString(car, SerializerFeature.WriteMapNullValue);
                JSONObject jsonObject = JSON.parseObject(carsPOJOJsonString);
                Class<?> clazz = car.getClass();
                Field[] fields = clazz.getDeclaredFields();

                Map<String, Object> tmp = jsonObject;

                for (Field field : fields) {
                    field.setAccessible(true);
                    String key = field.getName();

                    if (multi_values_fields.containsKey(key)) {
                        String value_name = multi_values_fields.get(key);
                        String[] real_ids = ((String) field.get(car)).split(",");
                        List<Integer> ids = Arrays.stream(real_ids).map(x -> {
                            return Integer.valueOf(x);
                        }).filter(x -> {
                            return x != -1;
                        }).collect(Collectors.toList());
                        tmp.put(value_name, ids);

                    } else if (multi_values_fields.values().contains(key)) {
                        continue;
                    } else {
                        if (field_mapping.containsKey(key)) {
                            Object real_id = field.get(car);

                            if (id_mapping.containsKey(key + real_id)) {
                                String real_name = id_mapping.get(key + real_id).getValue();
                                tmp.put(key + "_name", real_name);
                            } else {
                                tmp.put(key + "_name", null);
                            }

                        }
                    }
                }

                result.add(tmp);
            }

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("getStateCars failed");
        }

    }


    public String getRandomString() {
        LocalDateTime localDateTime = LocalDateTime.now();

        String time = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomChar = Utils.getRandomChar(10);
        return time + randomChar;
    }

    public String getCarName(String brand, String series, String type) {
        String _type = type.replaceAll(java.util.regex.Pattern.quote(series), "").trim();
        String _series = series.replaceAll(java.util.regex.Pattern.quote(brand), "").trim();
        return brand + " " + _series + " " + _type;
    }

}
