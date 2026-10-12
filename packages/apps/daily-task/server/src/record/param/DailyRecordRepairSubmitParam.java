package com.guhecloud.cube.biz.daily.task.record.param;

import lombok.Data;

import java.util.List;

@Data
public class DailyRecordRepairSubmitParam {
    private Long exceptionId;
    private String executionResult;
    private String remark;
    private List<Long> fileIds;
}
