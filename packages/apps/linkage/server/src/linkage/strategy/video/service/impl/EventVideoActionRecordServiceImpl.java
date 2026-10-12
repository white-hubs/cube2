package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.dao.EventVideoRecordMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideoRecord;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.EventVideoRecordService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventVideoActionRecordServiceImpl extends ServiceImpl<EventVideoRecordMapper, EventVideoRecord> implements EventVideoRecordService {

    @Override
    public List<EventVideoRecord> listByEventId(@NonNull Long eventId) {
        return this.lambdaQuery().eq(EventVideoRecord::getEventId, eventId).orderByDesc(EventVideoRecord::getCreateTime).list();
    }
}
