package com.guhecloud.cube.biz.daily.task.record.controller;

import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordDetailParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordPageParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordSubmitParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordRepairSubmitParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordRepairTransferParam;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordDetail;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordExportResult;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordExceptionResult;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordPageResult;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordSubmitResult;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 记录
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyRecordController {
    private final DailyRecordService dailyRecordService;

    /**
     * 记录分页列表
     *
     * @param param 条件入参
     *
     * @return 分页对象
     */
    @GetMapping("/daily/record/page")
    @DataPermissionSwitch(dataModel = "dailyTask:taskRecord")
    public Pagination<DailyRecordPageResult> listRecord(DailyRecordPageParam param) {
        return dailyRecordService.pageRecord(param);
    }

    /**
     * 日常巡检
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/daily/record/inspect")
    @DataPermissionSwitch(dataModel = "dailyTask:inspectTaskRecord")
    public Pagination<DailyRecordPageResult> inspectListRecord(DailyRecordPageParam param) {
        return dailyRecordService.pageRecord(param);
    }

    /**
     * 日常巡检
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/daily/record/grid")
    @DataPermissionSwitch(dataModel = "dailyTask:gridTaskRecord")
    public Pagination<DailyRecordPageResult> gridListRecord(DailyRecordPageParam param) {
        return dailyRecordService.pageRecord(param);
    }

    /**
     * 获取记录详情
     *
     * @param param 记录条件
     *
     * @return 记录详情
     */
    @GetMapping("/daily/record/detail")
    public DailyRecordDetail detailRecord(DailyRecordDetailParam param) {
        return dailyRecordService.detailRecord(param);
    }

    /**
     * 提交记录
     *
     * @param param 提交参数
     *
     * @return 记录ID
     */
    @PostMapping("/daily/record/submit")
    public DailyRecordSubmitResult submitRecord(@RequestBody DailyRecordSubmitParam param) {
        return dailyRecordService.submitRecord(param);
    }

    @PostMapping("/daily/record/repair/submit")
    public Boolean submitRepair(@RequestBody DailyRecordRepairSubmitParam param) {
        return dailyRecordService.submitRepair(param);
    }

    @PostMapping("/daily/record/repair/transfer")
    public Boolean transferRepair(@RequestBody DailyRecordRepairTransferParam param) {
        return dailyRecordService.transferRepair(param);
    }

    @GetMapping("/daily/record/repair/detail")
    public DailyRecordExceptionResult getRepairDetail(@RequestParam Long exceptionId) {
        return dailyRecordService.getRepairDetail(exceptionId);
    }

    /**
     * 获取导出记录数据
     *
     * @param param 查询条件参数，用于过滤数据
     *
     * @return 导出结果列表，包含每条记录的基本信息以及相关的错误项名称集合
     */
    @GetMapping("/daily/record/export-data")
    public List<DailyRecordExportResult> getExportRecordDataList(DailyRecordPageParam param) {
        return dailyRecordService.getExportRecordDataList(param);
    }
}
