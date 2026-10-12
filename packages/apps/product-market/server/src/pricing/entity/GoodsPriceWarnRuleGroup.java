package com.guhecloud.cube.srv.gen.product.market.server.pricing.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 采价预警规则组
 * @date 2023-05-16 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsPriceWarnRuleGroup extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //告警规则组名称
    @Unique(composites = { "deleted" }, message = "规则名称重复")
    private String groupName;
    //创建人姓名
    private String creatorName;
    //tru开启 false关闭
    private Boolean status;
    //备注
    private String remark;

}