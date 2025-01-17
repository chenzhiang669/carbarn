package com.carbarn.inter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.dto.cars.CarsOfUsersDTO;
import com.carbarn.inter.pojo.dto.cars.FirstPageCarsDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import com.carbarn.inter.service.CarsService;
import com.carbarn.inter.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carbarn/cars")
public class CarsController {

    @Autowired
    private CarsService carsService;

    @GetMapping("")
    public List<FirstPageCarsDTO> getCars() {
        return carsService.getCars();
    }

    @PostMapping("/search")
    public AjaxResult searchCars(@RequestHeader(name = "language", required = true) String language,
                                 @RequestBody SearchCarsDTO searchCarsDTO) {
        searchCarsDTO.setPageStart((searchCarsDTO.getPageNo() - 1) * searchCarsDTO.getPageSize());

        System.out.println(searchCarsDTO.toString());
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

        List<FirstPageCarsDTO> result = carsService.searchCars(searchCarsDTO);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        for(FirstPageCarsDTO dto:result){
            System.out.println(dto);
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

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
        JSONObject json_body = JSON.parseObject(body);
        if (!json_body.containsKey("vin")) {
            return AjaxResult.error("Missing required parameter: vin");
        }

        String vin = json_body.getString("vin");
        Map<String, Object> map = carsService.getByVin(vin);
        if (map.containsKey("0")) {
            return AjaxResult.error("该车架号已经上传过，请不要重复上传");
        } else if (map.containsKey("1")) {
            return AjaxResult.error("未找到该车架号");
        } else {
            return AjaxResult.success("成功找到车架号", map);
        }
    }


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

}
