package com.carbarn.contract.service.impl;

import static com.carbarn.contract.contants.ContractState.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.contract.mapper.ContractMapper;
import com.carbarn.contract.pojo.dto.*;
import com.carbarn.contract.pojo.ContractPOJO;
import com.carbarn.contract.service.ContractService;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.mapper.UserMapper;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.service.CarsService;
import com.carbarn.inter.service.impl.CarsServiceImpl;
import com.carbarn.inter.utils.AjaxResult;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class ContractServiceImpl implements ContractService {

    public static int operate_save = 0; //操作: 保存
    public static int operate_confirm = 1; //操作: 确认

    public static DateTimeFormatter dateTimeFormater1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static DateTimeFormatter dateTimeFormater2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CarsService carsService;

    @Autowired
    private ParamsMapper paramsMapper;


    //创建新合同
    @Override
    public AjaxResult createNewContract(String language, ContractPOJO contractPOJO) {
        int car_id = contractPOJO.getCar_id();
        long buyer_id = contractPOJO.getBuyer_id();
        long seller_id = contractPOJO.getSeller_id();
        Map<String, Object> carsInfo = carsService.getCarsByID(language, car_id);
//        int vehicleType = (int) carsInfo.get("vehicleType");

//        if (vehicleType == CarsServiceImpl.usedcar_with_invoice
//                || vehicleType == CarsServiceImpl.usedcar_without_invoice) {
//
//            //针对二手车: 判断当前车辆是否已经被某个买家锁定
//            int isContractExist = contractMapper.isusedCarLocks(car_id);
//            if (isContractExist > 0) {
//                return AjaxResult.error("当前汽车已经被锁定，无法创建合同！");
//            }
//
//        } else if (vehicleType == CarsServiceImpl.newcar) {
//            //针对新车：判断当前买卖双方对车辆是否已经创建了合同
//            int isContractExist = contractMapper.isContractExits(car_id, buyer_id, seller_id);
//            if (isContractExist > 0) {
//                return AjaxResult.error("当前汽车的合同已经存在，请不要重复创建！");
//            }
//        }

        contractPOJO.setBuyer_state(buyer_state_draft); //买家状态为草稿箱
        contractPOJO.setSeller_state(seller_state_draft); //卖家状态为草稿箱
        String datetime = LocalDateTime.now().format(dateTimeFormater1);
        String contract_id = datetime + "-" + car_id + "-" + buyer_id + "-" + seller_id; //生成合同id: 时间(年月日时分秒)-汽车id-买家id-卖家id
        contractPOJO.setContract_id(contract_id);

        UserPojo buyerInfo = userMapper.getUserInfoByID(buyer_id); //从用户系统中获取买家信息
        UserPojo sellerInfo = userMapper.getUserInfoByID(seller_id); //从用户系统中获取卖家信息
        contractPOJO.setBuyer_nickname(buyerInfo.getNickname()); //新合同买家的昵称默认使用用户系统中的昵称
        contractPOJO.setBuyer_phone_num(buyerInfo.getPhone_num()); //新合同买家的电话号码默认使用用户系统中的电话号码

        String param_contract = paramsMapper.getValue(ParamKeys.param_contract);  //从参数库表中获取相关信息
        if(param_contract != null){
            JSONObject params = JSON.parseObject(param_contract);
            contractPOJO.setBuyer_guarantee_fund(params.getDouble("buyer_guarantee_fund"));
            contractPOJO.setSeller_guarantee_fund(params.getDouble("seller_guarantee_fund"));
        }else{
            contractPOJO.setBuyer_guarantee_fund(300.0);
            contractPOJO.setSeller_guarantee_fund(300.0);
        }

        contractMapper.createNewContract(contractPOJO);  //创建新合同

        ContractFullMessageDTO contractFullMessageDTO = transformContractInfo(language, contractPOJO, buyerInfo, sellerInfo, carsInfo);
        return AjaxResult.success("创建合同成功", contractFullMessageDTO);
    }

    //更新合同(保存/确认)
    @Override
    public AjaxResult updateContract(ContractPOJO contractPOJO) {
        try {
            if (operate_save == contractPOJO.getOperate()) {
                //如果是保存操作，将买家的状态置为草稿箱。
                contractPOJO.setBuyer_state(buyer_state_draft);
            } else if (operate_confirm == contractPOJO.getOperate()) {
                //如果是确认操作，将买家状态置为: 待卖家确认， 并更新买家确认时间
                contractPOJO.setBuyer_state(buyer_state_wating_seller_confirm);
                String buyer_confirm_time = LocalDateTime.now().format(dateTimeFormater2);
                contractPOJO.setBuyer_confirm_time(buyer_confirm_time);

                //如果卖家当前的状态是: 已退回，则将卖家的状态置为: 有修改
                int current_seller_state = contractMapper.getSellerState(contractPOJO.getContract_id());
                if (current_seller_state == seller_state_return) {
                    contractPOJO.setSeller_state(seller_state_modified);
                } else { //否则置为: 新合同
                    contractPOJO.setSeller_state(seller_state_new_contract);
                }
            } else {
                return AjaxResult.error("Missing required parameter: 'operate'");
            }

            contractMapper.updateContract(contractPOJO);
            return AjaxResult.success("更新合同信息成功");
        } catch (Exception e) {
            return AjaxResult.success("更新合同信息失败");
        }
    }

    //获取合同信息
    @Override
    public ContractFullMessageDTO getContractInfo(String language, String contract_id) {
        ContractPOJO contractPOJO = contractMapper.getContractInfo(contract_id);
        long buyer_id = contractPOJO.getBuyer_id();
        long seller_id = contractPOJO.getSeller_id();
        int car_id = contractPOJO.getCar_id();
        UserPojo buyerInfo = userMapper.getUserInfoByID(buyer_id);
        UserPojo sellerInfo = userMapper.getUserInfoByID(seller_id);
        Map<String, Object> carsInfo = carsService.getCarsByID(language, car_id);

        return transformContractInfo(language, contractPOJO, buyerInfo, sellerInfo, carsInfo);
    }

    public ContractFullMessageDTO transformContractInfo(String language,
                                                        ContractPOJO contractPOJO,
                                                        UserPojo buyerInfo,
                                                        UserPojo sellerInfo,
                                                        Map<String, Object> carsInfo) {

        ContractUserInfoDTO contractBuyerInfoDTO = ContractUserInfoDTO.getContractBuyerInfoDTO(buyerInfo, contractPOJO);
        ContractUserInfoDTO contractSellerInfoDTO = ContractUserInfoDTO.getContractSellerInfoDTO(sellerInfo);
        ContractCarInfoDTO contractCarInfoDTO = ContractCarInfoDTO.getContractCarInfoDTO(carsInfo);

        ContractFullMessageDTO contractInfo = ContractFullMessageDTO.getContractFullMessageDTO(contractPOJO);
        contractInfo.setBuyer_info(contractBuyerInfoDTO);
        contractInfo.setSeller_info(contractSellerInfoDTO);
        contractInfo.setCar_info(contractCarInfoDTO);

        return contractInfo;
    }

    @Override
    public List<UserContractDTO> userContracts(SearchContractDTO searchContractDTO) {
        return contractMapper.userContracts(searchContractDTO);
    }

    //买家退回合同
    @Override
    public void returnContract(String language, String contract_id) {
        contractMapper.updateBuyerState(contract_id, buyer_state_be_returned); //买家状态更改为被退回
        contractMapper.updateSellerState(contract_id, seller_state_return); //卖家状态更改为已退回
    }

    //卖家支付保障金成功
    @Override
    public void seller_pay_fund_success(String contract_id) {
        contractMapper.updateBuyerState(contract_id, buyer_state_seller_confirm); //买家状态更改为: 卖家已确认
        contractMapper.updateSellerState(contract_id, seller_state_waiting_buyer_confirm); //卖家状态更改为：等待买家确认(支付保障金)
        //TODO 更新字段 pay_seller_guarantee_fund 为true
    }

    //买家支付保障金成功
    @Override
    public void buyer_pay_fund_success(String contract_id) {
        contractMapper.updateBuyerState(contract_id, buyer_state_platform_review); //买家状态更改为: 等待平台审核
        contractMapper.updateSellerState(contract_id, seller_state_waiting_buyer_paycar); //卖家状态更改为：等待买家支付车款
        //TODO 更新字段 pay_buyer_guarantee_fund 为true
    }

    //平台审核通过
    public void review_success(String contract_id) {
        contractMapper.updateBuyerState(contract_id, buyer_state_platform_wating_paycar); //买家状态更改为: 等待支付车款
    }

    //买家支付车款成功
    public void buyer_paycar_success(String contract_id) {
        contractMapper.updateBuyerState(contract_id, buyer_state_platform_wating_transport); //买家状态更改为: 车辆运输中
        contractMapper.updateSellerState(contract_id, seller_state_waiting_deliver_car); //卖家状态更改为：车辆待交付
    }

    //平台验车成功
    public void platform_inspection_car_success(String contract_id) {
        contractMapper.updateBuyerState(contract_id, buyer_state_inspection_car); //买家状态更改为: 验车
        contractMapper.updateSellerState(contract_id, seller_state_finished); //卖家状态更改为：交易完成
    }

    //汽车到岸
    public void car_landed(String contract_id) {
        contractMapper.updateBuyerState(contract_id, buyer_state_car_landed); //买家状态更改为: 到岸
    }

    //汽车清关
    public void customs_clearance(String contract_id) {
        contractMapper.updateBuyerState(contract_id, buyer_state_customs_clearance); //买家状态更改为: 清关
    }

    //汽车交付
    public void car_delivery(String contract_id) {
        contractMapper.updateBuyerState(contract_id, buyer_state_car_delivery); //买家状态更改为: 交付
    }

    //交易完成
    public void trade_finished(String contract_id) {
        contractMapper.updateBuyerState(contract_id, buyer_state_finished); //买家状态更改为: 交易完成
    }

}