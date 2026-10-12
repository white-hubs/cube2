package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "project_user")
public class ProjectUser {

    private String projectId;

    private String userId;

    private Boolean manager;

}
