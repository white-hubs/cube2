package com.guhecloud.cube.biz.adapter.park.server.facade.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 费用统计返回对象
 *
 * @author lanchun
 * @date 2022/10/8 15:26
 */
@Data
public class ChargeTotalResponse {

    /**
     * 临时车辆缴费笔数
     */
    @JsonProperty("paynum_t")
    private Integer payNumTemp;

    /**
     * 临时车辆缴费总额
     */
    @JsonProperty("payfee_t")
    private String payAmount;

    /**
     * 临时车辆优惠总额
     */
    @JsonProperty("paydiscfee_t")
    private String payDiscAmountTemp;

    /**
     * 套餐车充值笔数
     */
    @JsonProperty("paynum_m")
    private Integer payNumMeal;

    /**
     * 套餐车充值总额
     */
    @JsonProperty("payfee_m")
    private String payAmountMeal;
}
