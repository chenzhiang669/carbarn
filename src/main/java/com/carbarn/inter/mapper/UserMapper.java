package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//@Configuration
//@MapperScan("com.carbarn.inter.mapper")
@Mapper
public interface UserMapper {
    //    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(@Param("id") Long id);

    List<User> findAll();

    int insert(User user);

    int update(User user);

    int delete(@Param("id") Long id);
}
