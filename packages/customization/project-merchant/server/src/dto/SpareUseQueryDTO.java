package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SpareUseQueryDTO extends BaseQueryDTO {

    private String deviceTypeId;

    private String areaId;

    private String spareTypeId;

    private String businessType;

    private String businessKey;

}
