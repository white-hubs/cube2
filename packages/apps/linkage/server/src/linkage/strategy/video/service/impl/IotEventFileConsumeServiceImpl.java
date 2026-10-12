package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.dao.IotEventFileConsumeMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.IotEventFileConsume;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.IotEventFileConsumeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IotEventFileConsumeServiceImpl extends ServiceImpl<IotEventFileConsumeMapper, IotEventFileConsume> implements IotEventFileConsumeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull String iotEventInstanceId, @NonNull String additional) {
        IotEventFileConsume iotEventFileConsume = IotEventFileConsume.builder()
                .iotEventInstanceId(iotEventInstanceId)
                .additional(additional)
                .consumed(false)
                .build();
        this.baseMapper.insert(iotEventFileConsume);
        return iotEventFileConsume.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String iotEventInstanceId, Set<String> additions) {
        List<IotEventFileConsume> iotEventFileConsumes = additions.stream()
                .map(addition -> IotEventFileConsume.builder()
                        .iotEventInstanceId(iotEventInstanceId)
                        .additional(addition)
                        .consumed(false)
                        .build()).collect(Collectors.toList());
        this.saveBatch(iotEventFileConsumes);
    }

    @Override
    public List<IotEventFileConsume> getStayEventFileConsume(@NonNull Integer size) {
        return this.baseMapper.selectPage(new Page<>(0, size), Wrappers.<IotEventFileConsume>lambdaQuery()
                .eq(IotEventFileConsume::getConsumed, false)
                .orderByDesc(IotEventFileConsume::getCreateTime)).getRecords();
    }

    @Override
    public List<IotEventFileConsume> getStayEventFileConsume(LocalDateTime beginTime) {
        return this.baseMapper.selectList(Wrappers.<IotEventFileConsume>lambdaQuery()
                                                  .eq(IotEventFileConsume::getConsumed, false)
                                                  .le(IotEventFileConsume::getCreateTime, beginTime));
    }

    @Override
    public List<IotEventFileConsume> getStayEventFileConsume(LocalDateTime beginTime, int batchSize) {
        return this.baseMapper.selectPage(
                new Page<>(1, batchSize),
                Wrappers.<IotEventFileConsume>lambdaQuery()
                        .eq(IotEventFileConsume::getConsumed, false)
                        .le(IotEventFileConsume::getCreateTime, beginTime)
        ).getRecords();
    }
}
