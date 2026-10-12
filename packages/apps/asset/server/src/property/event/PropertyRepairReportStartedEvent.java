package com.guhecloud.cube.srv.gen.property.management.server.property.event;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.param.PropertyRepairReportParam;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyDetailedService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyRepairReportService;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-20  11:28
 * @Description: 流程发起消费
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "ATT_FAULT_ORDER_APPLY_STARTED", topic = ProcessStartedMessage.TOPIC, selectorExpression = "FAULT")
public class PropertyRepairReportStartedEvent implements RocketMQListener<ProcessStartedMessage> {
    private final PropertyDetailedService propertyDetailedService;
    private final PropertyRepairReportService propertyRepairReportService;
    private static final String TYPE = "PROPERTY";
    private static final String NAME = "FAULT";

    @Override
    public void onMessage(ProcessStartedMessage message) {
        if (NAME.equals(message.getBusinessType())) {
            PropertyRepairReportParam faultOrderDTO = JSONObject.parseObject(message.getData(), PropertyRepairReportParam.class);
            if (TYPE.equals(faultOrderDTO.getObjType())) {
                log.info("资产报修流程发起成功:{},data:{}", message.getProcessInstanceId(), message.getData());
                propertyRepairReportService.insert(message, faultOrderDTO.getFaultObj()[0]);
                Boolean bool = propertyDetailedService.update(PropertyStatusEnum.REPAIR_REPORT, faultOrderDTO.getFaultObj()[0]);
                log.info("资产报修单修改资产状态结果：{}", bool);
            }
        }
    }
}