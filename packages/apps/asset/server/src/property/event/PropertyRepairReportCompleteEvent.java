package com.guhecloud.cube.srv.gen.property.management.server.property.event;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.param.PropertyRepairReportParam;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyDetailedService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyRepairReportService;
import com.guhecloud.platform.base.util.ObjectUtils;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-20  11:27
 * @Description: 故障报修完成消费
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "ATT_FAULT_ORDER_COMPLETED", topic = ProcessCompletedMessage.TOPIC, selectorExpression = "FAULT")
public class PropertyRepairReportCompleteEvent implements RocketMQListener<ProcessCompletedMessage> {
    private static final String TYPE = "PROPERTY";
    private static final String NAME = "FAULT";
    private final PropertyDetailedService propertyDetailedService;
    private final PropertyRepairReportService propertyRepairReportService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        if (NAME.equals(message.getBusinessId().getBusinessType())) {
            PropertyRepairReportParam faultOrderDTO = JSONObject.parseObject(message.getData(), PropertyRepairReportParam.class);
            if (TYPE.equals(faultOrderDTO.getObjType())) {
                String status = propertyRepairReportService.selectRepairReport(faultOrderDTO.getFaultObj()[0], message.getProcessInstanceId());
                if (ObjectUtils.isNotEmpty(status)) {
                    Boolean bool = propertyDetailedService.update(PropertyStatusEnum.getPropertyStatusEnum(status), faultOrderDTO.getFaultObj()[0]);
                    log.info("资产报修流程审批结束。修改状态:{}:结果：{}", status, bool);
                }
                log.info("资产报修流程审批结束:{},data:{},result:{}", message.getProcessInstanceId(), message.getData(), message.getResult()
                        .name());
            }
        }
    }
}
