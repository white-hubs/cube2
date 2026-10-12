package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialRenmeParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.MaterialService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.MaterialVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 素材Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/website/material")
public class MaterialController {

    private final MaterialService materialService;

    /**
     * 新增
     */
    @PostMapping
    public void add(@RequestBody List<MaterialParam> param) {
        materialService.add(param);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        materialService.delete(id);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/batch")
    public void deleteBatch(@RequestBody List<Long> ids) {
        materialService.deleteBatch(ids);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public MaterialVO detail(@PathVariable Long id) {
        return materialService.detail(id);
    }

    /**
     * 查询
     */
    @GetMapping("/list")
    public List<MaterialVO> list(MaterialQueryParam param) {
        return materialService.list(param);
    }

    /**
     * 重命名素材
     */
    @PutMapping("/{id}/rename")
    public void rename(@PathVariable Long id, @RequestBody MaterialRenmeParam param) {
        materialService.rename(id, param.getName());
    }

}
