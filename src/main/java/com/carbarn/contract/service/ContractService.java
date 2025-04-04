package com.carbarn.contract.service;


import com.carbarn.contract.pojo.dto.ContractFullMessageDTO;
import com.carbarn.contract.pojo.ContractPOJO;
import com.carbarn.contract.pojo.dto.SearchContractDTO;
import com.carbarn.contract.pojo.dto.UserContractDTO;
import com.carbarn.inter.utils.AjaxResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContractService {

    AjaxResult createNewContract(String language, ContractPOJO contractPOJO);

    AjaxResult updateContract(ContractPOJO contractPOJO);

    ContractFullMessageDTO getContractInfo(String language, String contract_id);

    List<UserContractDTO> userContracts(SearchContractDTO searchContractDTO);

    void returnContract(String language, String contract_id);


}
