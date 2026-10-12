package com.guhecloud.cube.srv.gen.safety.education.server.transfer.service;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.Message;

public interface DataTransferService {

    /**
     * 培训任务数据转换
     *
     * @param groupTrainTaskMessage
     */
    ConsumeOrderlyStatus trainingTaskDataTransfer(String groupTrainTaskMessage);

    /**
     * 考试任务数据转换
     *
     * @param groupTrainTaskMessage
     */
    ConsumeOrderlyStatus examTaskDataTransfer(String groupTrainTaskMessage);

    /**
     * 市场消息转发到集团
     *
     * @param id
     * @param sourceCode
     * @param message
     */
    void marketMessagePushGroup(String id, String sourceCode, Message message);
}
