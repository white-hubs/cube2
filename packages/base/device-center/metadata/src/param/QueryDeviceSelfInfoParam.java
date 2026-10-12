package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;

import java.util.List;

@Data
public class QueryDeviceSelfInfoParam {
    private String keyword;

    /**
     * 相当于原来的 model
     */
    private long productId;

    //类型id -> device_biz_type
    private Long genreId;

    private List<Long> filteredDeviceIds;
}