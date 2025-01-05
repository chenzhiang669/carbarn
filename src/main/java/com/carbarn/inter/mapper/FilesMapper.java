package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.Files;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FilesMapper {

    void insertFiles(Files files);

    void deleteFiles(@Param("url") String url);
}
