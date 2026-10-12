package com.guhecloud.platform.database.entity.enhanced;

import com.guhecloud.platform.database.entity.Entity;
import com.guhecloud.platform.database.entity.Updatable;
import com.guhecloud.platform.base.model.ActorTypeEnum;

import java.io.Serializable;

public interface EnhancedUpdatable<T extends Serializable> extends Entity<T>, Updatable<T> {

    /**
     * 获取更新用户id
     *
     * @return 更新用户id
     */
    String getUpdaterId();

    /**
     * 设置更新用户id
     *
     * @param updaterId 更新用户id
     */
    void setUpdaterId(String updaterId);

    /**
     * 获取更新人类型
     *
     * @return 更新人类型
     */
    ActorTypeEnum getUpdaterType();

    /**
     * 设置更新人类型
     *
     * @param updaterType 更新人类型
     */
    void setUpdaterType(ActorTypeEnum updaterType);
}
