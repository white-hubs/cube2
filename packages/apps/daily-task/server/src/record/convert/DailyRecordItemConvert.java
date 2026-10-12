package com.guhecloud.cube.biz.daily.task.record.convert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordItemPO;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordItemParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordSubmitParam;

import java.util.HashMap;
import java.util.List;

public class DailyRecordItemConvert {
    /**
     * 构建记录项对象
     *
     * @param param    提交参数对象，包含任务ID和任务名称等信息
     * @param item     记录项参数对象，包含标准ID、项目ID、结果ID等详细信息
     * @param recordId 记录ID
     *
     * @return 构建完成的记录项持久化对象
     */
    public static DailyRecordItemPO buildRecordItem(DailyRecordSubmitParam param, DailyRecordItemParam item, Long recordId,
                                                    HashMap<Long, List<Long>> fileRefMap
                                                   ) {
        // 使用建造者模式构建DailyRecordItemPO对象，设置各项属性包括：
        // 基本信息（ID、任务信息、记录ID）、标准信息、项目信息、结果信息等
        var fileRefKey = IdWorker.getId();
        fileRefMap.put(fileRefKey, item.getFileIdList());
        return DailyRecordItemPO.builder().id(IdWorker.getId()).taskId(param.getTaskId()).taskName(param.getTaskName()).recordId(recordId)
                .fileRefKey(fileRefKey)
                .itemId(item.getItemId()).itemName(item.getItemName()).itemTag(item.getItemTag()).remark(item.getRemark())
                .defaultResultItem(item.getDefaultResultItem()).mustRemarkResultItems(item.getMustRemarkResultItems())
                .mustTakePhotoResultItems(item.getMustTakePhotoResultItems()).resultId(item.getResultId()).resultName(item.getResultName())
                .resultItemId(item.getResultItemId()).resultItemName(item.getResultItemName()).errorFlag(item.getErrorFlag()).build();
    }
}
