package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.seawall.SeaWallPOJO;
import com.carbarn.inter.pojo.seawall.SeaWallPageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//@Configuration
//@MapperScan("com.carbarn.inter.mapper")
@Mapper
public interface SeaWallMapper {

    List<SeaWallPOJO> getSeaWall(@Param("seaWallPageDTO")SeaWallPageDTO seaWallPageDTO);

    void insertNewSeaWall(SeaWallPOJO seaWallPOJO);

    void deleteSeaWall(@Param("id") int id);
}
