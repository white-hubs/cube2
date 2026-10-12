package com.guhecloud.cube.deliver.project.merchant.controller.system;

import com.guhecloud.cube.deliver.project.merchant.dto.system.ProjectDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Project;
import com.guhecloud.cube.deliver.project.merchant.enums.ProjectEnum;
import com.guhecloud.cube.deliver.project.merchant.service.system.ProjectService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-11  14:48
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/public/projects/list")
    public List<ProjectDTO> getPublicList(@RequestParam(name = "terminalType", defaultValue = "pc") String terminalType) {
        return new ProjectDTO(projectService.lambdaQuery()
                                      .notIn("app".equals(terminalType), Project::getCode, ProjectEnum.CODE_SNCD.getCode(), ProjectEnum.CODE_HQ.getCode())
                                      .eq(Project::getEnabled, true).list()).list();
    }
}
