package com.guhecloud.cube.customization.xialing.server.drone.param;

import lombok.Data;

/**
 * 前端提交的ID和名称快照。
 */
@Data
public class IdNameParam {
    /** 业务主键。 */
    private Long id;
    /** 展示名称快照。 */
    private String name;
    /** 设备编号快照，报警对象为设备时使用。 */
    private String code;
    /** 区域ID快照，报警对象为设备时使用。 */
    private String areaId;
    /** 区域名称快照，报警对象为设备时使用。 */
    private String areaName;
}
