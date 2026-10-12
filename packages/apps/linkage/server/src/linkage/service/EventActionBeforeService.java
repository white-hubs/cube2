package com.guhecloud.cube.biz.linkage.server.linkage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventActionBefore;

import java.util.List;

public interface EventActionBeforeService extends IService<EventActionBefore> {

    List<EventActionBefore> getListByEventId(Long eventId);
}
