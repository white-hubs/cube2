package com.guhecloud.platform.database.entity.enhanced;

import com.guhecloud.platform.database.entity.Isolatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BasicEnhancedTenantEntity<T extends Serializable> extends BasicEnhancedEntity<T> implements Isolatable<T> {

    /**
     * 租户编号
     */
    private Integer tenant;
}
