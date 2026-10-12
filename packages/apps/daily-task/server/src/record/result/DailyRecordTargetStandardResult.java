package com.guhecloud.cube.biz.daily.task.record.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 关联的标准返回对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordTargetStandardResult {
    /**
     * 关联的主id
     */
    private Long masterId;
    private Long standardId;
    private String standardName;

    private List<DailyRecordItemResult> itemList;

}
