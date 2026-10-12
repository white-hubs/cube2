package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 粮仓业务设备解绑请求体。
 */
@Data
public class WarehouseUnbindParam {
    /** 智慧仓 id。 */
    private Long warehouseId;
    /** 要解绑的业务设备 id 列表。 */
    private List<Long> deviceIds;
    /** 是否按当前查询条件解绑全部已绑定业务设备。 */
    private Boolean selectAllDevices;
    /** 勾选全部时使用的产品 id 集合。 */
    private Set<Long> productIds;
    /** 勾选全部时使用的区域 id 集合，按设备最末级区域匹配。 */
    private Set<Long> areaIds;
    /** 勾选全部时使用的设备名称，模糊匹配。 */
    private String deviceName;
}
