package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.job;

import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 延迟消息发送job
 *
 * @author lanchun
 * @date 2022/6/27 11:14
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageDelayJob {

    private final MessageDelayHandler messageDelayHandler;

    @JobAnnotation(name = "messageDelaySend", cron = "0 0/1 * * * ?", desc = "联动-报警延迟抄送消息发送")
    public void messageDelaySend(String param) {
        this.messageDelayHandler.messageDelayHandler();
    }
}
