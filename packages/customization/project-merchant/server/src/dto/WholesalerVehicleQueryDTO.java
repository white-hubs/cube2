package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangliang
 * @author 2021/12/31
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WholesalerVehicleQueryDTO extends BaseQueryDTO {

    private String veciclePlate;

    private String type;

    private String owner;

    private String mobile;

    private String stallName;

    private String stallId;

    private String companyId;

}
