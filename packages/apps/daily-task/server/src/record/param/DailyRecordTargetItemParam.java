package com.guhecloud.cube.biz.daily.task.record.param;

import lombok.Data;

import java.util.List;

/**
 * 执行记录目标项参数
 */
@Data
public class DailyRecordTargetItemParam {
    /**
     * 记录id
     */
    private Long recordId;
    /**
     * 对象类别
     * (设备/档位/现场点/资产/风险源/小散工程)
     */
    private Long targetId;
    private Long standardId;
    private String standardName;
    private Long itemId;
    private String itemName;
    private String itemTag;
    private String remark;
    private Long resultId;
    private String resultName;
    private Long resultItemId;
    private String resultItemName;
    private Boolean errorFlag;
    private List<Long> fileIdList;

    /**
     * 默认结果项
     */
    private String defaultResultItem;
    /**
     * 必须拍照的结果项
     */
    private String mustTakePhotoResultItems;
    /**
     * 必须备注的结果项
     */
    private String mustRemarkResultItems;
}
