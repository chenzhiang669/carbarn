package com.carbarn.inter.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.dto.cars.*;
import com.carbarn.inter.pojo.usercar.Constant;
import com.carbarn.inter.service.CarsService;
import com.carbarn.inter.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/carbarn/cars")
public class CarsController {

    @Autowired
    private CarsService carsService;

    @Autowired
    private ParamsMapper paramsMapper;

    @GetMapping("")
    public List<FirstPageCarsDTO> getCars() {
        return carsService.getCars();
    }

    @PostMapping("/search")
    public AjaxResult searchCars(@RequestHeader(name = "language", required = true) String language,
                                 @RequestBody SearchCarsDTO searchCarsDTO) {
        searchCarsDTO.setPageStart((searchCarsDTO.getPageNo() - 1) * searchCarsDTO.getPageSize());

        int pageNo = searchCarsDTO.getPageNo();
        int pageSize = searchCarsDTO.getPageSize();
        if (pageNo < 1) {
            return AjaxResult.error("Missing required parameter: pageNo");
        }
        if (pageSize <= 0) {
            return AjaxResult.error("'pageSize' Must meet the conditions  pageSize > 0");
        } else {
            searchCarsDTO.setPageStart((pageNo - 1) * pageSize);
        }

        String order_way = searchCarsDTO.getOrder_way();
        if (!"asc".equals(order_way) && !"desc".equals(order_way)) {
            return AjaxResult.error("order_way should be one of [asc, desc], but now is " + order_way);
        }

        searchCarsDTO.setLanguage(language);

        List<FirstPageCarsDTO> result = new ArrayList<FirstPageCarsDTO>();
        String keywords = searchCarsDTO.getKeywords();
        if(keywords != null && !"".equals(keywords)){
            result = carsService.searchCarsByKeywords(searchCarsDTO);
        }else{
           result = carsService.searchCars(searchCarsDTO);
        }

        AjaxResult ajaxResult = AjaxResult.success("搜索到相关数据", result);
        ajaxResult.put("pageNo", pageNo);
        ajaxResult.put("pageSize", pageSize);
        return ajaxResult;


    }

    @GetMapping("/details")
    public AjaxResult getCarsByID(@RequestHeader(name = "language", required = true) String language,
                                @RequestParam(name = "carid") long carid) {

        Map<String, Object> map =  carsService.getCarsByID(language, carid);
        if(map.size() == 0){
            return AjaxResult.error("获取汽车明细失败，carid: " + carid);
        }else{
            return AjaxResult.success("获取明细成功", map);
        }
    }

    @GetMapping("/users")
    public CarsOfUsersDTO getCarsOfUsers(@RequestParam(name = "userid") long userid) {
        return carsService.getCarsOfUsers(userid);
    }


    @PostMapping("/upload/vin")
    public AjaxResult uploadCar(@RequestHeader(name = "language", required = true) String language,
                                @RequestBody String body) {

        String user_id = (String) StpUtil.getLoginId();

        JSONObject json_body = JSON.parseObject(body);
        if (!json_body.containsKey("vin")) {
            return AjaxResult.error("Missing required parameter: vin");
        }

        String vin = json_body.getString("vin");
        return carsService.getByVin_new(vin, Integer.valueOf(user_id));
//        if (map.containsKey("0")) {
//            return AjaxResult.error("该车架号已经上传过，请不要重复上传");
//        } else if (map.containsKey("1")) {
//            return AjaxResult.error("未找到该车架号");
//        } else {
//            return AjaxResult.success("成功找到车架号", map);
//        }
    }


//    @PostMapping("/upload/vin_new")
//    public AjaxResult uploadCar_new(@RequestHeader(name = "language", required = true) String language,
//                                @RequestBody String body) {
//        JSONObject json_body = JSON.parseObject(body);
//        if (!json_body.containsKey("vin")) {
//            return AjaxResult.error("Missing required parameter: vin");
//        }
//
//        String vin = json_body.getString("vin");
//        Map<String, Object> map = carsService.getByVin_new(vin);
//        if (map.containsKey("0")) {
//            return AjaxResult.error("该车架号已经上传过，请不要重复上传");
//        } else if (map.containsKey("1")) {
//            return AjaxResult.error("未找到该车架号");
//        } else {
//            return AjaxResult.success("成功找到车架号", map);
//        }
//    }


    @PostMapping("/upload/fillmessage")
    public AjaxResult fillMessage(@RequestHeader(name = "language", required = true) String language,
                                  @RequestBody String body) {
        JSONObject json_body = JSON.parseObject(body);
        if (!json_body.containsKey("type_id")) {
            return AjaxResult.error("Missing required parameter: type_id");
        }


        try {
            int type_id = json_body.getInteger("type_id");

            Map<String, Object> map = carsService.fillMessage(language, type_id);
            if (map.size() == 0) {
                return AjaxResult.error("获取数据异常", map);
            } else {
                return AjaxResult.success("正常获取数据", map);
            }
        } catch (NumberFormatException e) {
            return AjaxResult.error("type_id should be number, but now is " + json_body.getString("type_id"));
        }

    }

    @PostMapping("/upload")
    public AjaxResult insertNewCar(@RequestHeader(name = "language", required = true) String language,
                                   @RequestBody CarsPOJO carsPOJO) {
        if (carsPOJO.getUser_id() == 0) {
            return AjaxResult.error("Missing required parameter: user_id");
        } else if (carsPOJO.getBrand_id() == 0) {
            return AjaxResult.error("Missing required parameter: brand_id");
        }
        if (carsPOJO.getSeries_id() == 0) {
            return AjaxResult.error("Missing required parameter: series_id");
        }
        if (carsPOJO.getType_id() == 0) {
            return AjaxResult.error("Missing required parameter: type_id");
        }
        if (carsPOJO.getVin() == null) {
            return AjaxResult.error("Missing required parameter: vin");
        }


        return carsService.insertNewCar(carsPOJO);
    }


    @PostMapping("/uploadNewCar")
    public AjaxResult uploadNewCar(@RequestHeader(name = "language", required = true) String language,
                                   @RequestBody CarsPOJO carsPOJO) {
//        if (carsPOJO.getUser_id() == -1) {
//            return AjaxResult.error("Missing required parameter: user_id");
//        }

        String user_id = (String) StpUtil.getLoginId();
        carsPOJO.setUser_id(Integer.valueOf(user_id));

        return carsService.uploadNewCar(carsPOJO, Integer.valueOf(user_id));
    }


    @PostMapping("/updateCar")
    public AjaxResult updateCar(@RequestHeader(name = "language", required = true) String language,
                                   @RequestBody CarsPOJO carsPOJO) {
        if (carsPOJO.getUser_id() == -1) {
            return AjaxResult.error("Missing required parameter: user_id");
        }
//        if (carsPOJO.getBrand_id() == 0) {
//            return AjaxResult.error("Missing required parameter: brand_id");
//        }
//        if (carsPOJO.getSeries_id() == 0) {
//            return AjaxResult.error("Missing required parameter: series_id");
//        }
//        if (carsPOJO.getType_id() == 0) {
//            return AjaxResult.error("Missing required parameter: type_id");
//        }
        if (carsPOJO.getId() == 0) {
            return AjaxResult.error("Missing required parameter: id");
        }

        return carsService.updateCar(carsPOJO);
    }


    @GetMapping("/carTypeDetails")
    public AjaxResult getCarTypeDetails(@RequestHeader(name = "language", required = true) String language,
                                  @RequestParam(name = "type_id") int type_id) {

        String carTypeDetails = carsService.getCarTypeDetails(type_id, language);
        if(carTypeDetails == null){
            return AjaxResult.success("[]");
        }else{
            JSONArray response = JSON.parseArray(carTypeDetails);
            return AjaxResult.success(response);
        }
    }


    @PostMapping("/getCarName")
    public AjaxResult getCarName(@RequestHeader(name = "language", required = true) String language,
                                 @RequestBody CarNameDTO carNameDTO) {

        if(carNameDTO.getBrand() == null
        || carNameDTO.getSeries() == null
        || carNameDTO.getType() == null){
            return AjaxResult.error("Missing required parameter: [brand, series, type]");
        }
        String name = carsService.getCarName(carNameDTO, language);
        carNameDTO.setName(name);
        return AjaxResult.success("getCarName success", carNameDTO);
    }

    @PostMapping("/operate/updateCarsState")
    public AjaxResult updateCarState(@RequestBody OperateUpdateStateDTO operateUpdateStateDTO) {

        if(!is_whitelist_user()){
            return AjaxResult.error("The current user does not have permission to perform this operation.");
        }

        if(operateUpdateStateDTO.getCarid() == 0
        || operateUpdateStateDTO.getState() == -1){
            return AjaxResult.error("Missing required parameter: [carid, state]");
        }

        if(operateUpdateStateDTO.getState() == Constant.STATE_ON_UNREVIEW
            && operateUpdateStateDTO.getReason() == null){
            return AjaxResult.error("Missing required parameter: [reason]");
        }
        carsService.updateCarState(operateUpdateStateDTO);

        return AjaxResult.success("update cars state successful");
    }

    @PostMapping("/operate/getStateCars")
    public AjaxResult getStateCars(@RequestBody OperateSearchCarsDTO operateSearchCarsDTO) {
        if(!is_whitelist_user()){
            return AjaxResult.error("The current user does not have permission to perform this operation.");
        }


        operateSearchCarsDTO.setPageStart((operateSearchCarsDTO.getPageNo() - 1) * operateSearchCarsDTO.getPageSize());
        operateSearchCarsDTO.setLanguage("zh");
        int pageNo = operateSearchCarsDTO.getPageNo();
        int pageSize = operateSearchCarsDTO.getPageSize();
        if (pageNo < 1) {
            return AjaxResult.error("Missing required parameter: pageNo");
        }
        if (pageSize <= 0) {
            return AjaxResult.error("'pageSize' Must meet the conditions  pageSize > 0");
        } else {
            operateSearchCarsDTO.setPageStart((pageNo - 1) * pageSize);
        }

        return carsService.getStateCars(operateSearchCarsDTO);
    }

    private boolean is_whitelist_user(){
        String operate_whitelist = paramsMapper.getValue(ParamKeys.param_operate_whitelist);
        JSONArray operate_whitelist_array = JSON.parseArray(operate_whitelist);

        Set<Long> whitelist = operate_whitelist_array.stream().map(x -> {
            return Long.valueOf(x.toString());
        }).collect(Collectors.toSet());

        String user_id = (String) StpUtil.getLoginId();
        long user_id_long = Long.valueOf(user_id);

        return whitelist.contains(user_id_long);
    }
}
