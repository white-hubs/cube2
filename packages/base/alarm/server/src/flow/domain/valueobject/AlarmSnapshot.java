package com.guhecloud.cube.base.alarm.server.flow.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报警快照
 *
 * @author lanchun
 * @date 2023/2/28 11:36
 */
@Getter
@AllArgsConstructor
public class AlarmSnapshot {

    /**
     * 报警的iot快照
     */
    private String snapshot;

    /**
     * 触发报警信息
     */
    private String alarmSnapshot;

    /**
     * 业务快照
     */
    private String businessSnapshot;

    public void changeAlarmSnapshot(String alarmSnapshot) {
        this.alarmSnapshot = alarmSnapshot;
    }
}
