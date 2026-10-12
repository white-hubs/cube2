package com.guhecloud.cube.biz.emergency.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 应急组织管理
 * @date 2024-01-22 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmergencyOrganization extends BasicEnhancedTenantTreeEntity<Long> {

    //
    private Long id;
    //名称
    private String name;
    //父级ID
    private Long parentId;
    //等级
    private Integer level;
    //序列
    private String sequence;

}