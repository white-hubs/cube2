package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SpareQueryDTO extends BaseQueryDTO {

    private String estimate;

    private String typeId;

    private String typeCode;

    private String status;

    private Boolean hasDetails;

}
