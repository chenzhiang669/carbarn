package com.carbarn.inter.controller.test;

import com.carbarn.inter.pojo.async.TypeDetailsDTO;
import com.carbarn.inter.pojo.event.EventPojo;
import com.carbarn.inter.pojo.event.EventType;
import com.carbarn.inter.service.EventService;
import com.carbarn.inter.service.TestService;
import com.carbarn.inter.utils.AjaxResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试接口")
@RestController
@RequestMapping("/carbarn/test")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/testAsync")
    public AjaxResult tracking(@RequestBody TypeDetailsDTO typeDetailsDTO) {

        int type_id = typeDetailsDTO.getType_id();

        return testService.test(type_id);
    }
}
