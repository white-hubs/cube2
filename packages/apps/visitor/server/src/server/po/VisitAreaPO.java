package com.guhecloud.cube.srv.gen.visitor.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 访问区域
 */
@Data
@TableName("visit_area")
@EqualsAndHashCode(callSuper = true)
public class VisitAreaPO extends BasicEnhancedTenantEntity<Long> {
    private static final long serialVersionUID = 7061184311405177413L;
    private Long id;
    private Long areaId;
    private String areaName;
    /**
     * 关联设备数
     */
    private Integer deviceNumber;
    /**
     * 访问时段（列表使用
     */
    private String visitPeriods;
    private String createUserName;
    private Boolean normal;
}
