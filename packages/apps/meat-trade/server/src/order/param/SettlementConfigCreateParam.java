package com.guhecloud.cube.biz.meattrade.server.order.param;

import com.guhecloud.cube.biz.meattrade.server.order.entity.SettlementConfig;
import com.guhecloud.cube.biz.meattrade.server.order.enumeration.SettlementConfigEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 结算配置新增
 */
@Data
public class SettlementConfigCreateParam {

    /**
     * 配置规则
     */
    @NotNull(message = "配置规则不能为空")
    List<SettlementConfig> rules;
    /**
     * 计算名称
     */
    @NotNull(message = "结算配置名称不能为空")
    private String name;
    /**
     * 计算方式
     */
    @NotNull(message = "计算方式不能为空")
    private SettlementConfigEnum way;
    /**
     * 是否选中
     */
    @NotNull(message = "是否选中不能为控股")
    private Boolean flag;

}
