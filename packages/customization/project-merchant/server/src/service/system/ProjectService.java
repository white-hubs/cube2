package com.guhecloud.cube.deliver.project.merchant.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.entity.Project;

public interface ProjectService extends IService<Project> {
    public static final String CODE_HQ = "hq"; // 总部项目编码
    public static final String BUSINESS_TYPE = "project";
}
