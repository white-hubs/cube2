package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command.BroadcastPlayTextCirculateCommand;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command.BroadcastPlayTextCommand;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command.BroadcastPlayTextStopCommand;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.aggregates.BroadcastPlayPlan;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.aggregates.BroadcastPlayRecord;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.event.BroadcastPlayTextEvent;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.factory.BroadcastPlayPlanFactory;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.factory.BroadcastPlayRecordFactory;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.DeviceInfo;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.BroadcastPlayPlanRepository;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.BroadcastPlayRecordRepository;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastPlayTextCommandService {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final BroadcastPlayRecordRepository broadcastPlayRecordRepository;

    private final BroadcastPlayPlanRepository broadcastPlayPlanRepository;

    /**
     * 广播文本播放
     *
     * @param broadcastPlayTextCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public String playText(BroadcastPlayTextCommand broadcastPlayTextCommand) {
        //构建广播文本播放记录
        BroadcastPlayRecord broadcastPlayRecord = BroadcastPlayRecordFactory.newBroadcastPlayRecord(broadcastPlayTextCommand);
        BroadcastPlayTextEvent broadcastPlayTextEvent = new BroadcastPlayTextEvent(this,
                                                                                   broadcastPlayRecord.getBroadcastPlayText()
                                                                                           .getPlayContent(),
                                                                                   broadcastPlayRecord.getBroadcastPlayText()
                                                                                           .getDeviceInfos().stream()
                                                                                           .map(DeviceInfo::getThirdPartyCode)
                                                                                           .filter(StringUtils::isNotBlank)
                                                                                           .collect(Collectors.toSet()),
                                                                                   broadcastPlayRecord.getBroadcastPlayText()
                                                                                           .getBroadcastPlayNum()
        );
        //发送广播播放事件
        this.applicationEventPublisher.publishEvent(broadcastPlayTextEvent);
        //修改播放结果
        broadcastPlayRecord.changeBroadcastPlayStatus(broadcastPlayTextEvent.getBroadcastPlayTextResponse());
        //持久化到数据库
        this.broadcastPlayRecordRepository.save(broadcastPlayRecord);
        return broadcastPlayTextEvent.getBroadcastPlayTextResponse().getPlayTextTaskId();
    }

    /**
     * 广播文本循环播放
     *
     * @param broadcastPlayTextCirculateCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void playTextCirculate(BroadcastPlayTextCirculateCommand broadcastPlayTextCirculateCommand) {
        //构造广播播放计划
        BroadcastPlayPlan broadcastPlayPlan = BroadcastPlayPlanFactory.newBroadcastPlayPlan(broadcastPlayTextCirculateCommand);
        //持久化到数据库
        this.broadcastPlayPlanRepository.save(broadcastPlayPlan);
    }

    /**
     * 广播文本设备停止播放
     *
     * @param broadcastPlayTextStopCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void playTextDeviceStop(BroadcastPlayTextStopCommand broadcastPlayTextStopCommand) {
        List<BroadcastPlayPlan> broadcastPlayPlanList = this.broadcastPlayPlanRepository.selectPlayByDeviceId(broadcastPlayTextStopCommand.getDeviceId());
        broadcastPlayPlanList.forEach(broadcastPlayPlan -> {
            broadcastPlayPlan.getBroadcastPlayText().removeDeviceInfo(broadcastPlayTextStopCommand.getDeviceId());
            //持久化到数据库
            this.broadcastPlayPlanRepository.save(broadcastPlayPlan);
        });
    }
}
