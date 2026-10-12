package com.guhecloud.cube.biz.mdata.server.bizlog.event;

import com.guhecloud.cube.biz.mdata.server.bizlog.entity.OperationLog;
import com.guhecloud.cube.biz.mdata.server.bizlog.mapper.OperationLogMapper;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.cloud.mq.SysOperationLog;
import com.guhecloud.platform.cloud.mq.SysOperationLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-11-27  16:07
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = SysOperationLogMessage.TOPIC, topic = SysOperationLogMessage.TOPIC)
public class OperationLogEvent implements RocketMQListener<SysOperationLog> {
    private final OperationLogMapper operationLogMapper;

    @Override
    public void onMessage(SysOperationLog message) {
        if (log.isDebugEnabled()) {
            log.debug("收到操作日志消息:{}", message.toString());
        }
        operationLogMapper.insert(ConvertUtils.convert(OperationLog.class, message));
    }
}
