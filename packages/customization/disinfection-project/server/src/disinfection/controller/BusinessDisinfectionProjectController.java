package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.ProjectPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.ProjectUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionProjectService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.ProjectAreaCountVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.ProjectPageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消杀项目相关接口
 */
@RestController
@RequestMapping("/disinfection/projects")
@RequiredArgsConstructor
public class BusinessDisinfectionProjectController {
    private final BusinessDisinfectionProjectService businessProjectService;

    /**
     * 新增项目
     */
    @PostMapping
    public Boolean insertProject(@Valid @RequestBody ProjectUpsertParam param) {
        return businessProjectService.insertProject(param);
    }

    /**
     * 删除项目
     */
    @DeleteMapping("{id}")
    public Boolean deleteProject(@PathVariable Long id) {
        return businessProjectService.delProject(id);
    }

    /**
     * 修改项目
     */
    @PutMapping("{id}")
    public Boolean updateProject(@PathVariable Long id, @Valid @RequestBody ProjectUpsertParam param) {
        return businessProjectService.updateProject(id, param);
    }

    /**
     * 获取项目基本信息
     */
    @GetMapping("/{productId}")
    public ProjectInfoVO detail(@PathVariable Long productId) {
        return businessProjectService.detailProject(productId);
    }

    /**
     * 分页
     */
    @GetMapping
    public Pagination<ProjectPageVO> list(ProjectPageParam param) {
        return businessProjectService.pageProject(param);
    }

    /**
     * 列表
     */
    @GetMapping("list-all")
    public List<ProjectPageVO> listAll(ProjectPageParam param) {
        return businessProjectService.listAll(param);

    }

    /**
     * 获取项目区域个数信息列表
     */
    @GetMapping("/areas-count")
    public List<ProjectAreaCountVO> projectAreaCount() {
        return businessProjectService.projectAreaCount();
    }

}
