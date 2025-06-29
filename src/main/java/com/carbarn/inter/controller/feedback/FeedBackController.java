package com.carbarn.inter.controller.feedback;

import com.carbarn.inter.pojo.feedback.FeedBackDTO;
import com.carbarn.inter.pojo.language.LanguageDTO;
import com.carbarn.inter.service.FeedBackService;
import com.carbarn.inter.service.LanguageService;
import com.carbarn.inter.utils.AjaxResult;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/carbarn")
public class FeedBackController {
    @Autowired
    private FeedBackService feedBackService;

    @PostMapping("/feedback")
    public AjaxResult feedback(@RequestBody FeedBackDTO feedBackDTO) {

        try{
            if(feedBackDTO.getDescription() == null){
                return AjaxResult.error("Missing required parameter 'description'");
            }else if(feedBackDTO.getUrl() == null){
                return AjaxResult.error("Missing required parameter 'url'");
            }
            feedBackService.insertFeedBack(feedBackDTO);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("问题反馈失败");
        }

        return AjaxResult.success("问题反馈成功");
    }
}
