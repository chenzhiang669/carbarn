package com.carbarn.inter.service.impl;

import com.carbarn.inter.mapper.LanguageMapper;
import com.carbarn.inter.pojo.language.LanguageDTO;
import com.carbarn.inter.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    private LanguageMapper languageMapper;

    @Override
    public List<LanguageDTO> getLanguage() {
        return languageMapper.getLanguage();
    }
}
