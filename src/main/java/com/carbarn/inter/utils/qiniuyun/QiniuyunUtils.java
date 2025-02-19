package com.carbarn.inter.utils.qiniuyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class QiniuyunUtils {
    private static String accessKey = "PqXya-MIqpVmW8og93Bp2JF6r50k2D5saTGBQEU5";
    private static String secretKey = "Nb2_0yeVGO7qfrEsM0PphxKsIHjywL6JVMEusyJ6";
    private static String bucket = "carbarn";
//    private static String domain = "http://sovo2nxf7.hd-bkt.clouddn.com/";

    private static String domain = "http://image.chechuhai.top/";
    private static Configuration cfg = new Configuration(Region.region0());
    private static UploadManager uploadManager = new UploadManager(cfg);

    public static String uploadFile(byte[] bytes, String key){

        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket, key); //覆盖上传
            Response response = uploadManager.put(bytes, key, upToken);
            System.out.println(response.bodyString());
            JSONObject json = JSON.parseObject(response.bodyString());
            if(json.containsKey("key")){
                String return_key = json.getString("key");
                String url = domain + return_key;
                return url;
            }else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        String localFilePath = "D:\\abcdefghijklmn.jpg";
        byte[] fileContent = Files.readAllBytes(Paths.get(localFilePath));
        String key = "carbarn/domain/test/abcdefghijklmnopqrst.jpg";
        String url = QiniuyunUtils.uploadFile(fileContent, key);
        System.out.println(url);
    }
}
