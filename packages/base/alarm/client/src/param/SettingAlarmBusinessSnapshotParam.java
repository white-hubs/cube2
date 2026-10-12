package com.guhecloud.cube.base.alarm.client.param;

import lombok.Data;

@Data
public class SettingAlarmBusinessSnapshotParam {

    /**
     * 报警主键
     */
    private Long id;

    /**
     * 业务快照信息
     */
    private String businessSnapshot;
}
