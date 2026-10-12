package com.guhecloud.cube.biz.daily.task.record.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 记录对象详情返回对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordTargetDetailResult {
    /**
     * 标准列表
     */
    private List<DailyRecordTargetStandardResult> standardList;
    /**
     * 工单列表
     */
    private List<DailyRecordWorkOrderResult> workOrderList;
}
