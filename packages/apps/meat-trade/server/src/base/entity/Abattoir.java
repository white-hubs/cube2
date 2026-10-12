package com.guhecloud.cube.biz.meattrade.server.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 屠宰场信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "abattoir")
public class Abattoir extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 屠宰场编号
     */
    private String code;
    /**
     * 屠宰场名称
     */
    private String name;
    /**
     * 屠宰场简称
     */
    private String abbr;
    /**
     * 屠宰场地址
     */
    private String address;
    /**
     * 备注信息
     */
    private String remark;
    /**
     * 是否启用
     */
    private Boolean enabled;
}
