package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.dao.EventIotDeviceProRecordMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.entity.EventIotDeviceProRecord;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.enumeration.AttributeControlExecuteResultEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.service.EventIotDeviceProRecordService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventIotDeviceProRecordServiceImpl extends ServiceImpl<EventIotDeviceProRecordMapper, EventIotDeviceProRecord> implements EventIotDeviceProRecordService {

    @Override
    public EventIotDeviceProRecord getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCommandStatus(@NonNull Set<Long> ids, @NonNull AttributeControlExecuteResultEnum executeStatus,
                                    @NonNull String executeStatusDescribe
                                   ) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        LambdaUpdateWrapper<EventIotDeviceProRecord> updateWrapper = Wrappers.<EventIotDeviceProRecord>lambdaUpdate()
                .set(EventIotDeviceProRecord::getExecuteStatus, executeStatus)
                .set(EventIotDeviceProRecord::getExecuteStatusDescribe, executeStatusDescribe)
                .in(EventIotDeviceProRecord::getId, ids);
        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCommandStatus(@NonNull Long id, @NonNull AttributeControlExecuteResultEnum executeStatus,
                                    String executeStatusDescribe
                                   ) {
        EventIotDeviceProRecord eventIotDeviceProRecord = this.getById(id);
        Assert.isNotFound(eventIotDeviceProRecord, "控制记录不存在");
        SessionContextHolder.requestTenant(eventIotDeviceProRecord.getTenant());
        eventIotDeviceProRecord.setExecuteStatus(executeStatus);
        eventIotDeviceProRecord.setExecuteStatusDescribe(executeStatusDescribe);
        this.baseMapper.updateById(eventIotDeviceProRecord);
    }

    @Override
    public List<EventIotDeviceProRecord> listByEventId(@NonNull Long eventId) {
        return this.lambdaQuery().eq(EventIotDeviceProRecord::getEventId, eventId).orderByDesc(EventIotDeviceProRecord::getOperatorTime)
                .list();
    }
}
