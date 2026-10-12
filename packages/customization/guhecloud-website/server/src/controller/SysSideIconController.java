package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSideIconParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSideIconQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysSideIconService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSideIconVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 侧边图标Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/website/side-icon")
@Validated
public class SysSideIconController {

    private final SysSideIconService sideIconService;

    /**
     * 新增
     */
    @PostMapping
    public void add(@Validated @RequestBody SysSideIconParam param) {
        sideIconService.add(param);
    }

    /**
     * 编辑
     */
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Validated @RequestBody SysSideIconParam param) {
        sideIconService.update(id, param);
    }

    /**
     * 列表查询
     */
    @GetMapping("/list")
    public List<SysSideIconVO> list(@Validated SysSideIconQueryParam param) {
        return sideIconService.list(param);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public SysSideIconVO detail(@PathVariable Long id) {
        return sideIconService.detail(id);
    }

    /**
     * 启用、禁用
     */
    @PutMapping("/{id}/status")
    public void toggleStatus(@PathVariable Long id, @RequestParam Integer status) {
        sideIconService.toggleStatus(id, status);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sideIconService.delete(id);
    }

    /**
     * 排序
     */
    @PutMapping("/{id}/sort")
    public void sort(@PathVariable Long id, @RequestParam Integer sort) {
        sideIconService.sort(id, sort);
    }

    /**
     * 批量启用、禁用
     */
    @PutMapping("/batch/status")
    public void batchStatus(@RequestBody List<Long> ids, @RequestParam Integer status) {
        sideIconService.batchStatus(ids, status);
    }

}
