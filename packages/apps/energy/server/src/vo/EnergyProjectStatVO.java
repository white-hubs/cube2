package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

@Data
public class EnergyProjectStatVO {
    private String projectCategory;
    private String instrumentType;
    private String projectId;
    private String projectName;
    private String parentId;
    private String parentName;
    private String value;
}
