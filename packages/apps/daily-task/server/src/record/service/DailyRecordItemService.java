package com.guhecloud.cube.biz.daily.task.record.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordItemPO;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordItemResult;

import java.util.List;

public interface DailyRecordItemService extends IService<DailyRecordItemPO> {
    List<DailyRecordItemResult> getRecordItemDetail(Long taskId);

    List<DailyRecordItemResult> getDailyRecordItemResults(List<DailyRecordItemPO> recordItem);
}
