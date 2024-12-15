package com.carbarn.inter.service.impl;

import com.carbarn.inter.config.CarbarnConfig;
import com.carbarn.inter.mapper.CarsMapper;
import com.carbarn.inter.pojo.Cars;
import com.carbarn.inter.pojo.dto.cars.CarsOfUsersDTO;
import com.carbarn.inter.pojo.dto.cars.FirstPageCarsDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import com.carbarn.inter.service.CarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class CarsServiceImpl implements CarsService {

    @Autowired
    private CarsMapper carsMapper;
    @Autowired
    private CarbarnConfig carbarnConfig;

    @Override
    public List<FirstPageCarsDTO> getCars() {
        return carsMapper.getCars();
    }

    @Override
    public List<FirstPageCarsDTO> searchCars(SearchCarsDTO searchCarsDTO) {
        return carsMapper.searchCars(searchCarsDTO);
    }

    @Override
    public Cars getCarsByID(long carid) {
        return carsMapper.getCarsByID(carid);
    }

    @Override
    public CarsOfUsersDTO getCarsOfUsers(long userid) {
        return carsMapper.getCarsOfUsers(userid);
    }

    @Override
    public void uploadCar(Cars cars,
                          MultipartFile head_picture,
                          MultipartFile[] pictures,
                          MultipartFile vrc_file) {

        String baseDir = carbarnConfig.getPicturesDir() + "/" + cars.getId();

        File dir = new File(baseDir);
        if(!dir.exists()){
            dir.mkdirs();
        }

        try{
            if(vrc_file != null){
                byte[] bytes = vrc_file.getBytes();
                Path path = Paths.get(baseDir + "/vrc.jpg");
                Files.write(path, bytes);
                cars.setVrc("https//66666777888999/vrc.jpg");
            }

            if(head_picture != null){
                byte[] bytes = head_picture.getBytes();
                Path path = Paths.get(baseDir + "/head_picture.jpg");
                Files.write(path, bytes);
                cars.setHeader_picture("https//66666777888999/head_picture.jpg");
            }

            if(pictures != null && pictures.length > 0){
                for(int i = 0;i < pictures.length; i++){
                    MultipartFile picture = pictures[i];
                    byte[] b = picture.getBytes();
                    Path path1 = Paths.get(baseDir + "/pictures" + i + ".jpg");
                    Files.write(path1, b);
                }
                cars.setAll_pictures("https://23324123/picture1.jpg,https://23324123/picture2.jpg,https://23324123/picture3.jpg,https://23324123/picture4.jpg,");
            }

            carsMapper.updateCarPictures(cars);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
