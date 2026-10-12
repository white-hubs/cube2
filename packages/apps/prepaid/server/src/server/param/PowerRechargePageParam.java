package com.guhecloud.cube.srv.prepaid.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * 电表充值记录入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PowerRechargePageParam extends Paging {
    /**
     * 电表编号
     */
    @NotNull(message = "电表编号不能为空!")
    private String meterId;
    /**
     * 时间段
     */
    private Period period;
}
