package com.carbarn.inter.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TranslationDescriptionMapper {
    void insertNewTranslationDescription(@Param("link_id") long link_id,
                                         @Param("link_table") String link_table,
                                         @Param("link_field") String link_field,
                                         @Param("language") String language,
                                         @Param("value") String value);

    String getValue(@Param("link_id") long link_id,
                    @Param("link_table") String link_table,
                    @Param("link_field") String link_field,
                    @Param("language") String language);
}
