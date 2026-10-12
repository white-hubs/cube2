package com.guhecloud.cube.biz.meattrade.server.base.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * 字典信息
 */
@Data
public class Dictionary extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 类型
     */
    private String type;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Float sort;
}
