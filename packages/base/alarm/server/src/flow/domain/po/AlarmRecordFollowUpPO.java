package com.guhecloud.cube.base.alarm.server.flow.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 报警记录跟进信息
 *
 * @author lanchun
 * @date 2024/11/25 15:07
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("alarm_record_follow_up")
public class AlarmRecordFollowUpPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 报警记录主键
     */
    private Long alarmRecordId;

    /**
     * 内容
     */
    private String content;

    /**
     * 跟进人主键
     */
    private Long followUpUserId;

    /**
     * 跟进人名称
     */
    private String followUpUserName;

    /**
     * 跟进时间
     */
    private LocalDateTime followUpTime;
}
