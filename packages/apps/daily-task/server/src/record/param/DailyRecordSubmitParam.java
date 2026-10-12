package com.guhecloud.cube.biz.daily.task.record.param;

import lombok.Data;

import java.util.List;

/**
 * 批量提交执行记录参数
 */
@Data
public class DailyRecordSubmitParam {
    /**
     * 日常任务类型
     */
    private String dailyType;
    private Long taskId;
    private String taskName;
    /**
     * 说明
     */
    private String remark;

    /**
     * 文件
     */
    private List<Long> fileIds;

    /**
     * 记录项
     */
    private List<DailyRecordItemParam> recordItemList;
    /**
     * 记录对象
     */
    private List<DailyRecordTargetParam> recordTargetList;
}
