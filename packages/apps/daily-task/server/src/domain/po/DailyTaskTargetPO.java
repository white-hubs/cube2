package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.daily.task.status.base.BaseState;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 任务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_task_target")
@EqualsAndHashCode(callSuper = true)
public class DailyTaskTargetPO extends BasicEnhancedTenantEntity<Long> implements BaseState {
    private Long id;
    private Long taskId;
    private String targetCategory;
    private Long targetId;
    private String targetName;
    /**
     * 规则ID
     */
    private Long ruleId;
    /**
     * 标签ID
     */
    private String tagIds;

    /**
     * 快照
     */
    private Boolean snapshot;
    /**
     * 是否被分发
     */
    private Boolean distributed;
    /**
     * 对象状态
     */
    private String status;

    private String targetCode;

    /**
     * 所属区域
     */
    private String areaName;

    /**
     * 详细地址
     */
    private String location;
    /**
     * 经纬度(例如 104.05573200000003,30.586921999999998)
     */
    private String targetLntAndLat;
}
