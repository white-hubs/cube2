package com.guhecloud.cube.srv.gen.property.management.server.consumable.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.filter.ConsumableCurrentUserDeptFieldFilter;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.filter.ConsumableCustomDeptFieldFilter;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.filter.ConsumableUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 耗材管理
 * @date 2023-03-16 10:08:36
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DataPermission(value = {
        @DataPermissionField(permission = "CURRENT_USER", field = "create_by", fieldFilter = ConsumableCurrentUserDeptFieldFilter.class),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "create_by", fieldFilter = ConsumableUserDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "create_by", fieldParamConvert = ConsumableCustomDeptFieldFilter.class)
}
)
@TableName("consumable")
public class Consumable extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //名称
    private String name;
    //编码
    private String code;
    //类型ID
    private Long categoryId;
    //品牌
    private String brand;
    //型号
    private String model;
    //商品码
    private String goodsCode;
    //单位
    private String unit;
    //质保期限
    private String qualityPeriod;
    //库存上线
    private Integer stockMax;
    //库存下线
    private Integer stockMin;
    //限领数量（人/月）
    private Integer useLimit;
    //当前库存数量
    private Integer stock;
    //总金额
    private BigDecimal amount;
    //附件
    private String fileIds;
    private String status;
}