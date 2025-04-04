package com.carbarn.inter.service;


import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.seawall.SeaWallPOJO;
import com.carbarn.inter.pojo.seawall.SeaWallPageDTO;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.utils.AjaxResult;

import java.util.List;

public interface SeaWallService {

    List<SeaWallPOJO> getSeaWall(SeaWallPageDTO seaWallPageDTO);

}
