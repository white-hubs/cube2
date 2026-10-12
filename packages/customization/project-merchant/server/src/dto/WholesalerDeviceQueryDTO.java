package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangliang
 * @author 2022/01/04
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WholesalerDeviceQueryDTO extends BaseQueryDTO {
    private String type;

    private String stallName;

    private String companyName;

    private String stallId;

    private String description;

    private String companyId;

    private Boolean danger;

}
