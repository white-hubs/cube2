package com.guhecloud.cube.base.message2.server.handler.exeception;

import com.guhecloud.cube.base.message2.server.handler.pending.MessageSendTask;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义的线程池拒绝策略
 *
 * @author lanchun
 * @date 2023/5/10 16:30
 */
@Slf4j
public class MsgSendTaskRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (r instanceof MessageSendTask) {
            MessageSendTask messageSendTask = (MessageSendTask) r;
            log.error("消息发送任务被线程池拒绝,任务id为{}", messageSendTask.getMessageInfo().getId());
            //重新将消息投递到mq中
            RocketMQContextHolder.send("MSG_SEND_TASK", messageSendTask.getMessageInfo());
        }
    }
}
