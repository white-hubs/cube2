package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSitePageParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSitePageQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysSitePageService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSitePageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 页面管理Controller
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/website/page")
public class SysSitePageController {

    private final SysSitePageService sitePageService;

    /**
     * 新增页面
     */
    @PostMapping
    public void add(@RequestBody SysSitePageParam param) {
        sitePageService.add(param);
    }

    /**
     * 编辑页面
     */
    @PutMapping("/{id}")
    public void update(@RequestBody SysSitePageParam param) {
        sitePageService.update(param);
    }

    /**
     * 获取页面列表
     */
    @GetMapping("/list")
    public List<SysSitePageVO> list(SysSitePageQueryParam param) {
        return sitePageService.list(param);
    }

    /**
     * 获取页面详情
     */
    @GetMapping("/{id}")
    public SysSitePageVO detail(@PathVariable Long id) {
        return sitePageService.detail(id);
    }

    /**
     * 删除页面
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sitePageService.delete(id);
    }

}
