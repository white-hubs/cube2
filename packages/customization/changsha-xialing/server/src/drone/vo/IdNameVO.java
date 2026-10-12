package com.guhecloud.cube.customization.xialing.server.drone.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ID和名称回显快照。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdNameVO {
    /** 业务主键。 */
    private Long id;
    /** 展示名称。 */
    private String name;
    /** 设备编号快照，报警对象为设备时返回。 */
    private String code;
    /** 区域ID快照，报警对象为设备时返回。 */
    private String areaId;
    /** 区域名称快照，报警对象为设备时返回。 */
    private String areaName;

    /**
     * 构造基础ID名称快照。
     */
    public IdNameVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
