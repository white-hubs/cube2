package com.guhecloud.platform.standard.interfaces;

import java.util.Collection;

/**
 * 通用仓库接口
 *
 * @param <Entity> 实体类型
 * @param <ID>     实体标识类型
 */
public interface Repository<Entity, ID> {
    boolean save(Entity entity);

    boolean batchSave(Collection<Entity> entities);

    Entity get(ID id);

    Collection<Entity> batchGet(Collection<ID> ids);

    boolean delete(ID id);

    boolean batchDelete(Collection<ID> ids);

    boolean exists(ID id);
}
