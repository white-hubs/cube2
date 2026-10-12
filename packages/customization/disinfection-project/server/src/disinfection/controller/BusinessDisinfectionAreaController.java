package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.AreaPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.AreaUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionAreaService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.AreaInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.AreaPageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * 消杀区域接口
 */
@RestController
@RequestMapping("/disinfection/projects/{projectId}/areas")
@RequiredArgsConstructor
@Slf4j
public class BusinessDisinfectionAreaController {
    private final BusinessDisinfectionAreaService businessAreaService;

    /**
     * 根据条件获取全部列表
     */
    @GetMapping("list-all")
    public List<AreaInfoVO> listAll(@PathVariable Long projectId, String keyword) {
        return businessAreaService.listAll(projectId, keyword);
    }

    /**
     * 新增区域
     */
    @PostMapping
    public Boolean insertArea(@PathVariable Long projectId, @Valid @RequestBody AreaUpsertParam param) {
        return businessAreaService.insertArea(param, projectId);
    }

    /**
     * 批量编辑区域
     */
    @PutMapping
    public Boolean batchUpdateArea(@PathVariable Long projectId, @Valid @RequestBody AreaUpsertParam param) {
        return businessAreaService.updateArea(param, projectId);
    }

    /**
     * 单个修改区域
     */

    @PutMapping("{id}")
    public Boolean updateArea(@PathVariable Long projectId, @PathVariable Long id,
                              @Valid @RequestBody AreaUpsertParam.AreaParam param
                             ) {
        return businessAreaService.singleUpdateArea(param, projectId, id);
    }

    /**
     * 删除区域
     */
    @DeleteMapping("{areaId}")
    public Boolean deleteArea(@PathVariable Long areaId) {
        return businessAreaService.deleteArea(areaId);
    }

    /**
     * 区域分页
     */
    @GetMapping
    public Pagination<AreaPageVO> listArea(@PathVariable String projectId, AreaPageParam param) {
        return businessAreaService.pageArea(param, projectId);
    }

}
