package com.guhecloud.cube.srv.prepaid.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 水表分页入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WaterMeterPageParam extends Paging {
    /**
     * 水表编号
     */
    private String meterId;
    /**
     * 是否失联
     */
    private Boolean unConnect;
    /**
     * 是否欠费
     */
    private Boolean oweStatus;
}
