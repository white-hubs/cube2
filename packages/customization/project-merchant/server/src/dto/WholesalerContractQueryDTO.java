package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangliang
 * @author 2022/01/14
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WholesalerContractQueryDTO extends BaseQueryDTO {

    private Integer status;

    private Integer signingWay;

    private String contractType;

    private String createBy;

    private String stallId;

}
