package com.guhecloud.cube.customized.youxianqu.park.management.server.project.controller;

import com.guhecloud.cube.customized.youxianqu.park.management.server.project.param.ProjectManagementQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.project.result.ProjectManagementResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.project.service.ProjectManagementService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目管理接口
 *
 * @author lanchun
 * @date 2024/8/28 15:23
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectManagementController {
    private final ProjectManagementService projectManagementService;

    /**
     * 分页列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @GetMapping("/projectManagement/page")
    public Pagination<ProjectManagementResult> getPage(ProjectManagementQueryParam param) {
        return projectManagementService.getPage(param);
    }
}
