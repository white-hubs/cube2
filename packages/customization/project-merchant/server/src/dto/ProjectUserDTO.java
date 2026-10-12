package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProjectUserDTO {

    @NotNull
    @NotBlank
    private String projectId;

    @NotNull
    @NotBlank
    private String userId;

    private String deptId;

    private Boolean isDeptManager;

    private String postId;

    private List<String> roleIds;

}
