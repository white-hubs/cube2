package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 消防设备
 * @date 2023/9/8 14:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FireFightingDeviceVO {
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 租户编号
     */
    private String marketTenant;
    /**
     * 所属市场
     */
    private String marketName;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 归属部门
     */
    private String dep;
    /**
     * 所在区域
     */
    private String area;
    /**
     * 状态
     */
    private String state;

}
