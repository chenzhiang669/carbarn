package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.carseek.CarsSeekPOJO;
import com.carbarn.inter.pojo.carseek.ListUserCarsSeekSearchDTO;
import com.carbarn.inter.pojo.carseek.SearchCarsSeekDTO;
import com.carbarn.inter.pojo.carseek.UserCarsSeekDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CarsSeekMapper {

    void insertNewCarSeek(CarsSeekPOJO carsSeekPOJO);

    void updateCarSeekById(CarsSeekPOJO carsSeekPOJO);

    List<UserCarsSeekDTO> listUserCarSeek(ListUserCarsSeekSearchDTO listUserCarsSeekSearchDTO);

    List<UserCarsSeekDTO> searchCarSeek(SearchCarsSeekDTO searchCarsSeekDTO);
    long getCarSeekidByRandomString(String randomString);

    void deleteCarSeek(@Param("id") long id);

    List<UserCarsSeekDTO> listUserCarSeekExpire(ListUserCarsSeekSearchDTO listUserCarsSeekSearchDTO);

    List<UserCarsSeekDTO> listUserCarSeekUnExpire(ListUserCarsSeekSearchDTO listUserCarsSeekSearchDTO);

    UserCarsSeekDTO getCarSeekById(@Param("language") String language,
                                   @Param("id") long id);
}
