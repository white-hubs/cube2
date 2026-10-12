package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeQueryDTO extends BaseQueryDTO {

    private String type;

    private String deviceTypeId;

    private String deviceId;

}