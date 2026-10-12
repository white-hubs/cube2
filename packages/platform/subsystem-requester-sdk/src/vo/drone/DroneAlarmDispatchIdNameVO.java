package com.guhecloud.subsystem.requester.sdk.vo.drone;

import lombok.Data;

/**
 * 无人机报警派飞ID和名称回显快照。
 */
@Data
public class DroneAlarmDispatchIdNameVO {
    /** 业务主键。 */
    private Long id;
    /** 展示名称。 */
    private String name;
}
