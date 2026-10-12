package com.guhecloud.cube.biz.daily.task.record.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordExceptionPO;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordRepairSubmitParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordRepairTransferParam;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordExceptionResult;

import java.util.List;
import java.util.Map;

public interface DailyRecordExceptionService extends IService<DailyRecordExceptionPO> {
    List<DailyRecordExceptionPO> createExceptions(Long recordId, Long taskId, String dailyType,
                                                   Map<Long, String> abnormalTargets);

    Boolean submitRepair(DailyRecordRepairSubmitParam param);

    Boolean transferRepair(DailyRecordRepairTransferParam param);

    DailyRecordExceptionResult getExceptionDetail(Long exceptionId);

    Map<Long, List<DailyRecordExceptionResult>> getResultByRecordIds(List<Long> recordIds);
}
