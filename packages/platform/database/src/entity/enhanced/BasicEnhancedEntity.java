package com.guhecloud.platform.database.entity.enhanced;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.database.entity.BasicEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 扩展的基础数据类型
 *
 * @param <T>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasicEnhancedEntity<T extends Serializable> extends BasicEntity<T> implements EnhancedCreatable<T>, EnhancedUpdatable<T> {

    /**
     * 创建人id
     */
    private String creatorId;

    /**
     * 创建人类型
     * {@link com.guhecloud.platform.base.model.ActorTypeEnum}
     */
    private ActorTypeEnum creatorType;

    /**
     * 更新人id
     */
    private String updaterId;

    /**
     * 更新人类型
     * {@link com.guhecloud.platform.base.model.ActorTypeEnum}
     */
    private ActorTypeEnum updaterType;
}
