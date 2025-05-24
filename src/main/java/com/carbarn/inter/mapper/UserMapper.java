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
public interface UserMapper {
    User selectByUsername(@Param("username") String username);

    void signin(User user);

    @Select("select * from user where id = #{userId}")
    User selectById(Long userId);

    void signup(SignupUserDTO SignupUserDTO);

    void vipsignup(VipSignupUserDTO VipSignupUserDTO);

    void updateNickname(@Param("id") long id,
                        @Param("nickname") String nickname);

    void updateAvatar(@Param("id") long id,
                        @Param("avatar") String avatar);

    void updateCardealership(@Param("id") long id,
                        @Param("car_dealership") String car_dealership);

    void updateAddress(@Param("id") long id,
                      @Param("address") String address);


    UserPojo getUserInfoByPhoneNum(@Param("phone_num") String phone_num);


    UserPojo getUserInfoByID(@Param("id") long id);

    Boolean isPhoneNumExist(@Param("phone_num") String phone_num,
                            @Param("area_code") String area_code);

    void updateRole(@Param("user_id") long user_id, @Param("role") int role);

    void updateUserInfo(UserPojo userPojo);

    void updateLanguage(UserPojo userPojo);

    UserViewCountPojo userViewCount(@Param("user_id") long user_id);

    UserViewCountPojo userDtViewCount(@Param("user_id") long user_id,
                                      @Param("dt") String dt);

    void deRegister(@Param("user_id") long user_id);
}
