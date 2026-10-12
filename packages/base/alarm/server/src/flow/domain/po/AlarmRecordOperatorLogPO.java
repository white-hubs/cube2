package com.guhecloud.cube.base.alarm.server.flow.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 报警操作日志
 *
 * @author lanchun
 * @date 2022/6/14 15:14
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("alarm_record_operator_log")
public class AlarmRecordOperatorLogPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 报警记录主键
     */
    private Long alarmRecordId;

    /**
     * 操作类型
     */
    private AlarmOperatorTypeEnum operatorType;

    /**
     * 操作人
     */
    private String operatorBy;

    /**
     * 操作人姓名
     */
    private String operatorByName;

    /**
     * 操作时间
     */
    private LocalDateTime operatorTime;

    /**
     * 操作内容
     */
    private String operatorContent;

    /**
     * 操作备注
     */
    private String remark;
}
