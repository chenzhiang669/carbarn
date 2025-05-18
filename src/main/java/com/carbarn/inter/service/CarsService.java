package com.carbarn.inter.service;

import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.dto.cars.*;
import com.carbarn.inter.utils.AjaxResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CarsService {
    List<FirstPageCarsDTO> getCars();

    List<FirstPageCarsDTO> searchCars(SearchCarsDTO searchCarsDTO);

    Map<String, Object> getCarsByID(String language, long carid);

    CarsOfUsersDTO getCarsOfUsers(long userid);


//    Map<String, Object> getByVin(String vin);

    AjaxResult getByVin_new(String vin, int user_id);

    Map<String, Object> fillMessage(String language, int type_id);

    AjaxResult insertNewCar(CarsPOJO carsPOJO);

    AjaxResult uploadNewCar(CarsPOJO carsPOJO, int user_id);

    AjaxResult updateCar(CarsPOJO carsPOJO);

    List<FirstPageCarsDTO> searchCarsByKeywords(SearchCarsDTO searchCarsDTO);

    String getCarTypeDetails(int type_id, String language);

    String getCarName(CarNameDTO carNameDTO, String language);

    void updateCarState(OperateUpdateStateDTO operateUpdateStateDTO);

    AjaxResult getStateCars(OperateSearchCarsDTO operateSearchCarsDTO);
}
