package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysArticleCategoryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.ArticleCategoryService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysArticleCategoryVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章分类管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/website/article/category")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleCategoryController {

    private final ArticleCategoryService categoryService;

    /**
     * 新增分类
     */
    @PostMapping
    public void add(@Valid @RequestBody SysArticleCategoryParam param) {
        categoryService.add(param);
    }

    /**
     * 修改分类
     */
    @PutMapping("/{id}")
    public void update(
            @PathVariable Long id,
            @Valid @RequestBody SysArticleCategoryParam param) {
        categoryService.update(id, param);
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }

    /**
     * 树形结构
     */
    @GetMapping("/tree")
    public List<SysArticleCategoryVO> tree() {
        List<SysArticleCategoryVO> tree = categoryService.getTree();
        return tree;
    }

    /**
     * 启用、禁用
     */
    @PutMapping("/{id}/status")
    public void toggleStatus(@PathVariable Long id) {
        categoryService.toggleStatus(id);
    }

}
