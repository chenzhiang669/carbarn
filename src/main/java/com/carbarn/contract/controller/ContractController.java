package com.carbarn.contract.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.carbarn.contract.contants.ContractState;
import com.carbarn.contract.pojo.ContractPOJO;
import com.carbarn.contract.pojo.dto.ContractFullMessageDTO;
import com.carbarn.contract.pojo.dto.SearchContractDTO;
import com.carbarn.contract.pojo.dto.UserContractDTO;
import com.carbarn.contract.service.ContractService;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.service.UserService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.sms.SendSms;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "合同服务")
@RestController
@RequestMapping("/carbarn/contract")
public class ContractController {
    @Autowired
    private ContractService contractService;

    @Autowired
    private ParamsMapper paramsMapper;

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

    @PostMapping("/deleteContract")
    public AjaxResult deleteContract(@RequestHeader(name = "language", required = true) String language,
                                     @RequestBody ContractPOJO contractPOJO) {
        //TODO 通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        String contract_id = contractPOJO.getContract_id();
        if(contract_id == null){
            return AjaxResult.error("error: Missing required parameter: 'contract_id'");
        }
        return contractService.deleteContract(contract_id);
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
    public AjaxResult userContratcs(@RequestHeader(name = "language", required = true) String language,
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

    @PostMapping("/updateBuyerState")
    public AjaxResult updateBuyerState(@RequestHeader(name = "language", required = true) String language,
                                    @RequestBody ContractPOJO contractPOJO) {
        //TODO 通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        String contract_id = contractPOJO.getContract_id();
        int buyer_state = contractPOJO.getBuyer_state();

        if(contract_id == null || buyer_state == 0){
            return AjaxResult.error("error: Missing required parameter: 'contract_id' or 'buyer_state'");
        }

        String operate_whitelist = paramsMapper.getValue(ParamKeys.param_operate_whitelist);
        JSONArray operate_whitelist_array = JSON.parseArray(operate_whitelist);
        Set<Long> whitelist = operate_whitelist_array.stream().map(x -> {
            return Long.valueOf(x.toString());
        }).collect(Collectors.toSet());

        String user_id = (String) StpUtil.getLoginId();
        long user_id_long = Long.valueOf(user_id);

        //只有白名单里面的人员才有操作的权限
        if(!whitelist.contains(user_id_long)){
            return AjaxResult.error("The current user does not have permission to perform this operation.");
        }



        try{
            contractService.updateBuyerState(contract_id, buyer_state);
            return AjaxResult.success("update buyerstate success");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("update buyerstate fail");
        }
    }

    @PostMapping("/updateSellerState")
    public AjaxResult updateSellerState(@RequestHeader(name = "language", required = true) String language,
                                       @RequestBody ContractPOJO contractPOJO) {
        //TODO 通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        String contract_id = contractPOJO.getContract_id();
        int seller_state = contractPOJO.getSeller_state();

        if(contract_id == null || seller_state == 0){
            return AjaxResult.error("error: Missing required parameter: 'contract_id' or 'seller_state'");
        }


        String operate_whitelist = paramsMapper.getValue(ParamKeys.param_operate_whitelist);
        JSONArray operate_whitelist_array = JSON.parseArray(operate_whitelist);
        Set<Long> whitelist = operate_whitelist_array.stream().map(x -> {
            return Long.valueOf(x.toString());
        }).collect(Collectors.toSet());

        String user_id = (String) StpUtil.getLoginId();
        long user_id_long = Long.valueOf(user_id);

        //只有白名单里面的人员才有操作的权限
        if(!whitelist.contains(user_id_long)){
            return AjaxResult.error("The current user does not have permission to perform this operation.");
        }

        try{
            contractService.updateSellerState(contract_id, seller_state);
            return AjaxResult.success("update sellerstate success");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("update sellerstate fail");
        }
    }

    @PostMapping("/operationStaffUploadContract")
    public AjaxResult operationStaffUploadContract(@RequestHeader(name = "language", required = true) String language,
                                        @RequestBody ContractPOJO contractPOJO) {
        //TODO 通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        String contract_id = contractPOJO.getContract_id();
        String operationContract = contractPOJO.getOperationContract();

        if(contract_id == null || operationContract == null){
            return AjaxResult.error("error: Missing required parameter: 'contract_id' or 'operationContract'");
        }

        String operate_whitelist = paramsMapper.getValue(ParamKeys.param_operate_whitelist);
        JSONArray operate_whitelist_array = JSON.parseArray(operate_whitelist);

        Set<Long> whitelist = operate_whitelist_array.stream().map(x -> {
            return Long.valueOf(x.toString());
        }).collect(Collectors.toSet());

        String user_id = (String) StpUtil.getLoginId();
        long user_id_long = Long.valueOf(user_id);

        //只有白名单里面的人员才有操作的权限
        if(!whitelist.contains(user_id_long)){
            return AjaxResult.error("The current user does not have permission to perform this operation.");
        }

        try{
            contractService.updateOperationContract(contractPOJO);
            return AjaxResult.success("upload contract success");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("upload contract fail");
        }
    }

    @PostMapping("/UserUploadContract")
    public AjaxResult UserUploadContract(@RequestHeader(name = "language", required = true) String language,
                                                   @RequestBody ContractPOJO contractPOJO) {
        //TODO 通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。

        String contract_id = contractPOJO.getContract_id();
        String userContract = contractPOJO.getUserContract();

        if(contract_id == null || userContract == null){
            return AjaxResult.error("error: Missing required parameter: 'contract_id' or 'userContract'");
        }

        try{
            contractService.updateUserContract(contractPOJO);
            contractService.updateBuyerState(contract_id, ContractState.buyer_state_platform_review_again); //用户合同上传成功后，将买家的状态修改为: 合同复审中
            return AjaxResult.success("upload contract success");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("upload contract fail");
        }
    }

    @PostMapping("/watingUploadcontract")
    public AjaxResult watingUploadcontract(@RequestHeader(name = "language", required = true) String language,
                                         @RequestBody ContractPOJO contractPOJO) {
        //TODO 通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        String contract_id = contractPOJO.getContract_id();

        if(contract_id == null){
            return AjaxResult.error("error: Missing required parameter: 'contract_id'");
        }

        try{
            contractService.updateBuyerState(contract_id, ContractState.buyer_state_platform_upload_contract); //用户下载合同成功后，前段调用，将买家的状态修改为: 合同复审中
            return AjaxResult.success("update buyerstate success");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("update buyerstate fail");
        }
    }

    @PostMapping("/getBuyerContractStateCount")
    public AjaxResult getBuyerContractStateCount(@RequestHeader(name = "language", required = true) String language,
                                           @RequestBody ContractPOJO contractPOJO) {
        long buyer_id = contractPOJO.getBuyer_id();

        if(buyer_id == 0){
            return AjaxResult.error("error: Missing required parameter: 'buyer_id'");
        }

        return contractService.getBuyerContractStateCount(buyer_id);
    }

    @PostMapping("/getSellerContractStateCount")
    public AjaxResult getSellerContractStateCount(@RequestHeader(name = "language", required = true) String language,
                                                 @RequestBody ContractPOJO contractPOJO) {
        long seller_id = contractPOJO.getSeller_id();

        if(seller_id == 0){
            return AjaxResult.error("error: Missing required parameter: 'seller_id'");
        }

        return contractService.getSellerContractStateCount(seller_id);
    }
}
