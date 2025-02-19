package com.carbarn.inter.service.impl;

import com.carbarn.inter.mapper.FeedBackMapper;
import com.carbarn.inter.mapper.LanguageMapper;
import com.carbarn.inter.pojo.feedback.FeedBackDTO;
import com.carbarn.inter.pojo.language.LanguageDTO;
import com.carbarn.inter.service.FeedBackService;
import com.carbarn.inter.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedBackServiceImpl implements FeedBackService {

    @Autowired
    private FeedBackMapper feedBackMapper;

    @Override
    public void insertFeedBack(FeedBackDTO feedBackDTO) {
        feedBackMapper.insertFeedBack(feedBackDTO);
    }
}
