package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//@Configuration
//@MapperScan("com.carbarn.inter.mapper")
@Mapper
public interface UserMapper {
    User selectByUsername(@Param("username") String username);

    void signin(User user);
}
