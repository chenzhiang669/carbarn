package com.carbarn.inter.controller;

import com.carbarn.inter.service.OcrService;
import com.carbarn.inter.utils.AjaxResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "Ocr识别服务")
@RestController
@RequestMapping("/ocr")
public class OcrController {
    @Autowired
    private OcrService ocrService;

    @PostMapping("/vinocr")
    public AjaxResult uploadFiles(@RequestParam(name = "file", required = true) MultipartFile file){
        return ocrService.vinOcr(file);
    }
}
