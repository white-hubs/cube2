package com.guhecloud.cube.base.visualization.server.controller;

import com.guhecloud.cube.base.visualization.server.entity.ProjectManage;
import com.guhecloud.cube.base.visualization.server.param.ProjectManageParam;
import com.guhecloud.cube.base.visualization.server.param.ProjectManageUe5UpdateParam;
import com.guhecloud.cube.base.visualization.server.param.ProjectManageUpdateParam;
import com.guhecloud.cube.base.visualization.server.service.ProjectManageService;
import com.guhecloud.cube.base.visualization.server.vo.ProjectManageVO;
import com.guhecloud.cube.base.visualization.server.vo.ProjectModelConfigurationVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 项目管理
 *
 * @author : lijie
 * @date : 2023-8-21
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/project")
public class ProjectManageController {
    private final ProjectManageService projectManageService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return 实例对象
     */
    @GetMapping("/queryById/{id}")
    public ProjectManageVO queryById(@PathVariable Long id) {
        return projectManageService.queryById(id);
    }

    /**
     * 项目复制
     *
     * @param id
     *
     * @return
     */
    @PostMapping("/copy/{id}")
    public Long copyProject(@PathVariable Long id) {
        ProjectManage projectManage = projectManageService.getById(id);
        Assert.isNotFound(projectManage, "复制的项目不存在");
        return this.projectManageService.copyProject(projectManage);
    }

    /**
     * 查询我的所有项目
     *
     * @return 项目信息
     */
    @GetMapping("/queryProjectAll")
    public List<ProjectManageVO> queryProjectAll() {
        return projectManageService.queryProjectAll();
    }

    /**
     * 新增数据
     *
     * @param projectManage 实例对象
     *
     * @return 实例对象
     */
    @PostMapping("/insert")
    public Boolean insert(@RequestBody @Valid ProjectManageParam projectManage) {
        return projectManageService.insert(projectManage);
    }

    /**
     * 更新数据
     *
     * @param projectManage 实例对象
     *
     * @return 实例对象
     */
    @PutMapping("/update")
    public Boolean update(@RequestBody @Valid ProjectManageUpdateParam projectManage) {
        return projectManageService.update(projectManage);
    }

    /**
     * 更新基础配置
     */
    @PutMapping("/update/base/config")
    public Boolean updateBaseConfig(@RequestBody @Valid ProjectManageUe5UpdateParam param) {
        return projectManageService.updateBaseConfig(param);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Boolean deleteById(@PathVariable Long id) {
        return projectManageService.deleteById(id);
    }

    /**
     * 查询可视化全量配置(新)
     */
    @GetMapping("/model/configuration")
    public ProjectModelConfigurationVO getModelConfigurationQueryByProjectId(@RequestParam Long projectId) {
        return projectManageService.getModelConfigurationQueryByProjectId(projectId);
    }

    /**
     * 数据迁移
     */
    @GetMapping("/data-migration")
    public void dataMigration() {
        projectManageService.dataMigration();
    }

}
