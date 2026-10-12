package com.guhecloud.cube.biz.daily.task.record.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordPO;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyRecordResult;
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
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface DailyRecordService extends IService<DailyRecordPO> {
    Pagination<DailyRecordPageResult> pageRecord(DailyRecordPageParam param);

    DailyRecordDetail detailRecord(DailyRecordDetailParam param);

    List<DailyRecordExportResult> getExportRecordDataList(DailyRecordPageParam param);

    DailyRecordSubmitResult submitRecord(DailyRecordSubmitParam param);

    Boolean submitRepair(DailyRecordRepairSubmitParam param);

    Boolean transferRepair(DailyRecordRepairTransferParam param);

    DailyRecordExceptionResult getRepairDetail(Long exceptionId);

    DailyRecordResult getNormalTaskRecord(Long id);
}
