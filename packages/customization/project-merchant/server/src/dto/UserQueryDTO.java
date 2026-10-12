package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;

@Data
public class UserQueryDTO {

    private String projectId;

    private String deptId;

    private String postId;

    private String search;

    private String searchStore;

    private String areaId;

    private Boolean enabled;

    private Integer gender;

    private String type;

    private Boolean isCompanyOwner;

}
