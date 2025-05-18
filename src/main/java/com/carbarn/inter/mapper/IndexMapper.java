package com.carbarn.inter.mapper;

import com.carbarn.inter.pojo.CarTypePOJO;
import com.carbarn.inter.pojo.dto.cars.index.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IndexMapper {

    List<IndexDTO> getIndex(@Param("language") String language);
    List<BrandDTO> getBrand(@Param("language") String language);

    List<SeriesDTO> getSeries(@Param("language")String language, @Param("brand_id") int brand_id);

    List<TypeDTO> getType(@Param("language")String language, @Param("brand_id") int brand_id, @Param("series_id") int series_id);

    List<HotIndexDTO> getHotWord(@Param("language")String language);

    List<HotBrandDTO> getHotBrand(@Param("language")String language);

    String getBrandIdByBrand(@Param("language") String language,
                             @Param("brand") String brand);

    Boolean isBrandIdExisted(@Param("language") String language,
                            @Param("brand_id") long brand_id);

    Boolean isSeriesIdExisted(@Param("language") String language,
                             @Param("series_id") long series_id);

    Boolean isTypeIdExisted(@Param("language") String language,
                              @Param("type_id") long type_id);

    void insertNewBrand(@Param("logo") String logo,
                        @Param("brand") String brand,
                        @Param("first_char") String first_char,
                        @Param("language") String language);

    void updateNewBrand(@Param("logo") String logo,
                        @Param("brand") String brand,
                        @Param("brand_id") int brand_id,
                        @Param("first_char") String first_char,
                        @Param("language") String language);


    void updateNewSeries(@Param("brand_id") int brand_id,
                        @Param("series_id") int series_id,
                        @Param("first_char") String first_char,
                        @Param("language") String language,
                        @Param("series") String series);

    void updateNewType(CarTypePOJO carTypePOJO);

    String getSeriesIdBySeries(@Param("language") String language,
                               @Param("series") String series);

    void insertNewSeries(@Param("brand_id") int brand_id,
                         @Param("first_char") String first_char,
                         @Param("language") String language,
                         @Param("series") String series);

    String getTypeIdByType(@Param("language") String language,
                           @Param("type") String type);

    void insertNewType(CarTypePOJO carTypePOJO);

    CarTypePOJO getTypeDefaultValue(@Param("language") String language,
                                    @Param("type_id") int type_id);

    TypeMessageDTO getTypeMessage(@Param("language") String language,
                        @Param("type_id") int type_id);

    List<Integer> getBrandIdByKeywords(@Param("language") String language,
                                       @Param("brand") String keywords);

    List<Integer> getSeriesIdByKeywords(@Param("language") String language,
                                        @Param("series") String keywords);
}
