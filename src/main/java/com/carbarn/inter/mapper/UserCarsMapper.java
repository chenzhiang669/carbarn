package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.dto.cars.CarsOfUsersDTO;
import com.carbarn.inter.pojo.dto.cars.FirstPageCarsDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import com.carbarn.inter.pojo.usercar.StateCountPOJO;
import com.carbarn.inter.pojo.usercar.UserCarPOJO;
import com.carbarn.inter.pojo.vin.VinPOJO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserCarsMapper {

    void updateCarState(@Param("state") int state,
                        @Param("carid") long carid);

    List<UserCarPOJO> selectUserCars(@Param("language") String language,
                                     @Param("user_id") long user_id,
                                     @Param("state") long state,
                                     @Param("keywords") String keywords);


    List<StateCountPOJO> selectStateCount(@Param("language") String language,
                                          @Param("user_id") long user_id,
                                          @Param("keywords") String keywords);
}
