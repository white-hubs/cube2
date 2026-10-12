package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 采价预警通知
 * @date 2023-06-05 15:08:36
 */
@Data
public class GoodsPriceWarnUserVO {

    //
    private Long id;
    //规则组ID
    private Long groupId;
    //用户ID
    private Long userId;
    //用户名称
    private String userName;
    //部门ID
    private Long deptId;
    //角色ID
    private Long roleId;

}