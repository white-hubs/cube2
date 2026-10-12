package com.guhecloud.cube.biz.daily.task.record.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetItemPO;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordTargetStandardResult;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface DailyRecordTargetItemService extends IService<DailyRecordTargetItemPO> {
    List<DailyRecordTargetStandardResult> getRecordTargetItemList(List<DailyRecordTargetItemPO> recordTargetItemPOList);

    Map<String, List<DailyRecordTargetItemPO>> getRecordTargetItemMap(HashSet<Long> ids, HashSet<Long> taskIds, HashSet<Long> targetIds,
                                                                      Boolean errorFlag
                                                                     );

    Map<Long, List<DailyRecordTargetStandardResult>> getRecordTargetStandardMap(
            List<DailyRecordTargetItemPO> recordTargetItemPOList
                                                                               );
}
