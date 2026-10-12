package com.guhecloud.cube.biz.daily.task.record.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordWorkOrderMapParam {
    private Set<Long> taskIds;
    private Set<Long> recordIds;
    private Set<Long> targetIds;
    private Set<Long> workOrderIds;
}
