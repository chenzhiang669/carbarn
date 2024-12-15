package com.carbarn.inter.service;

import com.carbarn.inter.pojo.Cars;
import com.carbarn.inter.pojo.dto.cars.CarsOfUsersDTO;
import com.carbarn.inter.pojo.dto.cars.FirstPageCarsDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarsService {
    List<FirstPageCarsDTO> getCars();

    List<FirstPageCarsDTO> searchCars(SearchCarsDTO searchCarsDTO);

    Cars getCarsByID(long carid);

    CarsOfUsersDTO getCarsOfUsers(long userid);


   void uploadCar(Cars cars,
                  MultipartFile head_picture,
                  MultipartFile[] pictures,
                  MultipartFile vrc_file);
}
