package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.CarTypePOJO;
import com.carbarn.inter.pojo.dto.cars.index.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ParamsMapper {

   String getValue(@Param("key") String key);

}
