package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;

@Data
public class SafetyReportQueryDTO extends BaseQueryDTO {

    private String firmId;

    private String projectCode;

}
