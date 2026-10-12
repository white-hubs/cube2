package com.guhecloud.cube.biz.linkage.server.linkage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.linkage.dao.EventActionBeforeMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventActionBefore;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventActionBeforeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EventActionBeforeServiceImpl extends ServiceImpl<EventActionBeforeMapper, EventActionBefore> implements EventActionBeforeService {
    @Override
    public List<EventActionBefore> getListByEventId(Long eventId) {
        return this.list(Wrappers.<EventActionBefore>lambdaQuery()
                                 .eq(EventActionBefore::getEventId, eventId));
    }
}
