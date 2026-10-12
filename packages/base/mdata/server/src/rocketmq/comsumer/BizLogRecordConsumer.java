package com.guhecloud.cube.biz.mdata.server.rocketmq.comsumer;

import com.guhecloud.cube.biz.mdata.server.bizlog.service.BizLogRecordService;
import com.guhecloud.platform.cloud.logger.message.BizLogRecordMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听业务操作日志
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = BizLogRecordMessage.TOPIC + "_" + "MDATA",
                         topic = BizLogRecordMessage.TOPIC
)
public class BizLogRecordConsumer implements RocketMQListener<BizLogRecordMessage> {
    private final BizLogRecordService bizLogRecordService;

    @Override
    public void onMessage(BizLogRecordMessage message) {
        log.info("消费业务日志：{}", message);
        this.bizLogRecordService.addBizLog(message.getBizLogRecord());
    }
}
