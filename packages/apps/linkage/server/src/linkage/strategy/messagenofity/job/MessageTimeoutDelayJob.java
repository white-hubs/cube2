package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.job;

import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 超时消息发送job
 *
 * @author lanchun
 * @date 2022/11/2 14:19
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageTimeoutDelayJob {

    private final MessageTimeoutHandler messageDelayHandler;

    @JobAnnotation(name = "messageTimeoutSend", cron = "0 0/1 * * * ?", desc = "联动-报警超时抄送消息发送")
    public void messageTimeoutSend(String param) {
        this.messageDelayHandler.messageTimeoutHandler();
    }
}
