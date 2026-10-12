package com.guhecloud.cube.biz.daily.task.configuration.controller;

import com.guhecloud.cube.biz.daily.task.configuration.param.DailyResultInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyResultPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyResultUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyResultResponse;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyResultService;
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
 * 执行结果项配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyResultController {
    private final DailyResultService dailyResultService;

    /**
     * 新增执行结果项
     *
     * @param param 入参
     * @return 主键ID
     */
    @PostMapping("/configuration/insertDailyResult")
    public Long insertDailyResult(@RequestBody @Valid DailyResultInsertParam param) {
        return dailyResultService.insertDailyResult(param);
    }

    /**
     * 删除执行结果项
     *
     * @param id 主键ID
     * @return 是否成功
     */
    @DeleteMapping("/configuration/deleteDailyResult/{id}")
    public Boolean deleteDailyResult(@PathVariable Long id) {
        return dailyResultService.deleteDailyResult(id);
    }

    /**
     * 修改执行结果项
     *
     * @param param 入参
     * @return 是否成功
     */
    @PostMapping("/configuration/updateDailyResult")
    public Boolean updateDailyResult(@RequestBody @Valid DailyResultUpdateParam param) {
        return dailyResultService.updateDailyResult(param);
    }

    /**
     * 查询执行结果项分页
     *
     * @param param 查询条件
     * @return 分页列表
     */
    @GetMapping("/configuration/queryDailyResultPage")
    public Pagination<DailyResultResponse> queryDailyResultPage(DailyResultPageParam param) {
        return dailyResultService.queryDailyResultPage(param);
    }
}

