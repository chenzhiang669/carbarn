package com.carbarn.inter.service.impl;

import com.carbarn.inter.mapper.IndexMapper;
import com.carbarn.inter.pojo.dto.cars.index.*;
import com.carbarn.inter.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexMapper indexMapper;

//    @Override
//    public List<HashMap<String, Object>> getIndex(String language) {
//        List<IndexDTO> indexs =  indexMapper.getIndex(language);
//
//        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//
//
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        for(int i = 0; i < indexs.size(); i++){
//
//            String index = indexs.get(i).getIndex();
//            String value = indexs.get(i).getValue();
//            int priority = indexs.get(i).getPriority();
//
//            if(i == 0){
//                List<String> values = new ArrayList<String>();
//                values.add(value);
//                map.put("index", index);
//                map.put("values", values);
//                map.put("priority", priority);
//            }else if(!index.equals(map.get("index"))){
//                list.add(map);
//                map = new HashMap<String, Object>();
//                List<String> values = new ArrayList<String>();
//                values.add(value);
//                map.put("index", index);
//                map.put("values", values);
//                map.put("priority", priority);
//            }else{
//                List<String> values = (List<String>) map.get("values");
//                values.add(value);
//            }
//        }
//
//        list.add(map);
//
//        return list;
//    }



    @Override
    public List<HashMap<String, Object>> getIndex(String language) {
        List<IndexDTO> indexs =  indexMapper.getIndex(language);

        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();


        HashMap<String, Object> map = new HashMap<String, Object>();
        for(int i = 0; i < indexs.size(); i++){

            String index = indexs.get(i).getIndex();
            String value = indexs.get(i).getValue();
            int priority = indexs.get(i).getPriority();
            int value_id = indexs.get(i).getValue_id();
            String field = indexs.get(i).getField();
            int is_mapping = indexs.get(i).getIs_mapping();

            if("displacement_type".equals(field)){
                continue;
            }

            if(i == 0){
                List<Map<String, Object>> li = new ArrayList<Map<String, Object>>();
                Map<String, Object> value_map = new HashMap<String, Object>();
                value_map.put("name", value);
                value_map.put("value_id", value_id);
                li.add(value_map);
                map.put("name", index);
                if(is_mapping == 0){
                    map.put("values", li);
                }
                map.put("priority", priority);
                map.put("field", field);
            }else if(!index.equals(map.get("name"))){
                list.add(map);
                map = new HashMap<String, Object>();
                List<Map<String, Object>> li = new ArrayList<Map<String, Object>>();
                Map<String, Object> value_map = new HashMap<String, Object>();
                value_map.put("name", value);
                value_map.put("value_id", value_id);
                li.add(value_map);
                map.put("name", index);
                if(is_mapping == 0){
                    map.put("values", li);
                }
                map.put("priority", priority);
                map.put("field", field);
            }else{
                List<Map<String, Object>>  li = (List<Map<String, Object>> ) map.get("values");
                Map<String, Object> value_map = new HashMap<String, Object>();
                value_map.put("name", value);
                value_map.put("value_id", value_id);
                li.add(value_map);

            }
        }

        list.add(map);

        return list;
    }

//    @Override
//    public Map<String, List<Map<String, Object>>> getBrand(String language) {
//        List<BrandDTO> brands = indexMapper.getBrand(language);
//        Map<String, List<Map<String, Object>>> map = new TreeMap<String, List<Map<String, Object>>>();
//
//        for(int i = 0; i < brands.size(); i++){
//            String first_char = brands.get(i).getFirst_char();
//            String brand = brands.get(i).getBrand();
//            String logo = brands.get(i).getLogo();
//            int brand_id = brands.get(i).getBrand_id();
//
//            if(!map.containsKey(first_char)){
//                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//                Map<String, Object> in_map = new HashMap<String, Object>();
//                in_map.put("brand", brand);
//                in_map.put("logo",logo);
//                in_map.put("brand_id",brand_id);
//                list.add(in_map);
//                map.put(first_char, list);
//
//            }else {
//                List<Map<String, Object>> list = map.get(first_char);
//                Map<String, Object> in_map = new HashMap<String, Object>();
//                in_map.put("brand", brand);
//                in_map.put("logo",logo);
//                in_map.put("brand_id",brand_id);
//                list.add(in_map);
//            }
//        }
//
//        return map;
//
//    }


    @Override
    public List<Object> getBrand(String language) {
        List<BrandDTO> origin_brands = indexMapper.getBrand(language);
        List<Object> list = new ArrayList<Object>();
        Map<String, Object> out_map = new HashMap<String, Object>();

        for(int i = 0; i < origin_brands.size(); i++){
            String first_char = origin_brands.get(i).getFirst_char();
            String brand = origin_brands.get(i).getBrand();
            String logo = origin_brands.get(i).getLogo();
            int brand_id = origin_brands.get(i).getBrand_id();

            if(i==0){
                out_map = new HashMap<String, Object>();
                Map<String, Object> in_map = new HashMap<String, Object>();
                in_map.put("brand",brand);
                in_map.put("logo", logo);
                in_map.put("brand_id", brand_id);
                List<Object> brands = new ArrayList<Object>();
                brands.add(in_map);
                out_map.put("first_char", first_char);
                out_map.put("brands", brands);
            }else if(!first_char.equals(out_map.get("first_char"))){
                list.add(out_map);
                out_map = new HashMap<String, Object>();
                Map<String, Object> in_map = new HashMap<String, Object>();
                in_map.put("brand",brand);
                in_map.put("logo", logo);
                in_map.put("brand_id", brand_id);
                List<Object> brands = new ArrayList<Object>();
                brands.add(in_map);
                out_map.put("first_char", first_char);
                out_map.put("brands", brands);

            }else {
                List<Object> brands = (List<Object>) out_map.get("brands");
                Map<String, Object> in_map = new HashMap<String, Object>();
                in_map.put("brand",brand);
                in_map.put("logo", logo);
                in_map.put("brand_id", brand_id);
                brands.add(in_map);
            }
        }

        list.add(out_map);
        return list;
    }

    @Override
    public List<Object> getSeries(String language, int brand_id) {
        List<SeriesDTO> origin_brands = indexMapper.getSeries(language, brand_id);
        List<Object> list = new ArrayList<Object>();
        Map<String, Object> out_map = new HashMap<String, Object>();

        for(int i = 0; i < origin_brands.size(); i++){
            String first_char = origin_brands.get(i).getFirst_char();
            String series = origin_brands.get(i).getSeries();
            int series_id = origin_brands.get(i).getSeries_id();

            if(i==0){
                out_map = new HashMap<String, Object>();
                Map<String, Object> in_map = new HashMap<String, Object>();
                in_map.put("series",series);
                in_map.put("series_id", series_id);
                List<Object> serieses = new ArrayList<Object>();
                serieses.add(in_map);
                out_map.put("first_char", first_char);
                out_map.put("series", serieses);
            }else if(!first_char.equals(out_map.get("first_char"))){
                list.add(out_map);
                out_map = new HashMap<String, Object>();
                Map<String, Object> in_map = new HashMap<String, Object>();
                in_map.put("series",series);
                in_map.put("series_id", series_id);
                List<Object> serieses = new ArrayList<Object>();
                serieses.add(in_map);
                out_map.put("first_char", first_char);
                out_map.put("series", serieses);

            }else {
                List<Object> serieses = (List<Object>) out_map.get("series");
                Map<String, Object> in_map = new HashMap<String, Object>();
                in_map.put("series",series);
                in_map.put("series_id", series_id);
                serieses.add(in_map);
            }
        }

        list.add(out_map);
        return list;
    }

//    @Override
//    public Map<String, Object> getSeries(String language, int brand_id) {
//        List<SeriesDTO> serieses = indexMapper.getSeries(language, brand_id);
//        Map<String, Object> map = new TreeMap<String, Object>();
//
//        for(int i = 0; i < serieses.size(); i++){
//            String first_char = serieses.get(i).getFirst_char();
//            String series = serieses.get(i).getSeries();
//            int series_id = serieses.get(i).getSeries_id();
//
//            if(!map.containsKey("first_char")){
//                List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
//                Map<String , Object> in_map = new HashMap<String, Object>();
//                in_map.put("series",series);
//                in_map.put("series_id", series_id);
//                list.add(in_map);
//                map.put("first_char", first_char);
//                map.put("series", list);
//
//            }else {
//                List<Map<String , Object>> list = (List<Map<String, Object>>) map.get("series");
//                Map<String , Object> in_map = new HashMap<String, Object>();
//                in_map.put("series",series);
//                in_map.put("series_id", series_id);
//                list.add(in_map);
//            }
//        }
//
//        return map;
//    }

    @Override
    public List<Object> getType(String language, int brand_id, int series_id) {
        List<TypeDTO> types = indexMapper.getType(language, brand_id, series_id);
        List<Object> list = new ArrayList<Object>();
        Map<String, Object> out_map = new HashMap<String, Object>();

        for(int i = 0; i < types.size(); i++){
            String year = types.get(i).getYear();
            String tp = types.get(i).getType();
            int type_id = types.get(i).getType_id();

            if(i==0){
                out_map = new HashMap<String, Object>();
                Map<String, Object> in_map = new HashMap<String, Object>();
                in_map.put("type",tp);
                in_map.put("type_id", type_id);
                List<Object> car_types = new ArrayList<Object>();
                car_types.add(in_map);
                out_map.put("year", year);
                out_map.put("car_types", car_types);
            }else if(!year.equals(out_map.get("year"))){
                list.add(out_map);
                out_map = new HashMap<String, Object>();
                Map<String, Object> in_map = new HashMap<String, Object>();
                in_map.put("type",tp);
                in_map.put("type_id", type_id);
                List<Object> car_types = new ArrayList<Object>();
                car_types.add(in_map);
                out_map.put("year", year);
                out_map.put("car_types", car_types);

            }else {
                List<Object> car_types = (List<Object>) out_map.get("car_types");
                Map<String, Object> in_map = new HashMap<String, Object>();
                in_map.put("type",tp);
                in_map.put("type_id", type_id);
                car_types.add(in_map);
            }
        }

        list.add(out_map);
        return list;
    }

    public void getTypeMessage(String language, int type_id){
        indexMapper.getTypeMessage(language, type_id);
    }

    @Override
    public List<HotIndexDTO> getHotWord(String language) {
        return indexMapper.getHotWord(language);

//        Map<String, String> map = new HashMap<String,String>();
//        for(int i = 0; i < hotwords.size(); i ++){
//            String hot_word = hotwords.get(i).getHot_word();
//            String field = hotwords.get(i).getField();
//            map.put(hot_word, field);
//        }
//
//        return map;
    }


    @Override
    public List<HotBrandDTO> getHotBrand(String language) {
        return indexMapper.getHotBrand(language);

//        Map<String, List<String>> map = new HashMap<String, List<String>>();
//        List<String> list = new ArrayList<String>();
//        for(int i = 0; i < hotwords.size(); i ++){
//            String hot_word = hotwords.get(i).getHot_word();
//            list.add(hot_word);
//        }
//        map.put("hot_brand", list);
//
//        return map;
    }


}
