package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStateEventEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 报警记录事件操作数据
 *
 * @author lanchun
 * @date 2022/6/6 10:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmStateEventOperateVO {

    private Long id;

    /**
     * 报警记录主键
     */
    private Long alarmRecordId;

    /**
     * 报警状态
     */
    private AlarmStatusEnum alarmStatus;

    /**
     * 状态事件
     */
    private AlarmStateEventEnum alarmStatusEvent;

    /**
     * 操作参数
     */
    private Object operationParam;

    /**
     * 操作时间
     */
    private LocalDateTime createTime;
}
