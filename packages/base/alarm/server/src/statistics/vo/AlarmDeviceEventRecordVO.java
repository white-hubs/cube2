package com.guhecloud.cube.base.alarm.server.statistics.vo;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmDeviceEventRecordVO {
    private LocalDateTime alarmTime;
    private String alarmEventName;
    private String alarmLevelName;
    private AlarmStatusEnum alarmStatusEnum;
}
