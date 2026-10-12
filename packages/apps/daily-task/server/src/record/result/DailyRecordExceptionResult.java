package com.guhecloud.cube.biz.daily.task.record.result;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DailyRecordExceptionResult {
    private Long id;
    private Long recordId;
    private Long taskId;
    private Long targetId;
    private String targetName;
    private String status;
    private Long repairUserId;
    private String repairUserName;
    private LocalDateTime repairedTime;
    private Boolean allowRepair;
    private List<DailyRecordRepairResult> repairList;
}
