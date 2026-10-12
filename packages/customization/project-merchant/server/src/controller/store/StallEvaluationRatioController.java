package com.guhecloud.cube.deliver.project.merchant.controller.store;

import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRatio;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallEvaluationRatioService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * <p>
 * 档位评价维度权重占比 前端控制器
 * </p>
 *
 * @author bq
 * @since 2022-06-16
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallEvaluationRatioController {

    private final StallEvaluationRatioService ratioService;

    /**
     * 根据项目ID查询配置
     * @param projectId
     * @return
     */
    @GetMapping("/stall-evaluation-ratio")
    public StallEvaluationRatio get(@RequestParam(value = "projectId") String projectId) {
        return ratioService.lambdaQuery().eq(StallEvaluationRatio::getProjectId, projectId).one();
    }

    /**
     * 新增或者修改
     * @param ratio
     */
    @PostMapping("/stall-evaluation-ratio")
    public void saveOrUpdate(@Valid @RequestBody StallEvaluationRatio ratio) {
        ratioService.insertOrUpdate(ratio);
    }

}

