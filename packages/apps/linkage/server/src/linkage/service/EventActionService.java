package com.guhecloud.cube.biz.linkage.server.linkage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventAction;

import java.util.Collection;
import java.util.List;

import java.util.Collection;
import java.util.List;

public interface EventActionService extends IService<EventAction> {

    void updateStatus(Long eventId, Long linkageId, ExecuteStatusEnum executeStatus);

    void updateStatus(Long id, ExecuteStatusEnum executeStatus);

    List<EventAction> getListByIds(Collection<Long> ids);
}
