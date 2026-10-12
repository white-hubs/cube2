package com.guhecloud.cube.biz.daily.task.record.param;

import lombok.Data;

import java.util.List;

@Data
public class DailyRecordTargetParam {
    private String targetCategory;
    private Long targetId;
    private List<DailyRecordTargetItemParam> targetItemList;
    /**
     * 需要复制的其他对象id集合
     */
    private List<Long> otherTargetIdList;
}
