package com.guhecloud.cube.base.device.center.statistics.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 设备运行统计
 * @date 2024/1/22 10:34
 */
@Data
public class DeviceStatisticsNumVO {

    /**
     * 设备品类名称
     */
    private String deviceProdCategoryName;
    /**
     * 设备品类编码
     */
    private String deviceProdCategoryCode;
    /**
     * 设备总数
     */
    private Integer totalNumber;
    /**
     * 在线数量
     */
    private Integer normalNumber;
    /**
     * 离线数量
     */
    private Integer offlineNumber;
}
