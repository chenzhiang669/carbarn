package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.dto.cars.CarsOfUsersDTO;
import com.carbarn.inter.pojo.dto.cars.FirstPageCarsDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CarsMapper {

    List<FirstPageCarsDTO> getCars();

    List<FirstPageCarsDTO> searchCars(SearchCarsDTO searchCarsDTO);

    CarsPOJO getCarsByID(long carid);

    CarsOfUsersDTO getCarsOfUsers(long userid);

    boolean existsByVin(String vin);

    void insertNewCar(CarsPOJO carsPOJO);

    List<FirstPageCarsDTO> searchCarsByKeywords(@Param("searchCarsDTO") SearchCarsDTO searchCarsDTO,
                                                @Param("brandIds") List<Integer> brandIds,
                                                @Param("seriesIds") List<Integer> seriesIds);

    String getCarTypeDetails(@Param("type_id") int type_id,
                             @Param("language") String language);
}
