package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;

/**
 * @author wangliang
 * @author 2022/01/21
 * @description
 */
@Data
public class WholesalerContractSignDTO {

    private String projectId;

    private String mobile;
    private String contractId;

    private String verifyCode;

    private String secondPartId;

}
