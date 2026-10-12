package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialFolderBatchUpdateParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialFolderParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.MaterialFolderService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.MaterialFolderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 素材文件夹Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/website/material-folder")
public class MaterialFolderController {

    private final MaterialFolderService materialFolderService;
    /**
     * 新增
     */
    @PostMapping
    public void add(@RequestBody MaterialFolderParam param) {
        materialFolderService.add(param);
    }

    /**
     * 重命名文件夹名称
     */
    @PutMapping("/{id}")
    public void update(@RequestBody MaterialFolderParam param) {
        materialFolderService.update(param);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        materialFolderService.delete(id);
    }

    /**
     * 批量修改
     */
    @PutMapping("/batch")
    public void updateBatch(@RequestBody MaterialFolderBatchUpdateParam param) {
        materialFolderService.updateBatch(param);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/batch")
    public void deleteBatch(@RequestBody List<Long> ids) {
        materialFolderService.deleteBatch(ids);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public MaterialFolderVO detail(@PathVariable Long id) {
        return materialFolderService.detail(id);
    }

    /**
     * 查询文件夹
     */
    @GetMapping("/list")
    public List<MaterialFolderVO> list(@RequestParam Long pathId) {
        return materialFolderService.list(pathId);
    }

}
