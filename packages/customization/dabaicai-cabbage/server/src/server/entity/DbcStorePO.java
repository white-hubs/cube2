package com.guhecloud.cube.srv.gen.cabbage.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 大白菜挡位
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("dbc_store")
@EqualsAndHashCode(callSuper = true)
public class DbcStorePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    // 档位编号
    private String storefrontCode;
    //档位名称
    private String storefrontName;
    //档位别名
    private String alias;
    //所属区域序号
    private String areaCode;
    //所属区域名称
    private String areaName;
    //档位类型序号
    private String storefrontTypeCode;
    //档位类型名称
    private String storefrontTypeName;
    //所属部门序号
    private String departmentCode;
    //所属部门名称
    private String departmentName;
    //档主名称
    private String customerName;
    //档主联系手机号
    private String customerMobile;
    //市场序号
    private Integer marketId;
    //市场名称
    private String marketName;

    // 对应古河云部门Id(全路径部门 用逗号隔开)
    private String organizationIds;
    // 对应古河云区域Id(全路径区域 用逗号隔开)
    private String areaIds;
    // 对应古河云挡位类型id
    private Long stallCategoryId;
}
