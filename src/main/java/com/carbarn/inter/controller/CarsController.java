package com.carbarn.inter.controller;

import com.carbarn.inter.pojo.Cars;
import com.carbarn.inter.pojo.dto.cars.CarsOfUsersDTO;
import com.carbarn.inter.pojo.dto.cars.FirstPageCarsDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import com.carbarn.inter.service.CarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/carbarn/cars")
public class CarsController {

    @Autowired
    private CarsService carsService;

    @GetMapping("")
    public List<FirstPageCarsDTO> getCars(){
        return carsService.getCars();
    }

    @PostMapping("/search")
    public List<FirstPageCarsDTO> searchCars(@RequestBody SearchCarsDTO searchCarsDTO){
        return carsService.searchCars(searchCarsDTO);
    }

    @GetMapping("/details")
    public Cars getCarsByID(@RequestParam(name = "carid") long carid){
        return carsService.getCarsByID(carid);
    }

    @GetMapping("/users")
    public CarsOfUsersDTO getCarsOfUsers(@RequestParam(name = "userid") long userid){
        return carsService.getCarsOfUsers(userid);
    }

    @PostMapping("/upload")
    public void uploadCar(@ModelAttribute Cars cars,
                          @RequestParam(value = "head_picture", required = true) MultipartFile head_picture,
                          @RequestParam(value = "pictures", required = true) MultipartFile[] pictures,
                          @RequestParam(value = "vrc_file", required = false) MultipartFile vrc_file){
        carsService.uploadCar(cars,head_picture,pictures,vrc_file);
    }

}
