package com.guhecloud.cube.biz.linkage.server.record.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.linkage.server.record.domain.aggregates.EventRecord;
import com.guhecloud.cube.biz.linkage.server.record.domain.entity.EventBaseInfo;
import com.guhecloud.cube.biz.linkage.server.record.domain.po.LinkageEventRecordPO;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventApp;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventDevice;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventLevel;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventSnapshot;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventType;
import com.guhecloud.cube.biz.linkage.server.record.infrastructure.repository.mapper.LinkageEventRecordMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventRecordRepository {

    private final LinkageEventRecordMapper linkageEventRecordMapper;

    /**
     * 保存事件记录
     *
     * @param eventRecord 事件记录实体
     *
     * @return EventRecord
     */
    public EventRecord save(EventRecord eventRecord) {
        var isExecuteSave = true;
        var existEventRecordPO = this.linkageEventRecordMapper.selectById(eventRecord.getBaseInfo().getId());
        var eventRecordPO = buildEventRecordPO(eventRecord);
        if (Objects.nonNull(existEventRecordPO)) {
            isExecuteSave = false;
            eventRecordPO.setCreateBy(existEventRecordPO.getCreateBy());
            eventRecordPO.setCreateTime(existEventRecordPO.getCreateTime());
        }
        if (isExecuteSave) {
            this.linkageEventRecordMapper.insert(eventRecordPO);
        } else {
            this.linkageEventRecordMapper.updateById(eventRecordPO);
        }
        return eventRecord;
    }

    private static LinkageEventRecordPO buildEventRecordPO(EventRecord eventRecord) {
        //事件记录信息
        return LinkageEventRecordPO.builder()
                .id(eventRecord.getBaseInfo().getId())
                .code(eventRecord.getBaseInfo().getCode())
                .name(eventRecord.getBaseInfo().getName())
                .eventId(eventRecord.getBaseInfo().getEventId())
                .eventTime(eventRecord.getBaseInfo().getEventTime())
                .levelId(eventRecord.getLevel().getId())
                .levelCode(eventRecord.getLevel().getCode())
                .levelName(eventRecord.getLevel().getName())
                .deviceId(eventRecord.getEventDevice().getId())
                .deviceCode(eventRecord.getEventDevice().getCode())
                .deviceName(eventRecord.getEventDevice().getName())
                .deviceAreaName(eventRecord.getEventDevice().getAreaName())
                .deviceLntAndLat(eventRecord.getEventDevice().getLntAndLat())
                .eventTypeId(eventRecord.getEventType().getId())
                .eventTypeCode(eventRecord.getEventType().getCode())
                .eventTypeName(eventRecord.getEventType().getName())
                .applicationCode(eventRecord.getEventApp().getCode())
                .applicationName(eventRecord.getEventApp().getName())
                .deviceAddress(eventRecord.getEventDevice().getAddress())
                .snapshot(eventRecord.getEventSnapshot().getSnapshot())
                .deleted(false)
                .build();
    }

    /**
     * 事件记录详情
     *
     * @param id 事件记录id
     *
     * @return LinkageEventRecordPO
     */
    public EventRecord detail(Long id) {
        var eventRecordPO = this.linkageEventRecordMapper.selectById(id);
        if (Objects.isNull(eventRecordPO)) {
            return null;
        }
        return EventRecord.builder()
                .baseInfo(new EventBaseInfo(eventRecordPO.getId(), eventRecordPO.getCode(), eventRecordPO.getName(), eventRecordPO.getEventId(), eventRecordPO.getEventTime()))
                .level(new EventLevel(eventRecordPO.getLevelId(), eventRecordPO.getLevelCode(), eventRecordPO.getLevelName()))
                .eventDevice(new EventDevice(eventRecordPO.getDeviceId(), eventRecordPO.getDeviceCode(), eventRecordPO.getDeviceName(), eventRecordPO.getDeviceAreaName(), eventRecordPO.getDeviceLntAndLat(), eventRecordPO.getDeviceAddress()))
                .eventSnapshot(new EventSnapshot(eventRecordPO.getSnapshot()))
                .eventType(new EventType(eventRecordPO.getEventTypeId(), eventRecordPO.getEventTypeCode(), eventRecordPO.getEventTypeName()))
                .eventApp(new EventApp(eventRecordPO.getApplicationCode(), eventRecordPO.getApplicationName()))
                .build();
    }

    public EventRecord detailByEventId(Long eventId) {
        var recordPO = this.linkageEventRecordMapper.selectOne(Wrappers.<LinkageEventRecordPO>lambdaQuery()
                                                                       .eq(LinkageEventRecordPO::getEventId, eventId));
        if (Objects.isNull(recordPO)) {
            return null;
        }
        return detail(recordPO.getId());
    }

    /**
     * 事件记录删除
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void remove(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        var eventRecordPOs = this.linkageEventRecordMapper.selectList(Wrappers.<LinkageEventRecordPO>lambdaQuery()
                                                                              .in(LinkageEventRecordPO::getId, ids));
        DatabaseContextHolder.softDelete(LinkageEventRecordPO.class, eventRecordPOs);
    }
}
