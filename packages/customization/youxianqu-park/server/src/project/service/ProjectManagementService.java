package com.guhecloud.cube.customized.youxianqu.park.management.server.project.service;

import com.guhecloud.cube.customized.youxianqu.park.management.server.project.param.ProjectManagementQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.project.result.ProjectManagementResult;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 项目管理服务
 */
public interface ProjectManagementService {
    /**
     * 分页列表
     *
     * @param param 条件
     * @return 分页
     */
    Pagination<ProjectManagementResult> getPage(ProjectManagementQueryParam param);
}
