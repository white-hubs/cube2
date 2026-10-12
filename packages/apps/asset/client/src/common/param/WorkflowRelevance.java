package com.guhecloud.cube.property.management.client.common.param;

import lombok.Data;

@Data
public class WorkflowRelevance {
    private String processInstanceId;
    private String businessType;
    //记录ID
    private Long propertyId;
}
