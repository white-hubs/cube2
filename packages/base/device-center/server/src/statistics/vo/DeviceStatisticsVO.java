package com.guhecloud.cube.base.device.center.statistics.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wql
 * @describe 设备运行统计
 * @date 2024/1/22 10:34
 */
@Data
public class DeviceStatisticsVO {

    /**
     * 设备id
     */
    private Long id;
    /**
     * 设备编号
     */
    private String deviceCode;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 所属产品
     */
    private String productName;
    /**
     * 产品品类
     */
    private String productCategoryName;

    /**
     * 当前状态
     */
    private boolean onlineState;

    /**
     * 区域id
     */
    private Long areaId;

    /**
     * 在线时长
     */
    private Long onlineTime;
    /**
     * 离线时长
     */
    private Long offlineTime;
    /**
     * 离线次数
     */
    private Integer offLineNum;
    /**
     * 报警次数
     */
    private Integer alarmNum;
    /**
     * 故障次数
     */
    private Integer faultNum;
    /**
     * 故障率
     */
    private BigDecimal faultRate;
}
