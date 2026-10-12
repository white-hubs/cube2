package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

/**
 * 报警派飞配置保存用ID和名称快照。
 */
@Data
public class DroneAlarmDispatchIdNameParam {
    /** 业务主键。 */
    private Long id;
    /** 展示名称快照。 */
    private String name;
}
