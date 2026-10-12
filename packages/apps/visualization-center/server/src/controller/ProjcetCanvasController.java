package com.guhecloud.cube.base.visualization.server.controller;

import com.guhecloud.cube.base.visualization.server.param.ProjcetCanvasParam;
import com.guhecloud.cube.base.visualization.server.service.ProjcetCanvasService;
import com.guhecloud.cube.base.visualization.server.vo.ProjcetCanvasVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目数据源
 *
 * @author : lijie
 * @date : 2023-8-22
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/project/canvas")
public class ProjcetCanvasController {
    private final ProjcetCanvasService projcetCanvasService;

    /**
     * 通过实例id、项目id查询信息
     *
     * @param instanceId 实例id
     * @param projectId  项目id
     *
     * @return 项目数据
     */
    @GetMapping("/queryCanvas/")
    public ProjcetCanvasVO queryByProjectId(Long instanceId, Long projectId) {
        return projcetCanvasService.queryByProjectId(instanceId, projectId);
    }

    /**
     * 更新数据
     *
     * @param projectManage 实例对象
     *
     * @return 实例对象
     */
    @PutMapping("/update")
    public Boolean update(@RequestBody @Valid ProjcetCanvasParam projectManage) {
        return projcetCanvasService.update(projectManage);
    }
}
