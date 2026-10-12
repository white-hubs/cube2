package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 雷达与业务设备绑定编辑请求体。
 * 当前无 HTTP 入口，供服务层按原绑定与新绑定差量调整关系。
 */
@Data
public class RadarDeviceEditParam {
    /** 原绑定智慧仓库 id。 */
    private Long originalWarehouseId;
    /** 原绑定雷达在设备中心对应的业务设备 id。 */
    private Long originalRadarDeviceId;
    /** 新智慧仓库 id。 */
    private Long newWarehouseId;
    /** 新雷达在设备中心对应的业务设备 id。 */
    private Long newRadarDeviceId;
    /** 编辑后绑定到新仓的业务设备 id 列表。 */
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
