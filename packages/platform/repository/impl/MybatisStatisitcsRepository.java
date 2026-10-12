package com.guhecloud.platform.repository.impl;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import com.guhecloud.platform.standard.interfaces.StatisitcsRepository;

import java.io.Serializable;
import java.util.Collection;

public abstract class MybatisStatisitcsRepository<Entity, DBEntity, ID extends Serializable> extends StatisitcsRepository<Entity, DBEntity, ID> {
    private static final int DEFAULT_BATCH_SAVE_SIZE = 1000;

    private final IRepository<DBEntity> mybatisRepository;

    public MybatisStatisitcsRepository(IRepository<DBEntity> mybatisRepository) {
        this.mybatisRepository = mybatisRepository;
    }

    @Override
    public boolean save(Entity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        DBEntity dbEntity = toDTO(entity);
        return mybatisRepository.saveOrUpdate(dbEntity);
    }

    @Override
    public boolean batchSave(Collection<Entity> entities) {
        return mybatisRepository.saveOrUpdateBatch(toDBEntities(entities), DEFAULT_BATCH_SAVE_SIZE);
    }

    @Override
    public Entity get(ID id) {
        return toEntity(mybatisRepository.getById(id));
    }

    @Override
    public Collection<Entity> batchGet(Collection<ID> ids) {
        return toEntities(mybatisRepository.listByIds(ids));
    }

    @Override
    public boolean delete(ID id) {
        return mybatisRepository.removeById(id);
    }

    @Override
    public boolean batchDelete(Collection<ID> ids) {
        return mybatisRepository.removeByIds(ids);
    }

    @Override
    public boolean exists(ID id) {
        throw new UnsupportedOperationException("Method is not supported. please override it if needed.");
    }
}
