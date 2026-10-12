package com.guhecloud.cube.base.device.center.statistics.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 设备运行统计
 * @date 2024/1/22 10:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceStatisticsQueryParam extends Paging {
    /**
     * 产品品类code
     */
    private String productCategoryCode;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 在线状态
     */
    private Boolean deviceState;

    /**
     * 任意区域ID过滤
     */
    private String anyMatchedAreaId;
}
