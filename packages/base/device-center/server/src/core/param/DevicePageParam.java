package com.guhecloud.cube.base.device.center.core.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DevicePageParam extends Paging {
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 区域id
     */
    private String lastAreaId;
    /**
     * 部门id
     */
    private Long departmentId;
    /**
     * 产品品类
     */
    private String prodCategoryCode;
    /**
     * 设备id集合
     */
    private List<Long> filterIds;
    /**
     * 设备id
     */
    private Long filterDeviceId;
}
