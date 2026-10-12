package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;

@Data
public class PlanDetailParam {
    private Long id;
    private String targetName;
    private String targetCategory;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean notTarget;
}
