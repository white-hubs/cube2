package com.guhecloud.cube.biz.emergency.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 应急物资类型管理
 * @date 2024-01-016 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmergencySuppliesType extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //名称
    private String name;
    //编号
    private String code;
    //状态
    private Boolean status;

}