package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.area.AreaPOJO;
import com.carbarn.inter.pojo.seafreight.PortPOJO;
import com.carbarn.inter.pojo.seafreight.ShippingLinePOJO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeaFreightMapper {
    List<PortPOJO> getPortInfos(@Param("keywords") String keywords);

    List<ShippingLinePOJO> getShippingLine();
}
