package com.guhecloud.platform.standard.interfaces;

import java.util.Collection;

public abstract class StatisitcsRepository<Entity, DBEntity, ID> implements Repository<Entity, ID> {
    public abstract Entity toEntity(DBEntity dbEntity);

    public abstract DBEntity toDTO(Entity entity);

    public Collection<Entity> toEntities(Collection<DBEntity> dbEntities) {
        return dbEntities.stream().map(this::toEntity).toList();
    }

    public Collection<DBEntity> toDBEntities(Collection<Entity> entities) {
        return entities.stream().map(this::toDTO).toList();
    }
}
