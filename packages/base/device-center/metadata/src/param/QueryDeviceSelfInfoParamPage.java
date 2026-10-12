package com.guhecloud.cube.base.device.center.metadata.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryDeviceSelfInfoParamPage extends Paging {
    private String keyword;

    /**
     * 相当于原来的 model
     */
    private long productId;

    private Long genreId;

    private Long areaId;

    private List<Long> filteredDeviceIds;
}