package com.guhecloud.cube.srv.gen.product.market.server.pricing.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 采价记录
 * @date 2023-05-16 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsPricingRecord extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //采价任务ID
    private Long taskId;
    private String taskName;
    //采价员ID
    private Long userId;
    //采价员姓名
    private String userName;
    //采价员电话
    private String userPhone;
    //部门ID
    private Long deptId;
    //部门名称
    private String deptName;
    //商品品类ID
    private Long goodsCategoryId;
    //商品品类名称
    private String goodsCategoryName;
    //品类父级IDS
    private String goodsCategoryIds;
    //档位编码
    private String stallCode;
    //档主名称
    private String stallBoss;
    //档主联系电话
    private String stallBossPhone;
    //商品来源（产地）
    private String goodsFrom;
    //最高价
    private BigDecimal maxPrice;
    //中间价
    private BigDecimal midPrice;
    //最低价
    private BigDecimal minPrice;
    //备注
    private String remark;

}