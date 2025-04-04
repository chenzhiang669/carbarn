package com.carbarn.contract.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.carbarn.contract.pojo.ContractPOJO;
import com.carbarn.contract.pojo.dto.ContractFullMessageDTO;
import com.carbarn.contract.pojo.dto.SearchContractDTO;
import com.carbarn.contract.pojo.dto.UserContractDTO;
import com.carbarn.contract.service.ContractService;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.service.UserService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.sms.SendSms;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "合同服务")
@RestController
@RequestMapping("/carbarn/contract")
public class ContractController {
    @Autowired
    private ContractService contractService;

    @PostMapping("/createNewContract")
    public AjaxResult createNewContract(@RequestHeader(name = "language", required = true) String language,
                                        @RequestBody ContractPOJO contractPOJO) {
        //TODO 1、通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        return contractService.createNewContract(language, contractPOJO);
    }


    @PostMapping("/updateContract")
    public AjaxResult updateContract(@RequestHeader(name = "language", required = true) String language,
                                        @RequestBody ContractPOJO contractPOJO) {
        //TODO 通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        return contractService.updateContract(contractPOJO);
    }


    @PostMapping("/getContractInfo")
    public AjaxResult getContractInfo(@RequestHeader(name = "language", required = true) String language,
                                     @RequestBody ContractPOJO contractPOJO) {
        //TODO 通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        ContractFullMessageDTO result = contractService.getContractInfo(language, contractPOJO.getContract_id());
        return AjaxResult.success("获取合同信息成功", result);
    }


    //买家退回合同
    @PostMapping("/returnContract")
    public AjaxResult returnContract(@RequestHeader(name = "language", required = true) String language,
                                      @RequestBody ContractPOJO contractPOJO) {
        //TODO 通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        contractService.returnContract(language, contractPOJO.getContract_id());
        return AjaxResult.success("退回合同成功");
    }

    //搜索用户合同及状态
    @PostMapping("/userContracts")
    public AjaxResult userContracts(@RequestHeader(name = "language", required = true) String language,
                                     @RequestBody SearchContractDTO searchContractDTO) {
        //TODO 通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。

        int pageNo = searchContractDTO.getPageNo();
        int pageSize = searchContractDTO.getPageSize();
        if (pageNo < 1) {
            return AjaxResult.error("Missing required parameter: pageNo");
        }
        if (pageSize <= 0) {
            return AjaxResult.error("'pageSize' Must meet the conditions  pageSize > 0");
        } else {
            searchContractDTO.setPageStart((pageNo - 1) * pageSize);
        }

        searchContractDTO.setLanguage(language);
        List<UserContractDTO> userContractDTOS =  contractService.userContracts(searchContractDTO);
        return AjaxResult.success("获取用户合同信息成功", userContractDTOS);
    }
}
