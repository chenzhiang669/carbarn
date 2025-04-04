package com.carbarn.contract.mapper;

import com.carbarn.contract.pojo.ContractPOJO;
import com.carbarn.contract.pojo.dto.SearchContractDTO;
import com.carbarn.contract.pojo.dto.UserContractDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractMapper {

    void createNewContract(ContractPOJO contractPOJO);

    void updateContract(ContractPOJO contractPOJO);

    int getSellerState(String contract_id);

    ContractPOJO getContractInfo(String contract_id);

    void updateBuyerState(@Param("contract_id") String contract_id,
                          @Param("buyer_state") int buyer_state);

    void updateSellerState(@Param("contract_id") String contract_id,
                           @Param("seller_state") int seller_state);

    int isContractExits(@Param("car_id") int car_id,
                        @Param("buyer_id") long buyer_id,
                        @Param("seller_id") long seller_id);

    int isusedCarLocks(@Param("car_id") int car_id);


    List<UserContractDTO> userContracts(@Param("searchContractDTO") SearchContractDTO searchContractDTO);

}
