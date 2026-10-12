package com.guhecloud.cube.base.device.center.metadata.param;

import com.guhecloud.cube.base.device.center.metadata.lang.DeviceSortFieldEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.SortEnum;
import lombok.Data;

/**
 * 设备排序参数
 *
 * @author lanchun
 * @date 2025/4/16 15:41
 */
@Data
public class DeviceSortParam {

    /**
     * 排序字段
     */
    private DeviceSortFieldEnum sortField;

    /**
     * 排序方式
     */
    private SortEnum sort;
}
