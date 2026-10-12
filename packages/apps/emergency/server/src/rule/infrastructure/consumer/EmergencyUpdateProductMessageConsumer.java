package com.guhecloud.cube.biz.emergency.server.rule.infrastructure.consumer;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.std.devcenter.ProductClient;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionDeviceService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionService;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionDevicePO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionPO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.consumer.message.ProductChangeEvent;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "emergency_product_change",
                         topic = Const.PRODUCT_CHANGE_TOPIC, selectorType = SelectorType.TAG
)
public class EmergencyUpdateProductMessageConsumer implements RocketMQListener<ProductChangeEvent> {

    private final EmergencyRuleConditionService emergencyRuleConditionService;

    private final EmergencyRuleConditionDeviceService emergencyRuleConditionDeviceService;

    private final ProductClient productClient;

    @Override
    public void onMessage(ProductChangeEvent message) {
        List<DeviceProductInfoDTO> productList = productClient.getProductList(Collections.singleton(message.getProductId()));
        if (CollectionUtils.isEmpty(productList)) {
            log.warn("product is not found:{}", message.getProductId());
            return;
        }
        DeviceProductInfoDTO productInfo = productList.getFirst();
        List<EmergencyRuleConditionPO> emergencyRuleConditionPOS = this.emergencyRuleConditionService.listByProductId(productInfo.getId());
        if (CollectionUtils.isEmpty(emergencyRuleConditionPOS)) {
            log.warn("product emergency_rue_condition is empty:{}", message.getProductId());
            return;
        }
        for (EmergencyRuleConditionPO ruleCondition : emergencyRuleConditionPOS) {
            ruleCondition.setDeviceProdCategoryCode(productInfo.getProductCategory().getCode());
            ruleCondition.setDeviceProdCategoryCodeConcat(productInfo.getProductCategory().getPathCode());
            ruleCondition.setDeviceProdCategoryName(productInfo.getProductCategory().getName());
            ruleCondition.setDeviceProdName(productInfo.getName());
            emergencyRuleConditionService.updateById(ruleCondition);
        }
        List<EmergencyRuleConditionDevicePO> emergencyRuleConditionDevicePOS = this.emergencyRuleConditionDeviceService.listByProductId(productInfo.getId());
        if (CollectionUtils.isEmpty(emergencyRuleConditionPOS)) {
            log.warn("product emergency_rue_condition_device is empty:{}", message.getProductId());
            return;
        }
        for (EmergencyRuleConditionDevicePO device : emergencyRuleConditionDevicePOS) {
            device.setDeviceProdCategoryCode(productInfo.getProductCategory().getCode());
            device.setDeviceProdCategoryCodeConcat(productInfo.getProductCategory().getPathCode());
            device.setDeviceProdCategoryName(productInfo.getProductCategory().getName());
            device.setDeviceProdName(productInfo.getName());
            emergencyRuleConditionDeviceService.updateById(device);
        }
    }
}