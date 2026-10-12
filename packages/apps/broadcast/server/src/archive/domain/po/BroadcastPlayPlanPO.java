package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 广播播放计划
 *
 * @author lanchun
 * @date 2023/3/23 11:43
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("broadcast_play_plan")
public class BroadcastPlayPlanPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 播放参数
     */
    private String playParam;

    /**
     * 当前播放任务id
     */
    private String currentPlayTextTaskId;
}
