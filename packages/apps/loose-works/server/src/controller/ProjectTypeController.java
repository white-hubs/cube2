package com.guhecloud.cube.srv.gen.loose.works.controller;

import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectTypeParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectTypeVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectTypePO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectTypeService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/8 16:34
 * 工程类型
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/project-type")
public class ProjectTypeController {
    private final ProjectTypeService projectTypeService;

    /**
     * 新增类型
     */
    @PostMapping("/save-or-update")
    public Boolean saveOrUpdate(@RequestBody ProjectTypeVO projectTypeVO) {
        return projectTypeService.saveOrUpdateProjectType(projectTypeVO);
    }

    /**
     * 删除类型
     */
    @DeleteMapping("/delete")
    public void delete(@RequestParam("id") Long id) {
        DatabaseContextHolder.softDelete(ProjectTypePO.class, id);
    }

    /**
     * 获取类型列表（分页）
     */
    @GetMapping("/page")
    public Pagination<ProjectTypeVO> getProjectTypePage(ProjectTypeParam param) {
        return projectTypeService.getProjectTypePage(param);
    }

    /**
     * 获取类型列表
     */
    @GetMapping("/list")
    public List<ProjectTypeVO> getProjectTypeList(ProjectTypeParam param) {
        List<ProjectTypePO> projectTypePOS = projectTypeService.lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getName()), ProjectTypePO::getName, param.getName())
                .list();
        return ObjectUtils.copying(projectTypePOS, ProjectTypeVO.class);
    }
}
