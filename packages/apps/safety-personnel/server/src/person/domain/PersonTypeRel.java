package com.guhecloud.cube.srv.gen.safety.personnel.server.person.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安全人员类型关系。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sp_person_type_rel")
public class PersonTypeRel extends BasicEnhancedTenantEntity<Long> {

    /**
     * 关系ID。
     */
    private Long id;

    /**
     * 安全人员ID。
     */
    private Long personId;

    /**
     * 人员类型编码。
     */
    private String typeCode;
}
