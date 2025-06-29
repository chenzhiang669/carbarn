package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.area.AreaPOJO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AreaMapper {
    List<AreaPOJO> getProvince();

    List<AreaPOJO> getCity(@Param("code") String code);
}
