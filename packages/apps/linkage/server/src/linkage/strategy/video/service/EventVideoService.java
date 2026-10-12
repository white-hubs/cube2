package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.dto.LinkageVideoDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideo;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideoRecord;

import java.util.List;

public interface EventVideoService extends IService<EventVideo> {

    List<EventVideoRecord> saveAndReturnRecords(Event event, Long linkageId, Long eventLinkageId, List<LinkageVideoDTO> linkageVideoDTOS);

    List<EventVideo> listByEventId(Long eventId);
}
