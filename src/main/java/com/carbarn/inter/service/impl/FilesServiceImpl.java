package com.carbarn.inter.service.impl;

import com.carbarn.inter.mapper.FilesMapper;
import com.carbarn.inter.pojo.Files;
import com.carbarn.inter.service.FilesService;
import com.carbarn.inter.utils.Utils;
import com.carbarn.inter.utils.qiniuyun.QiniuyunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FilesServiceImpl implements FilesService {

    public static String basePath = "carbarn/files/";
    @Autowired
    private FilesMapper pictureMapper;


    @Override
    public String insertFiles(String type, MultipartFile file) {

        try {
            byte[] bytes = file.getBytes();
            String fileName = Utils.getRandomChar(10);
            long timestamp = System.currentTimeMillis();

            String[] fileinfos = file.getOriginalFilename().split("\\.");
            int length = fileinfos.length;
            String key = "";
            if(length > 1){
                String fileType = fileinfos[length - 1];
                key = basePath + type + "/" + fileName + timestamp + "." + fileType;
            }else{
                key = basePath + type + "/" + fileName + timestamp;
            }

            String url = QiniuyunUtils.uploadFile(bytes, key);

            if(url == null){
                return null;
            }else {
                System.out.println("上传文件成功：" + url);
                Files picture = new Files();
                picture.setUrl(url);
                picture.setIs_delete(0);
                picture.setType(type);
                pictureMapper.insertFiles(picture);

                return url;
            }

        } catch (IOException e) {
            return null;
        }


    }

    @Override
    public boolean deleteFiles(String url) {
        try{
            pictureMapper.deleteFiles(url);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
