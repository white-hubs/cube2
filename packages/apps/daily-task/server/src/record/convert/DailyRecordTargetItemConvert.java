package com.guhecloud.cube.biz.daily.task.record.convert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetItemPO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetPO;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordTargetItemParam;

public class DailyRecordTargetItemConvert {
    /**
     * 构建记录目标项PO对象
     *
     * @param taskId       任务ID
     * @param taskName     任务名称
     * @param recordTarget 对象信息
     * @param targetItem   目标项参数对象，包含标准、项目、结果等相关信息
     * @param recordId     记录ID
     * @param fileRefId    文件引用ID
     *
     * @return 构建完成的DailyRecordTargetItemPO对象
     */
    public static DailyRecordTargetItemPO builderRecordTargetItem(Long taskId, String taskName, Long recordId,
                                                                  DailyRecordTargetPO recordTarget,
                                                                  DailyRecordTargetItemParam targetItem, long fileRefId
                                                                 ) {
        // 使用建造者模式构建DailyRecordTargetItemPO对象，设置各项属性值
        return DailyRecordTargetItemPO.builder().id(IdWorker.getId()).taskId(taskId)
                .taskName(taskName)
                .recordId(recordId).standardId(targetItem.getStandardId())
                .targetId(recordTarget.getTargetId())
                .targetCategory(recordTarget.getTargetCategory())
                .targetCode(recordTarget.getTargetCode())
                .targetName(recordTarget.getTargetName())
                .standardName(targetItem.getStandardName())
                .itemId(targetItem.getItemId()).itemName(targetItem.getItemName())
                .itemTag(targetItem.getItemTag())
                .remark(targetItem.getRemark()).defaultResultItem(targetItem.getDefaultResultItem())
                .mustRemarkResultItems(targetItem.getMustRemarkResultItems())
                .mustTakePhotoResultItems(targetItem.getMustTakePhotoResultItems())
                .resultId(targetItem.getResultId()).fileRefKey(fileRefId)
                .resultName(targetItem.getResultName())
                .resultItemId(targetItem.getResultItemId())
                .resultItemName(targetItem.getResultItemName())
                .errorFlag(Boolean.TRUE.equals(targetItem.getErrorFlag())).build();
    }
}
