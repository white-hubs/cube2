package com.guhecloud.cube.biz.daily.task.configuration.controller;

import com.guhecloud.cube.biz.daily.task.configuration.param.DailyTaskCategoryInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyTaskCategoryPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyTaskCategoryUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyTaskCategoryResponse;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyTaskCategoryService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务类型配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTaskCategoryController {
    private final DailyTaskCategoryService dailyTaskCategoryService;

    /**
     * 新增任务类型
     *
     * @param param 入参
     * @return 主键ID
     */
    @PostMapping("/configuration/insertDailyTaskCategory")
    public Long insertDailyTaskCategory(@RequestBody @Valid DailyTaskCategoryInsertParam param) {
        return dailyTaskCategoryService.insertDailyTaskCategory(param);
    }

    /**
     * 删除任务类型
     *
     * @param id 主键ID
     * @return 是否成功
     */
    @DeleteMapping("/configuration/deleteDailyTaskCategory/{id}")
    public Boolean deleteDailyTaskCategory(@PathVariable Long id) {
        return dailyTaskCategoryService.deleteDailyTaskCategory(id);
    }

    /**
     * 修改任务类型
     *
     * @param param 入参
     * @return 是否成功
     */
    @PostMapping("/configuration/updateDailyTaskCategory")
    public Boolean updateDailyTaskCategory(@RequestBody @Valid DailyTaskCategoryUpdateParam param) {
        return dailyTaskCategoryService.updateDailyTaskCategory(param);
    }

    /**
     * 查询任务类型分页
     *
     * @param param 查询条件
     * @return 分页列表
     */
    @GetMapping("/configuration/queryDailyTaskCategoryPage")
    public Pagination<DailyTaskCategoryResponse> queryDailyTaskCategoryPage(DailyTaskCategoryPageParam param) {
        return dailyTaskCategoryService.queryDailyTaskCategoryPage(param);
    }
}
