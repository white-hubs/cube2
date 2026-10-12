package com.guhecloud.cube.srv.gen.reportmanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 工作安排类型
 * @date 2024-01-04 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkPlanType extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //编码
    private String code;
    //类型名称
    private String name;
    //状态 0 禁用 1 启用
    private Boolean status;

}