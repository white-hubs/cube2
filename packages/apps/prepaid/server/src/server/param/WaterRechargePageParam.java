package com.guhecloud.cube.srv.prepaid.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * 水表充值记录入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WaterRechargePageParam extends Paging {
    /**
     * 水表编号
     */
    @NotNull(message = "水表编号不能为空!")
    private String meterId;
}
