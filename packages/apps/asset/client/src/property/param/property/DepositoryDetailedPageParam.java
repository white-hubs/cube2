package com.guhecloud.cube.property.management.client.property.param.property;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class DepositoryDetailedPageParam extends Paging {
    /**
     * 状态
     */
    private String status;
    /**
     * 入库日期
     */
    private String startTime;
    /**
     * 资产类别
     */
    private String typeId;
    /**
     * 关键字
     */
    private String keyword;
}
