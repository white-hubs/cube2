package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;

/**
 * @author wangliang
 * @author 2022/01/24
 * @description
 */
@Data
public class WholesalerProtocolVerCodeDTO {
    private String mobile;
    private String stallId;
    private String protocolId;
    private String verifyCode;
    private String projectId;
}
