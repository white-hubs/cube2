package com.guhecloud.cube.biz.emergency.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EmergencyFileType extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;
}
