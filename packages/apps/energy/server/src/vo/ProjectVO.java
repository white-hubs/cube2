package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectVO {
    private Long id;
    private String projectCode;
    private String projectName;
}
