package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 电缆点位可选设备分页。 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WarehouseCableAvailableDevicePageParam extends Paging {
    /** 智慧仓库粮仓 ID。 */
    private Long warehouseId;
    /** 设备名称，模糊搜索。 */
    private String deviceName;
    /** 是否仅查看未映射设备。 */
    private Boolean onlyUnmapped;
}
