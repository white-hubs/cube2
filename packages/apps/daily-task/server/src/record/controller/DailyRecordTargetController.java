package com.guhecloud.cube.biz.daily.task.record.controller;

import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordDetailParam;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordTargetDetailResult;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordTargetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyRecordTargetController {

    private final DailyRecordTargetService dailyRecordTargetService;

    /**
     * 获取记录目标详情
     *
     * @param param 查询参数，包含任务ID、目标ID和记录ID等信息
     *
     * @return DailyRecordTargetStandardResult 记录目标的详细信息，包括标准ID、标准名称、文件列表等
     */
    @GetMapping("/daily/record/target-detail")
    public DailyRecordTargetDetailResult getRecordTargetDetail(DailyRecordDetailParam param) {
        return dailyRecordTargetService.getRecordTargetDetail(param);
    }
}
