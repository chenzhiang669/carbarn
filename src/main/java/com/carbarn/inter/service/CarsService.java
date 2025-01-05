package com.carbarn.inter.service;

import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.dto.cars.CarsOfUsersDTO;
import com.carbarn.inter.pojo.dto.cars.FirstPageCarsDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import com.carbarn.inter.utils.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CarsService {
    List<FirstPageCarsDTO> getCars();

    List<FirstPageCarsDTO> searchCars(SearchCarsDTO searchCarsDTO);

    Map<String, Object> getCarsByID(String language, long carid);

    CarsOfUsersDTO getCarsOfUsers(long userid);


   Map<String, Object> getByVin(String vin);

    Map<String, Object> fillMessage(String language, int type_id);

    AjaxResult insertNewCar(CarsPOJO carsPOJO);
}
