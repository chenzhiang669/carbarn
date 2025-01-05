package com.carbarn.inter.controller;

import com.carbarn.inter.service.FilesService;
import com.carbarn.inter.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/carbarn/file")
public class FilesController {

    @Autowired
    private FilesService pictureService;


    @PostMapping("/upload")
    public AjaxResult uploadFiles(@RequestParam(name = "type", required = true) String type,
                                    @RequestParam(name = "file", required = true) MultipartFile file){

        String url = pictureService.insertFiles(type ,file);
        if(url == null){
            return AjaxResult.error("文件上传失败");
        }else{
            Map<String, String> map = new HashMap<String, String>();
            map.put("url", url);
            return AjaxResult.success("文件上传成功", map);
        }
    }


    @PostMapping("/delete")
    public AjaxResult  deleteFlies(@RequestParam(name = "url", required = true) String url){
        boolean bool = pictureService.deleteFiles(url);
        if(bool){
            return AjaxResult.success("文件删除成功");
        }else {
            return AjaxResult.error("文件删除失败");
        }
    }

}
