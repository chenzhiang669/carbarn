package com.carbarn.inter.service;


import com.carbarn.inter.utils.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

public interface OcrService {

    AjaxResult vinOcr(MultipartFile file);
}
