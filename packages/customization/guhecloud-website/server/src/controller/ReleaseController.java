package com.guhecloud.cube.customized.guhecloudwebsite.controller;

import com.guhecloud.cube.customized.guhecloudwebsite.param.BuildRecordQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.BuildRecordService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.BuildRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 构建发布制品
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/website/release")
@Validated
public class ReleaseController {

    private final BuildRecordService buildRecordService;

    /**
     * 分页查询构建记录
     */
    @GetMapping("/page")
    public Pagination<BuildRecordVO> page(BuildRecordQueryParam param) {
        return buildRecordService.page(param);
    }

    /**
     * 生成制品
     */
    @PostMapping("/build")
    public void build() {
        buildRecordService.build();
    }

    /**
     * 发布制品
     */
    @PostMapping("/{id}/publish")
    public Boolean publish(@PathVariable Long id) {
        return buildRecordService.publish(id);
    }

    /**
     * 删除制品
     */
    @DeleteMapping("/{id}")
    public Boolean deleteProduct(@PathVariable Long id) {
        return buildRecordService.deleteProduct(id);
    }
}
