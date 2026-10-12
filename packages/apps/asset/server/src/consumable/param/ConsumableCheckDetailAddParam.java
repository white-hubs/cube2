package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @author Appet
 * @Description 耗材盘点明细
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableCheckDetailAddParam {

    //盘点单ID
    @NotNull(message = "盘点单ID不能为空")
    private Long checkId;
    //耗材ID
    @NotNull(message = "耗材ID不能为空")
    private Long consumableId;
    //盘点人ID
    private Long checkerId;
    //当前数量
    private Integer num;
    //盘点数量
    private Integer checkNum;
    private String status;

}