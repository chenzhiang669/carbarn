package com.carbarn.contract.service;


import com.carbarn.contract.pojo.dto.ContractFullMessageDTO;
import com.carbarn.contract.pojo.ContractPOJO;
import com.carbarn.contract.pojo.dto.SearchContractDTO;
import com.carbarn.contract.pojo.dto.UserContractDTO;
import com.carbarn.contract.pojo.dto.UserContractStateDTO;
import com.carbarn.inter.utils.AjaxResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContractService {

    AjaxResult createNewContract(String language, ContractPOJO contractPOJO);

    AjaxResult updateContract(ContractPOJO contractPOJO);

    AjaxResult deleteContract(String contract_id);

    ContractFullMessageDTO getContractInfo(String language, String contract_id);

    List<UserContractDTO> userContracts(SearchContractDTO searchContractDTO);

    List<UserContractDTO> userDeletedContracts(SearchContractDTO searchContractDTO);

    void returnContract(String language, String contract_id);

    void seller_pay_fund_success(String contract_id);

    void buyer_pay_fund_success(String contract_id);

    void updateBuyerState(String contract_id, int buyer_state);

    void updateSellerState(String contract_id, int seller_state);

    void updateOperationContract(ContractPOJO contractPOJO);

    void updateUserContract(ContractPOJO contractPOJO);

    AjaxResult getBuyerContractStateCount(long buyer_id);

    AjaxResult getSellerContractStateCount(long seller_id);

}
