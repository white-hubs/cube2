package com.guhecloud.platform.standard.interfaces;

import com.guhecloud.platform.repository.interfaces.Paging;

import java.util.Collection;

public abstract class BussinessRepository<Entity, DBEntity, ID> implements Repository<Entity, ID> {

    public abstract <C> Collection<Entity> list(C condition, Paging paging);

    public abstract <C> long count(C condition);

    public abstract Entity toEntity(DBEntity dbEntity);

    public abstract DBEntity toDBEntity(Entity entity);

    public Collection<Entity> toEntities(Collection<DBEntity> dbEntities) {
        return dbEntities.stream().map(this::toEntity).toList();
    }

    public Collection<DBEntity> toDBEntities(Collection<Entity> entities) {
        return entities.stream().map(this::toDBEntity).toList();
    }
}
