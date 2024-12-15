package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.Cars;
import com.carbarn.inter.pojo.dto.cars.CarsOfUsersDTO;
import com.carbarn.inter.pojo.dto.cars.FirstPageCarsDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarsMapper {

    List<FirstPageCarsDTO> getCars();

    List<FirstPageCarsDTO> searchCars(SearchCarsDTO searchCarsDTO);

    Cars getCarsByID(long carid);

    CarsOfUsersDTO getCarsOfUsers(long userid);

    void uploadCar(Cars cars);

    void updateCarPictures(Cars cars);

}
