package com.guhecloud.cube.base.device.center.core.dto;

import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import lombok.Data;

import java.util.List;

@Data
public class DevicePageDto {
    private Long id;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备编码(孪生编码)
     */
    private String deviceCode;

    /**
     * 设备业务类型
     */
    private String deviceBizType;
    /**
     * 设备业务类型id
     */
    private Long deviceBizTypeId;

    /**
     * 归属部门名称
     */
    private String departmentName;

    /**
     * 区域ID名称,--分割
     */
    private String areaName;

    /**
     * 区域ID名称,--分割ids
     */
    private String areaId;

    /**
     * 区域信息对<br/>
     * key:区域ID<br/>
     * val:区域名称<br/>
     */
    private List<Pair<String, String>> areaPairs;

    /**
     * 0 离线 1在线
     */
    private Integer deviceState;

    /**
     * 型号名称
     */
    private String iotDeviceModelName;
    /**
     * 品牌名称
     */
    private String iotDeviceBrandName;
    /**
     * 产品ID
     */
    private String iotDeviceProductId;
    /**
     * 产品名称
     */
    private String iotDeviceProductName;

    /**
     * 品类ID
     */
    private String iotDeviceCategoryId;

    /**
     * 品类名称
     */
    private String iotDeviceCategoryName;

    /**
     * 品类级联
     */
    private String iotDeviceCategoryNameConcat;

    /**
     * 第三id
     */
    private String thirdPartyId;

    /**
     * 物联平台设备唯一编码 分页中提供，但这里有代码侵入
     */
    private String iotDeviceNumber;
}
