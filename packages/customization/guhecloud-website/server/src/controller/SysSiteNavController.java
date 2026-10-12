package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteNavParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteNavQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysSiteNavService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSiteNavVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 站点导航Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/website/nav")
@Validated
public class SysSiteNavController {

    private final SysSiteNavService siteNavService;

    /**
     * 新增
     */
    @PostMapping
    public void add(@Validated @RequestBody SysSiteNavParam param) {
        siteNavService.add(param);
    }

    /**
     * 编辑
     */
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Validated @RequestBody SysSiteNavParam param) {
        siteNavService.update(id, param);
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    public List<SysSiteNavVO> list(SysSiteNavQueryParam param) {
        return siteNavService.list(param);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public SysSiteNavVO detail(@PathVariable Long id) {
        return siteNavService.detail(id);
    }

    /**
     * 启用、禁用
     */
    @PutMapping("/{id}/status")
    public void toggleStatus(@PathVariable Long id, @RequestParam Integer status) {
        siteNavService.toggleStatus(id, status);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        siteNavService.delete(id);
    }

}
