package com.guhecloud.cube.srv.gen.cabbage.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 大白菜区域
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("dbc_area")
@EqualsAndHashCode(callSuper = true)
public class DbcAreaPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 市场编号
     */
    private Integer marketId;
    // 所属区域序号
    private String areaCode;
    //所属区域名称
    private String areaName;
    // 对应古河云区域Id(全路径区域 用逗号隔开)
    private String areaIds;
}
