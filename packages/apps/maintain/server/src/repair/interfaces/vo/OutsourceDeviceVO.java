package com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 委外设备列表
 * @date 2024/1/19 14:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OutsourceDeviceVO extends Paging {
    /**
     * 设备编码
     */
    private String deviceCode;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 设备品牌
     */
    private String deviceBrand;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 设备品类
     */
    private String deviceCategory;
    /**
     * 所属区域
     */
    private String areaName;
}
