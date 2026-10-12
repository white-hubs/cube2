package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideoRecord;

import java.util.List;

public interface EventVideoRecordService extends IService<EventVideoRecord> {

    List<EventVideoRecord> listByEventId(Long eventId);
}
