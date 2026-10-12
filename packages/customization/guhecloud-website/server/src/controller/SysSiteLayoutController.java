package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteLayoutAddParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteLayoutUpdateParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysSiteLayoutService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSiteLayoutVO;
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
 * 布局 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/website/layout")
public class SysSiteLayoutController {

    private final SysSiteLayoutService sysSiteLayoutService;

    /**
     * 获取布局列表
     */
    @GetMapping("/list")
    public List<SysSiteLayoutVO> list() {
        return sysSiteLayoutService.list();
    }

    /**
     * 获取布局详情
     */
    @GetMapping("/{id}")
    public SysSiteLayoutVO detail(@PathVariable Long id) {
        return sysSiteLayoutService.detail(id);
    }

    /**
     * 新增布局
     */
    @PostMapping
    public void add(@RequestBody SysSiteLayoutAddParam param) {
        sysSiteLayoutService.add(param);
    }

    /**
     * 编辑布局
     */
    @PutMapping
    public void update(@RequestBody SysSiteLayoutUpdateParam param) {
        sysSiteLayoutService.update(param);
    }

    /**
     * 删除布局
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sysSiteLayoutService.delete(id);
    }
}
