package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

@Data
public class ProjectItemVo {
    private Long itemId;
    private String bindType;
    private String deviceId;
    private String projectId;
    private Integer tenant;
    private String instrumentType;
    private String projectName;
}
