package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 建档设备
 * @date 2023/9/8 14:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FilingDeviceVO {
    /**
     * 资产名称
     */
    private String deviceName;
    private Integer marketTenant;
    /**
     * 所属市场
     */
    private String marketName;
    /**
     * 资产类别
     */
    private String deviceType;
    /**
     * 入库日期
     */
    private LocalDateTime putInDate;
    /**
     * 维保到期时间
     */
    private LocalDateTime expireDate;
    /**
     * 资产状态
     */
    private String state;
    /**
     * 管理员
     */
    private String manager;
    /**
     * 所在区域
     */
    private String area;
    /**
     * 使用人
     */
    private String deviceUser;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 模型编码
     */
    private String bimCode;
    /**
     * 风险源编码
     */
    private String serialNumber;
    /**
     * 属性
     */
    private String attribute;
    /**
     * 档位名称
     */
    private String stallName;
    /**
     * 现场点名称
     */
    private String sitePointName;
    private String maintenanceDepName;
    private String receiveDepName;
    private String propertyName;
    private String address;

    /**
     * 电动车分类
     */
    private String electronMobileType;
}
