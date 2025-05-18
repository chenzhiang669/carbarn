package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.async.TypeDetailsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AsyncMapper {
    TypeDetailsDTO getTypeDetails(@Param("type_id") int type_id,
                                    @Param("language") String language);

    void updateTypeDetails(@Param("type_id") int type_id,
                           @Param("language") String language,
                           @Param("details") String details);
}
