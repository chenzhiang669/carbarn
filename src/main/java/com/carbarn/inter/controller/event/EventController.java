package com.carbarn.inter.controller.event;

import cn.dev33.satoken.stp.StpUtil;
import com.carbarn.inter.pojo.event.EventPojo;
import com.carbarn.inter.pojo.event.EventType;
import com.carbarn.inter.service.EventService;
import com.carbarn.inter.utils.AjaxResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "埋点服务")
@RestController
@RequestMapping("/carbarn/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/tracking")
    public AjaxResult tracking(@RequestBody EventPojo eventPojo) {
        if(eventPojo.getEvent_type() == EventType.VIEW_EVENT){
            eventService.updateViewCount(eventPojo);
        }else if(eventPojo.getEvent_type() == EventType.CONTACT_EVENT){
            eventService.updateContactCount(eventPojo);
        }else if(eventPojo.getEvent_type() == EventType.CARSEEK_VIEW_EVENT){
            eventService.insertCarSeekCount(eventPojo);
        }else if(eventPojo.getEvent_type() == EventType.USER_COLLECT_EVENT){
            String user_id = (String) StpUtil.getLoginId();
            eventPojo.setUser_id(Long.valueOf(user_id));
            eventService.insertUserCollect(eventPojo);
        }else if(eventPojo.getEvent_type() == EventType.USER_REMOVE_COLLECT_EVENT){
            String user_id = (String) StpUtil.getLoginId();
            eventPojo.setUser_id(Long.valueOf(user_id));
            eventService.deleteUserCollect(eventPojo);
        }else if(eventPojo.getEvent_type() == EventType.CAR_VIEW_EVENT){
            eventService.insertCarCount(eventPojo);
        }else{
            AjaxResult.error("there is no event_type: " + eventPojo.getEvent_type());
        }

        return AjaxResult.success("success on dealing event_type: " + eventPojo.getEvent_type());
    }
}
