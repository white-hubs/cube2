package com.guhecloud.cube.biz.collection.analysis.collection.schedule.dto;

import com.guhecloud.cube.biz.collection.analysis.collection.ICollectionProcessor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 调度器配置
 *
 * @author lanchun
 * @date 2024/12/9 10:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleJob<REQ, REP> {

    /**
     * 任务id
     */
    private Long id;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 任务执行
     */
    private ICollectionProcessor<REQ, REP> collectionProcessor;
}
