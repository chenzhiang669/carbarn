package com.carbarn.contract.mapper;

import com.carbarn.contract.pojo.ContractPOJO;
import com.carbarn.contract.pojo.dto.SearchContractDTO;
import com.carbarn.contract.pojo.dto.UserContractDTO;
import com.carbarn.contract.pojo.dto.UserContractStateDTO;
import com.carbarn.inter.pojo.dto.cars.SearchCarsDTO;
import com.carbarn.inter.pojo.firstpage.FirstPageContractDealDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractMapper {

    void createNewContract(ContractPOJO contractPOJO);

    void updateContract(ContractPOJO contractPOJO);

    void deleteContract(String contract_id);

    void removeContract(String contract_id);

    int getSellerState(String contract_id);

    ContractPOJO getContractInfo(String contract_id);

    void updateBuyerState(@Param("contract_id") String contract_id,
                          @Param("buyer_state") int buyer_state);

    void updateSellerState(@Param("contract_id") String contract_id,
                           @Param("seller_state") int seller_state);

    void updateFirstReviewTime(@Param("contract_id") String contract_id,
                           @Param("operation_first_review_time") long operation_first_review_time);

    void updateSecondReviewTime(@Param("contract_id") String contract_id,
                               @Param("operation_second_review_time") long operation_second_review_time);

    int isContractExits(@Param("car_id") int car_id,
                        @Param("buyer_id") long buyer_id,
                        @Param("seller_id") long seller_id);

    int isusedCarLocks(@Param("car_id") int car_id);


    List<UserContractDTO> userContracts(@Param("searchContractDTO") SearchContractDTO searchContractDTO);

    List<UserContractDTO> userDeletedContracts(@Param("searchContractDTO") SearchContractDTO searchContractDTO);

    void updateOperationContract(ContractPOJO contractPOJO);

    void updateUserContract(ContractPOJO contractPOJO);

    List<UserContractStateDTO> getBuyerContractStateCount(long buyer_id);

    List<UserContractStateDTO> getSellerContractStateCount(long seller_id);

    int getSellerContractDeleteCount(long seller_id);

    void updateSellerConfirmTime(@Param("contract_id") String contract_id,
                                 @Param("seller_confirm_time") String seller_confirm_time);

    void updateSellerId(@Param("original_user_id") long original_user_id,
                        @Param("target_user_id") long target_user_id);

    double getIncomeBySellerId(@Param("user_id") long user_id);

    int getOrderNumBySellerId(@Param("user_id") long user_id);

    List<FirstPageContractDealDTO> contractDeal(@Param("language") String language);
}
