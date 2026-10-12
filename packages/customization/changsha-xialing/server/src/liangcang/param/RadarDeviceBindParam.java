package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 雷达与业务设备绑定请求体（JSON）。
 */
@Data
public class RadarDeviceBindParam {
    /** 智慧仓库仓 id。 */
    private Long warehouseId;
    /** 雷达在设备中心对应的业务设备 id。 */
    private Long radarDeviceId;
    /** 需绑定到该仓的业务设备 id 列表。 */
    private List<Long> deviceIds;
    /** 是否勾选当前查询条件下的全部可关联业务设备。 */
    private Boolean selectAllDevices;
    /** 勾选全部时使用的产品 id 集合。 */
    private Set<Long> productIds;
    /** 勾选全部时使用的区域 id 集合，按设备最末级区域匹配。 */
    private Set<Long> areaIds;
    /** 勾选全部时使用的设备名称，模糊匹配。 */
    private String deviceName;
}
