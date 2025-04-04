package com.carbarn.contract.pojo.dto;

import com.carbarn.contract.pojo.ContractPOJO;
import lombok.Data;

@Data
public class ContractFullMessageDTO {
    private String contract_id;
    private double price;
    private int cnt;
    private long buyer_id;
    private int seller_id;
    private double buyer_guarantee_fund;
    private boolean pay_buyer_guarantee_fund;
    private double seller_guarantee_fund;
    private boolean pay_seller_guarantee_fund;
    private String other_agreements;
    private String buyer_confirm_time;
    private String seller_confirm_time;
    private String create_time;

    private ContractUserInfoDTO buyer_info;

    private ContractUserInfoDTO seller_info;

    private ContractCarInfoDTO car_info;

    public static ContractFullMessageDTO getContractFullMessageDTO(ContractPOJO contractPOJO){
        ContractFullMessageDTO contractFullMessageDTO = new ContractFullMessageDTO();
        contractFullMessageDTO.setContract_id(contractPOJO.getContract_id());
        contractFullMessageDTO.setPrice(contractPOJO.getPrice());
        contractFullMessageDTO.setCnt(contractPOJO.getCnt());
        contractFullMessageDTO.setBuyer_id(contractPOJO.getBuyer_id());
        contractFullMessageDTO.setSeller_id(contractPOJO.getSeller_id());
        contractFullMessageDTO.setBuyer_guarantee_fund(contractPOJO.getBuyer_guarantee_fund());
        contractFullMessageDTO.setSeller_guarantee_fund(contractPOJO.getSeller_guarantee_fund());
        contractFullMessageDTO.setOther_agreements(contractPOJO.getOther_agreements());
        contractFullMessageDTO.setBuyer_confirm_time(contractPOJO.getBuyer_confirm_time());
        contractFullMessageDTO.setCreate_time(contractPOJO.getCreate_time());
        return contractFullMessageDTO;
    }
}
