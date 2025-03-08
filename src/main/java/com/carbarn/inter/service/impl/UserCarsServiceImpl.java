package com.carbarn.inter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.carbarn.inter.config.CarbarnConfig;
import com.carbarn.inter.mapper.CarsMapper;
import com.carbarn.inter.mapper.IndexMapper;
import com.carbarn.inter.mapper.UserCarsMapper;
import com.carbarn.inter.mapper.UserMapper;
import com.carbarn.inter.pojo.CarTypePOJO;
import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.dto.cars.CarsOfUsersDTO;
import com.carbarn.inter.pojo.dto.cars.FirstPageCarsDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import com.carbarn.inter.pojo.dto.cars.index.IndexDTO;
import com.carbarn.inter.pojo.dto.cars.index.TypeMessageDTO;
import com.carbarn.inter.pojo.language.LanguageConstant;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.pojo.usercar.Constant;
import com.carbarn.inter.pojo.usercar.StateCountPOJO;
import com.carbarn.inter.pojo.usercar.UserCarList;
import com.carbarn.inter.pojo.usercar.UserCarPOJO;
import com.carbarn.inter.pojo.vin.VinPOJO;
import com.carbarn.inter.service.CarsService;
import com.carbarn.inter.service.UserCarsService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.Utils;
import com.carbarn.inter.utils.qixiubao.Qixiubao;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserCarsServiceImpl implements UserCarsService {


    @Autowired
    private UserCarsMapper userCarsMapper;
    @Autowired
    private CarsMapper carsMapper;

    @Autowired
    private IndexMapper indexMapper;

    @Override
    public AjaxResult deleteCars(int carid, long user_id) {
        CarsPOJO carsPOJO = carsMapper.getCarsByID(carid);
        if (carsPOJO == null) {
            return AjaxResult.error("当前车辆不存在，无法删除!", new HashMap<String, Object>());
        }

        if (user_id != carsPOJO.getUser_id()) {
            return AjaxResult.error("该车辆非当前用户所属, 禁止删除!");
        }
        try {
            userCarsMapper.updateCarState(Constant.STATE_ON_DELETE, carid);
            return AjaxResult.success("汽车删除成功!");
        } catch (Exception e) {
            return AjaxResult.error("汽车删除失败!");
        }
    }

    @Override
    public AjaxResult unreviewCars(int carid, long user_id) {
        CarsPOJO carsPOJO = carsMapper.getCarsByID(carid);
        if (carsPOJO == null) {
            return AjaxResult.error("当前车辆不存在，无法撤销审核!", new HashMap<String, Object>());
        }

        if (user_id != carsPOJO.getUser_id()) {
            return AjaxResult.error("该车辆非当前用户所属, 禁止撤销审核!");
        }

        try {
            userCarsMapper.updateCarState(Constant.STATE_ON_DRAFT, carid);
            return AjaxResult.success("汽车撤销审核成功，回退到草稿箱!");
        } catch (Exception e) {
            return AjaxResult.error("汽车撤销审核失败!");
        }
    }

    @Override
    public AjaxResult delist(int carid, long user_id) {
        CarsPOJO carsPOJO = carsMapper.getCarsByID(carid);
        if (carsPOJO == null) {
            return AjaxResult.error("当前车辆不存在，无法下架!", new HashMap<String, Object>());
        }

        if (user_id != carsPOJO.getUser_id()) {
            return AjaxResult.error("该车辆非当前用户所属, 禁止下架!");
        }

        try {
            userCarsMapper.updateCarState(Constant.STATE_ON_DELIST, carid);
            return AjaxResult.success("汽车下架成功!");
        } catch (Exception e) {
            return AjaxResult.error("汽车下架失败!");
        }
    }

    @Override
    public AjaxResult selectUserCars(long user_id, int state) {
        List<UserCarPOJO> usercarlist = userCarsMapper.selectUserCars(LanguageConstant.ZH, user_id, state);
        return AjaxResult.success("获取用户汽车信息成功", usercarlist);
//        UserCarList on_sale = new UserCarList();
//        on_sale.setKey(Constant.KEY_ON_SALE);
//        on_sale.setCars(new ArrayList<UserCarPOJO>());
//
//        UserCarList on_draft = new UserCarList();
//        on_draft.setKey(Constant.KEY_ON_DRAFT);
//        on_draft.setCars(new ArrayList<UserCarPOJO>());
//
//        UserCarList on_review = new UserCarList();
//        on_review.setKey(Constant.KEY_ON_REVIEW);
//        on_review.setCars(new ArrayList<UserCarPOJO>());
//
//        UserCarList on_delist = new UserCarList();
//        on_delist.setKey(Constant.KEY_ON_DELIST);
//        on_delist.setCars(new ArrayList<UserCarPOJO>());
//
//        UserCarList on_unreview = new UserCarList();
//        on_unreview.setKey(Constant.KEY_ON_UNREVIEW);
//        on_unreview.setCars(new ArrayList<UserCarPOJO>());
//
//        int on_sale_count = 0;
//        int on_draft_count = 0;
//        int on_review_count = 0;
//        int on_delist_count = 0;
//        int on_unreview_count = 0;
//
//
//        for (UserCarPOJO userCarPOJO : usercarlist) {
//            if (userCarPOJO.getState() == Constant.STATE_ON_SALE) {
//                on_sale_count++;
//                on_sale.getCars().add(userCarPOJO);
//            } else if (userCarPOJO.getState() == Constant.STATE_ON_DRAFT) {
//                on_draft_count++;
//                on_draft.getCars().add(userCarPOJO);
//            } else if (userCarPOJO.getState() == Constant.STATE_ON_REVIEW) {
//                on_review_count++;
//                on_review.getCars().add(userCarPOJO);
//            } else if (userCarPOJO.getState() == Constant.STATE_ON_DELIST) {
//                on_delist_count++;
//                on_delist.getCars().add(userCarPOJO);
//            } else if (userCarPOJO.getState() == Constant.STATE_ON_UNREVIEW) {
//                on_unreview_count++;
//                on_unreview.getCars().add(userCarPOJO);
//            }
//        }
//
//        on_sale.setCount(on_sale_count);
//        on_draft.setCount(on_draft_count);
//        on_review.setCount(on_review_count);
//        on_delist.setCount(on_delist_count);
//        on_unreview.setCount(on_unreview_count);
//
//        List<UserCarList> result = new ArrayList<UserCarList>();
//        result.add(on_sale);
//        result.add(on_draft);
//        result.add(on_review);
//        result.add(on_delist);
//        result.add(on_unreview);


    }

    @Override
    public AjaxResult selectStateCount(long user_id) {

        List<StateCountPOJO> stateCountPOJOS = userCarsMapper.selectStateCount(user_id);

        if (stateCountPOJOS == null) {
            List<StateCountPOJO> list = new ArrayList<StateCountPOJO>();
            StateCountPOJO on_sale = new StateCountPOJO();
            on_sale.setState(Constant.STATE_ON_SALE);
            on_sale.setKey(Constant.KEY_ON_SALE);
            on_sale.setCnt(0);

            StateCountPOJO on_draft = new StateCountPOJO();
            on_draft.setState(Constant.STATE_ON_DRAFT);
            on_draft.setKey(Constant.KEY_ON_DRAFT);
            on_draft.setCnt(0);

            StateCountPOJO on_review = new StateCountPOJO();
            on_review.setState(Constant.STATE_ON_REVIEW);
            on_review.setKey(Constant.KEY_ON_REVIEW);
            on_review.setCnt(0);

            StateCountPOJO on_delist = new StateCountPOJO();
            on_delist.setState(Constant.STATE_ON_DELIST);
            on_delist.setKey(Constant.KEY_ON_DELIST);
            on_delist.setCnt(0);

            StateCountPOJO on_unreview = new StateCountPOJO();
            on_unreview.setState(Constant.STATE_ON_UNREVIEW);
            on_unreview.setKey(Constant.KEY_ON_UNREVIEW);
            on_unreview.setCnt(0);

            return AjaxResult.success("获取用户车辆个数信息成功!", list);

        } else {

            Map<Integer, StateCountPOJO> state_pojo = new HashMap<Integer, StateCountPOJO>();
            List<StateCountPOJO> list = new ArrayList<StateCountPOJO>();
            for (StateCountPOJO stateCountPOJO : stateCountPOJOS) {
                state_pojo.put(stateCountPOJO.getState(), stateCountPOJO);
            }

            if (state_pojo.containsKey(Constant.STATE_ON_SALE)) {
                StateCountPOJO on_sale = state_pojo.get(Constant.STATE_ON_SALE);
                on_sale.setKey(Constant.KEY_ON_SALE);
                list.add(on_sale);
            } else {
                StateCountPOJO on_sale = new StateCountPOJO();
                on_sale.setState(Constant.STATE_ON_SALE);
                on_sale.setKey(Constant.KEY_ON_SALE);
                on_sale.setCnt(0);
                list.add(on_sale);
            }

            if (state_pojo.containsKey(Constant.STATE_ON_DRAFT)) {
                StateCountPOJO on_draft = state_pojo.get(Constant.STATE_ON_DRAFT);
                on_draft.setKey(Constant.KEY_ON_DRAFT);
                list.add(on_draft);
            } else {
                StateCountPOJO on_draft = new StateCountPOJO();
                on_draft.setState(Constant.STATE_ON_DRAFT);
                on_draft.setKey(Constant.KEY_ON_DRAFT);
                on_draft.setCnt(0);
                list.add(on_draft);
            }

            if (state_pojo.containsKey(Constant.STATE_ON_REVIEW)) {
                StateCountPOJO on_review = state_pojo.get(Constant.STATE_ON_REVIEW);
                on_review.setKey(Constant.KEY_ON_REVIEW);
                list.add(on_review);
            } else {
                StateCountPOJO on_review = new StateCountPOJO();
                on_review.setState(Constant.STATE_ON_REVIEW);
                on_review.setKey(Constant.KEY_ON_REVIEW);
                on_review.setCnt(0);
                list.add(on_review);
            }

            if (state_pojo.containsKey(Constant.STATE_ON_DELIST)) {
                StateCountPOJO on_delist = state_pojo.get(Constant.STATE_ON_DELIST);
                on_delist.setKey(Constant.KEY_ON_DELIST);
                list.add(on_delist);
            } else {
                StateCountPOJO on_delist = new StateCountPOJO();
                on_delist.setState(Constant.STATE_ON_DELIST);
                on_delist.setKey(Constant.KEY_ON_DELIST);
                on_delist.setCnt(0);
                list.add(on_delist);
            }

            if (state_pojo.containsKey(Constant.STATE_ON_UNREVIEW)) {
                StateCountPOJO on_unreview = state_pojo.get(Constant.STATE_ON_UNREVIEW);
                on_unreview.setKey(Constant.KEY_ON_UNREVIEW);
                list.add(on_unreview);
            } else {
                StateCountPOJO on_unreview = new StateCountPOJO();
                on_unreview.setState(Constant.STATE_ON_UNREVIEW);
                on_unreview.setKey(Constant.KEY_ON_UNREVIEW);
                on_unreview.setCnt(0);
                list.add(on_unreview);
            }

            return AjaxResult.success("获取用户车辆个数信息成功!", list);
        }
    }

    @Override
    public AjaxResult edit(int carid, long user_id) {
        CarsPOJO carsPOJO = carsMapper.getCarsByID(carid);
        if (carsPOJO == null) {
            return AjaxResult.error("当前车辆不存在，无法修改!");
        }

        if (user_id != carsPOJO.getUser_id()) {
            return AjaxResult.error("该车辆非当前用户所属, 禁止修改!");
        }

        String language = LanguageConstant.ZH;

        String carsPOJOJsonString = JSON.toJSONString(carsPOJO, SerializerFeature.WriteMapNullValue);
        JSONObject jsonObject = JSON.parseObject(carsPOJOJsonString);
        Map<String, Object> map = jsonObject;

        try {
            int type_id = carsPOJO.getType_id();
            TypeMessageDTO typeMessageDTO = indexMapper.getTypeMessage(language, type_id);
            String brand = typeMessageDTO.getBrand();
            String series = typeMessageDTO.getSeries();
            String type = typeMessageDTO.getType();
            int brand_id = typeMessageDTO.getBrand_id();
            int series_id = typeMessageDTO.getSeries_id();
            if (brand != null && series != null && type != null) {
                String name = brand + " " + series + " " + type;
                map.put("brand", brand);
                map.put("brand_id", brand_id);
                map.put("series", series);
                map.put("series_id", series_id);
                map.put("type", type);
                map.put("name", name);
            } else {
                map.clear();
                return AjaxResult.error("该车辆获取出错, 无法修改!");
            }


            List<IndexDTO> indexes = indexMapper.getIndex(language);
            Map<Integer, IndexDTO> id_mapping = new HashMap<Integer, IndexDTO>();
            Map<String, IndexDTO> field_mapping = new HashMap<String, IndexDTO>();

            for (IndexDTO indexDTO : indexes) {
                int is_mapping = indexDTO.getIs_mapping();
                int id = indexDTO.getValue_id();
                String field = indexDTO.getField();

                if (is_mapping == 0) {
                    id_mapping.put(id, indexDTO);
                    field_mapping.put(field, indexDTO);
                }
            }

            Class<?> clazz = carsPOJO.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String key = field.getName();

                if (CarsServiceImpl.multi_values_fields.containsKey(key)) {
                    String value_name = CarsServiceImpl.multi_values_fields.get(key);
                    String[] real_ids = ((String) field.get(carsPOJO)).split(",");
                    List<Integer> ids = Arrays.stream(real_ids).map(x -> {
                        return Integer.valueOf(x);
                    }).filter(x -> {
                        return x != -1;
                    }).collect(Collectors.toList());
                    map.put(value_name, ids);

                } else if (CarsServiceImpl.multi_values_fields.values().contains(key)) {
                    continue;
                } else {
                    if (field_mapping.containsKey(key)) {
                        Object real_id = field.get(carsPOJO);

                        if (id_mapping.containsKey(real_id) && key.equals(id_mapping.get(real_id).getField())) {
                            String real_name = id_mapping.get(real_id).getValue();
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


        return AjaxResult.success("获取车辆信息成功，请进行修改！", map);
    }


}
