package com.guhecloud.platform.database.entity.enhanced;

import com.guhecloud.platform.database.entity.Creatable;
import com.guhecloud.platform.database.entity.Entity;
import com.guhecloud.platform.base.model.ActorTypeEnum;

import java.io.Serializable;

public interface EnhancedCreatable<T extends Serializable> extends Entity<T>, Creatable<T> {

    /**
     * 获取创建用户id
     *
     * @return 创建用户id
     */
    String getCreatorId();

    /**
     * 设置创建用户id
     *
     * @param creatorId 创建用户id
     */
    void setCreatorId(String creatorId);

    /**
     * 获取创建人类型
     *
     * @return 创建人类型
     */
    ActorTypeEnum getCreatorType();

    /**
     * 设置创建人类型
     *
     * @param creatorType 创建人类型
     */
    void setCreatorType(ActorTypeEnum creatorType);
}
