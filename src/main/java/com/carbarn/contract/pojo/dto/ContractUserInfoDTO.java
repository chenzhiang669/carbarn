package com.carbarn.contract.pojo.dto;

import com.carbarn.contract.pojo.ContractPOJO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import lombok.Data;

@Data
public class ContractUserInfoDTO {
    private long id;

    private String nickname;

    private String phone_num;

    private String address;

    private String email;


    public static ContractUserInfoDTO getContractBuyerInfoDTO(UserPojo userPojo, ContractPOJO contractPOJO){
        ContractUserInfoDTO contractBuyerInfoDTO = new ContractUserInfoDTO();
        contractBuyerInfoDTO.setId(contractPOJO.getBuyer_id());
        if(contractPOJO.getBuyer_nickname() != null){
            contractBuyerInfoDTO.setNickname(contractPOJO.getBuyer_nickname());
        }else{
            contractBuyerInfoDTO.setNickname(userPojo.getNickname());
        }

        if(contractPOJO.getBuyer_phone_num() != null){
            contractBuyerInfoDTO.setPhone_num(contractPOJO.getBuyer_phone_num());
        }else{
            contractBuyerInfoDTO.setPhone_num(userPojo.getPhone_num());
        }

        contractBuyerInfoDTO.setAddress(contractPOJO.getBuyer_address());
        contractBuyerInfoDTO.setEmail(contractPOJO.getBuyer_email());
        return contractBuyerInfoDTO;
    }

    public static ContractUserInfoDTO getContractSellerInfoDTO(UserPojo userPojo){
        ContractUserInfoDTO contractBuyerInfoDTO = new ContractUserInfoDTO();
        contractBuyerInfoDTO.setId(userPojo.getId());
        contractBuyerInfoDTO.setNickname(userPojo.getNickname());
        contractBuyerInfoDTO.setPhone_num(userPojo.getPhone_num());
        contractBuyerInfoDTO.setAddress(userPojo.getAddress());

        return contractBuyerInfoDTO;
    }
}
