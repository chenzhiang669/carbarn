package com.carbarn.inter.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ParamsMapper {

   String getValue(@Param("key") String key);

}
