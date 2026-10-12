package com.guhecloud.cube.biz.daily.task.configuration.controller;

import com.guhecloud.cube.biz.daily.task.configuration.param.DailyUrgencyInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyUrgencyPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyUrgencyUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyUrgencyResponse;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyUrgencyService;
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
 * 紧急程度配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyUrgencyController {
    private final DailyUrgencyService dailyUrgencyService;

    /**
     * 新增紧急程度
     *
     * @param param 入参
     * @return 主键ID
     */
    @PostMapping("/configuration/insertDailyUrgency")
    public Long insertDailyUrgency(@RequestBody @Valid DailyUrgencyInsertParam param) {
        return dailyUrgencyService.insertDailyUrgency(param);
    }

    /**
     * 删除紧急程度
     *
     * @param id 主键ID
     * @return 是否成功
     */
    @DeleteMapping("/configuration/deleteDailyUrgency/{id}")
    public Boolean deleteDailyUrgency(@PathVariable Long id) {
        return dailyUrgencyService.deleteDailyUrgency(id);
    }

    /**
     * 修改紧急程度
     *
     * @param param 入参
     * @return 是否成功
     */
    @PostMapping("/configuration/updateDailyUrgency")
    public Boolean updateDailyUrgency(@RequestBody @Valid DailyUrgencyUpdateParam param) {
        return dailyUrgencyService.updateDailyUrgency(param);
    }

    /**
     * 查询紧急程度分页
     *
     * @param param 查询条件
     * @return 分页列表
     */
    @GetMapping("/configuration/queryDailyUrgencyPage")
    public Pagination<DailyUrgencyResponse> queryDailyUrgencyPage(DailyUrgencyPageParam param) {
        return dailyUrgencyService.queryDailyUrgencyPage(param);
    }
}
