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
 * 报警记录发送对象数据模型
 *
 * @author lanchun
 * @date 2022/6/6 10:25
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("alarm_record_target")
public class AlarmRecordTargetPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 报警记录主键
     */
    private Long alarmRecordId;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
}
