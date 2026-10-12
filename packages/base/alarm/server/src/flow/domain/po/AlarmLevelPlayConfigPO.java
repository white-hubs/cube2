package com.guhecloud.cube.base.alarm.server.flow.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 报警级别播放配置
 *
 * @author lanchun
 * @date 2024/12/4 10:20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("alarm_level_play_config")
public class AlarmLevelPlayConfigPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 报警级别编号
     */
    private String alarmLevelCode;

    /**
     * 是否循环播放
     */
    private Boolean looped;

    /**
     * 播放次数
     */
    private Integer playNum;
}
