package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.pojo.user.pojo.UserViewCountPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

}
