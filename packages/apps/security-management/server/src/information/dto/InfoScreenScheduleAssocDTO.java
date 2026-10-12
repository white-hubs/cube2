package com.guhecloud.cube.srv.gen.securitymanagement.server.information.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息发布屏与开关屏配置管理 关联实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoScreenScheduleAssocDTO {
    /**
     * 关联主键id
     */
    private Long id;
    /**
     * 开关屏配置管理主键id
     */
    private Long informationPlanId;
    /**
     * 基础设备id
     */
    private Long securityDeviceId;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 租户编号
     */
    private Integer tenant;
}
