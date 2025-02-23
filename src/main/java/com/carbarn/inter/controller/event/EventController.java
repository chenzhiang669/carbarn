package com.carbarn.inter.controller.event;

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
        }

        return AjaxResult.success("埋点事件更新成功");
    }
}
