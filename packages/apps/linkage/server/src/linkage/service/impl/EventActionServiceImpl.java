package com.guhecloud.cube.biz.linkage.server.linkage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.dao.EventActionMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventAction;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventActionService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventActionServiceImpl extends ServiceImpl<EventActionMapper, EventAction> implements EventActionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(@NonNull Long eventId, @NonNull Long linkageId, @NonNull ExecuteStatusEnum executeStatus) {
        LambdaUpdateWrapper<EventAction> updateWrapper = Wrappers.<EventAction>lambdaUpdate()
                .eq(EventAction::getEventId, eventId)
                .eq(EventAction::getLinkageId, linkageId)
                .set(EventAction::getExecuteStatus, executeStatus)
                .set(EventAction::getUpdateTime, DateUtils.now());
        this.update(updateWrapper);
    }

    @Override
    public void updateStatus(@NonNull Long id, @NonNull ExecuteStatusEnum executeStatus) {
        LambdaUpdateWrapper<EventAction> updateWrapper = Wrappers.<EventAction>lambdaUpdate()
                .eq(EventAction::getId, id)
                .set(EventAction::getExecuteStatus, executeStatus)
                .set(EventAction::getUpdateTime, DateUtils.now());
        this.update(updateWrapper);
    }

    @Override
    public List<EventAction> getListByIds(Collection<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? null : this.listByIds(ids);
    }
}
