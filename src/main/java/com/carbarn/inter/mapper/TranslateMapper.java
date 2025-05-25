package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.translate.TranslatePOJO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TranslateMapper {

    List<TranslatePOJO> getTranslateData(@Param("language") String language);
}
