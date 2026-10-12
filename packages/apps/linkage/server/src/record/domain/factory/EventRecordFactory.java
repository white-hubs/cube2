package com.guhecloud.cube.biz.linkage.server.record.domain.factory;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.linkage.server.record.application.commandgateway.command.IncreaseEventRecordCommand;
import com.guhecloud.cube.biz.linkage.server.record.domain.aggregates.EventRecord;
import com.guhecloud.cube.biz.linkage.server.record.domain.entity.EventBaseInfo;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventApp;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventDevice;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventLevel;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventSnapshot;
import com.guhecloud.cube.biz.linkage.server.record.domain.valueobject.EventType;
import com.guhecloud.platform.redis.util.RedisUtils;

/**
 * 事件记录创建工厂
 */
public class EventRecordFactory {

    public static EventRecord newAlarmRecord(final IncreaseEventRecordCommand increaseEventRecordCommand) {
        //基本信息
        var baseInfo = new EventBaseInfo(IdWorker.getId(),
                                         String.valueOf(RedisUtils.generateIdentity()),
                                         increaseEventRecordCommand.getName(),
                                         increaseEventRecordCommand.getEventId(),
                                         increaseEventRecordCommand.getEventTime());
        //报警级别
        var levelInfo = new EventLevel(increaseEventRecordCommand.getLevelId(), increaseEventRecordCommand
                .getLevelCode(), increaseEventRecordCommand.getLevelName());
        //事件设备信息
        var eventDevice = new EventDevice(increaseEventRecordCommand.getDeviceId(),
                                          increaseEventRecordCommand.getDeviceCode(),
                                          increaseEventRecordCommand.getDeviceName(),
                                          increaseEventRecordCommand.getDeviceAreaName(),
                                          increaseEventRecordCommand.getDeviceLntAndLat(),
                                          increaseEventRecordCommand.getDeviceAddress());
        var snapshot = new EventSnapshot(increaseEventRecordCommand.getSnapshot());
        //类型信息
        var eventType = new EventType(increaseEventRecordCommand.getEventTypeId(), increaseEventRecordCommand.getEventTypeCode(), increaseEventRecordCommand.getEventTypeName());
        //事件应用
        var eventApp = new EventApp(increaseEventRecordCommand.getApplicationCode(), increaseEventRecordCommand.getApplicationName());
        return new EventRecord(baseInfo, levelInfo, eventDevice, eventType, eventApp, snapshot);
    }
}
