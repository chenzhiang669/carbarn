package com.carbarn.inter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.FilesMapper;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.pojo.Files;
import com.carbarn.inter.service.FilesService;
import com.carbarn.inter.utils.Utils;
import com.carbarn.inter.utils.alibaba.ChepaiOcr;
import com.carbarn.inter.utils.alibaba.PlateMasking;
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

    @Autowired
    private ParamsMapper paramsMapper;


    private byte[] masking(byte[] bytes, String picture_type){
        String param_vin_ocr = paramsMapper.getValue(ParamKeys.param_chepai_ocr);
        JSONObject json = JSON.parseObject(param_vin_ocr);
        String host = json.getString("host");
        String path = json.getString("path");
        String appcode = json.getString("appcode");

        String result = ChepaiOcr.chepaiORC(host, path, appcode, bytes);
        JSONObject result_json = JSON.parseObject(result);
        if(result_json.containsKey("plates")){
            JSONArray plates = result_json.getJSONArray("plates");
            for(int i = 0 ;i<plates.size();i++){
                JSONObject plate = plates.getJSONObject(i);
                if(plate.containsKey("roi")){
                    JSONObject roi = plate.getJSONObject("roi");
                    int w = roi.getInteger("w");
                    int h = roi.getInteger("h");
                    int x = roi.getInteger("x");
                    int y = roi.getInteger("y");

                    if(x - 20 >= 0){
                        x = x -20;
                    }

                    if(y - 20 >= 0){
                        y = y - 20;
                    }

                    w = (int) (w * 1.8);
                    h = (int) (h * 2.8);

                    return PlateMasking.masking(x,y,w,h,picture_type,bytes);
                }
            }
        }

        return null;
    }
    @Override
    public String insertFiles(String type, MultipartFile file) {

        try {
            byte[] bytes = file.getBytes();
            String fileName = Utils.getRandomChar(10);
            long timestamp = System.currentTimeMillis();
            String[] fileinfos = file.getOriginalFilename().split("\\.");
            int length = fileinfos.length;
            String key = "";

            byte[] masking = null;
            try{
                if("car_picture".equals(type)){
                    if(length >= 1){
                        String fileType = fileinfos[length - 1];
                        masking = masking(bytes, fileType);
                    }
                }
            }catch (Exception e){
                masking = null;
            }



            if(length > 1){
                String fileType = fileinfos[length - 1];
                key = basePath + type + "/" + fileName + timestamp + "." + fileType;
            }else{
                key = basePath + type + "/" + fileName + timestamp;
            }

            String url = null;
            if(masking != null){
                url = QiniuyunUtils.uploadFile(masking, key);
            }else{
                url = QiniuyunUtils.uploadFile(bytes, key);
            }



            if(url == null){
                return null;
            }else {
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
