package com.carbarn.inter.service;

import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.pojo.dto.cars.CarsOfUsersDTO;
import com.carbarn.inter.pojo.dto.cars.FirstPageCarsDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import com.carbarn.inter.pojo.usercar.StateCountPOJO;
import com.carbarn.inter.pojo.usercar.UserCarList;
import com.carbarn.inter.pojo.usercar.UserCarPOJO;
import com.carbarn.inter.pojo.usercar.UserLikeCarSearchDTO;
import com.carbarn.inter.utils.AjaxResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserCarsService {
    AjaxResult deleteCars(int carid, long user_id);

    AjaxResult unreviewCars(int carid, long user_id);

    AjaxResult delist(int carid, long user_id);

    AjaxResult selectUserCars(long user_id, int state, String keywords);


    AjaxResult selectStateCount(long user_id, String keywords);

    AjaxResult edit(int carid, long user_id);

    AjaxResult selectUserLikeCars(UserLikeCarSearchDTO userLikeCarSearchDTO);
}
