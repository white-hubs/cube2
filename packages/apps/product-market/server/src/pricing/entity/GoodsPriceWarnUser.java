package com.guhecloud.cube.srv.gen.product.market.server.pricing.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 采价预警通知
 * @date 2023-06-05 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsPriceWarnUser extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //规则组ID
    private Long groupId;
    //用户ID
    private Long userId;
    //用户姓名
    private String userName;
    //部门ID
    private Long deptId;
    //角色ID
    private Long roleId;

}