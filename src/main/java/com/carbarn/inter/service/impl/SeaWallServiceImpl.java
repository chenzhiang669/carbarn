package com.carbarn.inter.service.impl;

import com.carbarn.inter.mapper.SeaWallMapper;
import com.carbarn.inter.pojo.seawall.SeaWallPOJO;
import com.carbarn.inter.pojo.seawall.SeaWallPageDTO;
import com.carbarn.inter.service.SeaWallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeaWallServiceImpl implements SeaWallService {
    @Autowired
    private SeaWallMapper seaWallMapper;


    @Override
    public List<SeaWallPOJO> getSeaWall(SeaWallPageDTO seaWallPageDTO) {
        return seaWallMapper.getSeaWall(seaWallPageDTO);
    }
}