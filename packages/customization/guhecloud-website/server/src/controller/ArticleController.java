package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysArticleParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysArticleQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.ArticleService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysArticleVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/website/article")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 新增文章
     */
    @PostMapping
    public void add(@RequestBody SysArticleParam param) {
        articleService.add(param);
    }

    /**
     * 编辑文章
     */
    @PutMapping("/{id}")
    public void update(
            @PathVariable Long id,
            @RequestBody SysArticleParam param) {
        articleService.update(id, param);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Pagination<SysArticleVO> page(SysArticleQueryParam param) {
        return articleService.page(param);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public SysArticleVO detail(@PathVariable Long id) {
        return articleService.detail(id);
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        articleService.delete(id);
    }

    /**
     * 启用、禁用
     */
    @PutMapping("/{id}/status")
    public void toggleStatus(@PathVariable Long id) {
        articleService.toggleStatus(id);
    }

}
