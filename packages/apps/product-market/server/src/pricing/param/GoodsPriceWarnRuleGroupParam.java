package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author Appet
 * @Description 采价预警规则组
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPriceWarnRuleGroupParam {

    //告警规则组名称
    private String groupName;
    //true开启 false关闭
    private Boolean status;
    //备注
    private String remark;

    @Valid
    @NotNull(message = "预警规则不能为空")
    @Size(min = 1, message = "规则数据不能为空")
    List<GoodsPriceWarnRuleParam> rules;

    //通知人员
    private List<Long> userIds;

}