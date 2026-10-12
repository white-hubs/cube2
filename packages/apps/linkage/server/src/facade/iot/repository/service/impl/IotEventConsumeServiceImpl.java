package com.guhecloud.cube.biz.linkage.server.facade.iot.repository.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.facade.iot.repository.dao.IotEventConsumeMapper;
import com.guhecloud.cube.biz.linkage.server.facade.iot.repository.entity.IotEventConsume;
import com.guhecloud.cube.biz.linkage.server.facade.iot.repository.service.IotEventConsumeService;
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
public class IotEventConsumeServiceImpl extends ServiceImpl<IotEventConsumeMapper, IotEventConsume> implements IotEventConsumeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull String iotEventInstanceId, @NonNull String additional, Boolean consumed) {
        IotEventConsume eventConsume = IotEventConsume.builder()
                .iotEventInstanceId(iotEventInstanceId)
                .additional(additional)
                .consumed(consumed)
                .build();
        this.baseMapper.insert(eventConsume);
        return eventConsume.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String iotEventInstanceId, Set<String> additions) {
        List<IotEventConsume> iotEventConsumes = additions.stream()
                .map(addition -> IotEventConsume.builder()
                        .iotEventInstanceId(iotEventInstanceId)
                        .additional(addition)
                        .consumed(false)
                        .build()).collect(Collectors.toList());
        this.saveBatch(iotEventConsumes);
    }

    @Override
    public List<IotEventConsume> getStayEventConsume(@NonNull Integer size) {
        return this.baseMapper.selectPage(new Page<>(0, size), Wrappers.<IotEventConsume>lambdaQuery()
                .eq(IotEventConsume::getConsumed, false)
                .orderByDesc(IotEventConsume::getCreateTime)).getRecords();
    }

    @Override
    public List<IotEventConsume> getStayEventConsume(LocalDateTime beginTime) {
        return this.baseMapper.selectList(Wrappers.<IotEventConsume>lambdaQuery()
                                                  .eq(IotEventConsume::getConsumed, false)
                                                  .le(IotEventConsume::getCreateTime, beginTime));
    }
}
