package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.firstpage.FirstPageHotCarsDTO;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.pojo.user.pojo.UserViewCountPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@Configuration
//@MapperScan("com.carbarn.inter.mapper")
@Mapper
public interface EventMapper {
    void insertViewCount(@Param("user_id") long user_id,
                         @Param("dt") String dt);

    void insertViewedCount(@Param("user_id") long user_id,
                         @Param("dt") String dt);

    void insertContactCount(@Param("user_id") long user_id,
                           @Param("dt") String dt);

    void insertContactedCount(@Param("user_id") long user_id,
                            @Param("dt") String dt);

    void insertCarSeekCount(@Param("carseek_id") long carseek_id,
                              @Param("dt") String dt);

    void insertCarCount(@Param("car_id") long car_id,
                            @Param("dt") String dt);

    void insertUserCollect(@Param("user_id") long user_id,
                           @Param("car_id") long car_id);

    void deleteUserCollect(@Param("user_id") long user_id,
                           @Param("car_id") long car_id);

    int isLike(@Param("user_id") long user_id,
                   @Param("car_id") long car_id);

    List<FirstPageHotCarsDTO> getHotCars(@Param("language") String language,
                                         @Param("from_date") String from_date);
}
