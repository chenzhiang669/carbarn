package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.language.LanguageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LanguageMapper {

    List<LanguageDTO> getLanguage();
}
