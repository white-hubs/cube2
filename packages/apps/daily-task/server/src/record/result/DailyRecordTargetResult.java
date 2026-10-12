package com.guhecloud.cube.biz.daily.task.record.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordTargetResult {
    private Long masterId;
    private String targetCategory;
    private Long targetId;
    private String targetName;
    private List<DailyRecordTargetStandardResult> standardResultList;
    private DailyRecordExceptionResult exception;
}
