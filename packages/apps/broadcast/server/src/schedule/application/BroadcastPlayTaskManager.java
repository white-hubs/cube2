package com.guhecloud.cube.biz.subsystem.broadcast.server.schedule.application;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.BroadcastPlayPlanService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.schedule.infrastruceture.rocketmq.message.BroadcastPlayMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BroadcastPlayTaskManager {

    @Autowired
    private BroadcastPlayPlanService broadcastPlayPlanService;

    @JobAnnotation(name = "broadcastPlayCirculate", cron = "0/30 * * * * ?", desc = "广播循环播放任务")
    public void expireMsgDel(String param) {
        // 从数据库加载定时任务信息
        var taskInfoList = broadcastPlayPlanService.findExecutePlayPlan();
        taskInfoList.parallelStream().forEach(taskInfo -> {
            //发送广播播放消息
            SessionContextHolder.requestTenant(taskInfo.getTenant());
            BroadcastPlayMessage broadcastPlayMessage = new BroadcastPlayMessage();
            broadcastPlayMessage.setPlanId(taskInfo.getId());
            broadcastPlayMessage.setPlayParam(taskInfo.getPlayParam());
            broadcastPlayMessage.setTenant(taskInfo.getTenant());
            RocketMQContextHolder.send(BroadcastPlayMessage.TOPIC, broadcastPlayMessage);
        });
    }
}
