package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import lombok.Data;

/**
 * @author Appet
 * @Description 采价预警通知
 * @date 2023-06-05 15:08:36
 */
@Data
public class GoodsPriceWarnUserParam {

    //规则组ID
    private Long groupId;
    //用户ID
    private Long userId;
    //部门ID
    private Long deptId;
    //角色ID
    private Long roleId;

}