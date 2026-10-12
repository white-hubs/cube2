package com.guhecloud.cube.customization.xialing.server.drone.param;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 霞凝快速任务启动参数。
 */
@Data
public class DroneQuickTaskStartParam {
    /** 目标经度。 */
    private BigDecimal longitude;
    /** 目标纬度。 */
    private BigDecimal latitude;
    /** 机库编号。 */
    private String nestSn;
    /** 快速任务名称。 */
    private String taskName;
    /** 场景ID，可选。 */
    private Long sceneId;
    /** 场景编号，可选。 */
    private String sceneCode;
    /** 完成动作。 */
    private Integer finishAction;
    /** 飞行高度。 */
    private BigDecimal flyHeight;
}
