package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerAuth;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerAuthInfoDTO extends ConvertionDTO<WholesalerAuthInfoDTO, WholesalerAuth> {
    public WholesalerAuthInfoDTO() {}

    public WholesalerAuthInfoDTO(WholesalerAuth wholesalerAuth) {
        super();
        map(wholesalerAuth);
    }

    public WholesalerAuthInfoDTO(List<WholesalerAuth> list) {
        super();
        map(list);
    }

    private String id;

    private String registrationNumber;

    private String enterpriseName;

    private String address;

    private String realName;

    private String postCode;

    private String identityCard;

    private String mobile;

    private String signatory;

    private String signatoryMobile;
}
