package com.guhecloud.cube.iot.mgt.core.data.vo.cache;

import com.guhecloud.cube.iot.mgt.core.data.lang.AccProtocolEnum;
import lombok.Data;

@Data
public class DeviceDigestVo { //摘要
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备编号
     */
    private String deviceNumber;
    /**
     * 父设备ID
     */
    private String parentDeviceId;

    private String parentDeviceNumber;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 品类ID
     */
    private Long productCategoryId;

    /**
     * 产品品牌
     */
    private String productBrandCode;

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 产品版本号
     */
    private String prodVersion;

    /**
     * 平台接入适配协议{@link AccProtocolEnum}
     */
    private Integer protocolId;

    /**
     * 节点类型
     */
    private Integer nodeType;

}
