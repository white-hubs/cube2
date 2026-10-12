package com.guhecloud.cube.biz.daily.task.configuration.controller;

import com.guhecloud.cube.biz.daily.task.configuration.param.DailyCronBatchDeleteParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyCronInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyCronPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyCronUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyCronResponse;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyCronService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务周期配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyCronController {
    private final DailyCronService dailyCronService;

    /**
     * 新增任务周期
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    @PostMapping("/configuration/insertDailyCron")
    public Long insertDailyCron(@RequestBody @Valid DailyCronInsertParam param) {
        return dailyCronService.insertDailyCron(param);
    }

    /**
     * 批量删除任务周期
     *
     * @param param 批量删除
     *
     * @return 是否成功
     */
    @PostMapping("/configuration/deleteDailyCron")
    public Boolean deleteDailyCron(@RequestBody @Valid DailyCronBatchDeleteParam param) {
        return dailyCronService.deleteDailyCron(param);
    }

    /**
     * 修改任务周期
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PostMapping("/configuration/updateDailyCron")
    public Boolean updateDailyCron(@RequestBody @Valid DailyCronUpdateParam param) {
        return dailyCronService.updateDailyCron(param);
    }

    /**
     * 查询任务周期分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    @GetMapping("/configuration/queryDailyCronPage")
    public Pagination<DailyCronResponse> queryDailyCronPage(DailyCronPageParam param) {
        return dailyCronService.queryDailyCronPage(param);
    }
}

