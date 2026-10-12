package com.guhecloud.platform.repository.impl;

import com.baomidou.mybatisplus.extension.repository.IRepository;

public class MybatisRepositoryExampleImpl extends MybatisRepository<MybatisRepositoryExampleImpl.FakeEntity, MybatisRepositoryExampleImpl.FakeDbEntity, String> {
    public MybatisRepositoryExampleImpl(IRepository<FakeDbEntity> mybatisRepository) {
        super(mybatisRepository);
    }

    @Override
    public FakeEntity toEntity(FakeDbEntity dbEntity) {
        return null;
    }

    @Override
    public FakeDbEntity toDBEntity(FakeEntity entity) {
        return null;
    }

    public static class FakeEntity {}

    public static class FakeDbEntity {}
}
