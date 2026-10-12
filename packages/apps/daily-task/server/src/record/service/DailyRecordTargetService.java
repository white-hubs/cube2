package com.guhecloud.cube.biz.daily.task.record.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetPO;
import com.guhecloud.cube.biz.daily.task.record.dto.DailyRecordTargetErrorDTO;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordDetailParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordTargetBatchParam;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordTargetDetailResult;

import java.util.List;

public interface DailyRecordTargetService extends IService<DailyRecordTargetPO> {
    DailyRecordTargetDetailResult getRecordTargetDetail(DailyRecordDetailParam param);

    List<DailyRecordTargetErrorDTO> batchRecordTargetErrorList(DailyRecordTargetBatchParam param);
}
