package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class SpaceQueryDTO {

    private String projectId;

    private String search;

    private List<String> ids;

    private String type;

    private String attribute;

    private Boolean hasCompanyId;

    private String areaId;

    private String companyName;

    private String tradeModel;

    private String deptId;

}
