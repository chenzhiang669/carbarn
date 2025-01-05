package com.carbarn.inter.service;

import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    String insertFiles(String type, MultipartFile file);


    boolean deleteFiles(String url);
}
