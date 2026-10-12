package com.guhecloud.cube.biz.daily.task.record.param;

import lombok.Data;

import java.util.List;

@Data
public class DailyRecordItemParam {
    private Long recordId;
    private String dailyType;
    private Long taskId;
    private String taskName;
    private Long itemId;
    private String itemName;
    private String itemTag;
    private Long resultId;
    private String resultName;
    private Long resultItemId;
    private String resultItemName;
    private Boolean errorFlag;
    private String remark;
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
