package com.guhecloud.cube.srv.gen.maintain.server.upkeep.rocketmq.consumer;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.std.devcenter.ProductClient;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceRuleCondition;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenanceRuleConditionService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.rocketmq.consumer.message.ProductChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "up_keep_product_change",
                         topic = Const.PRODUCT_CHANGE_TOPIC, selectorType = SelectorType.TAG
)
public class UpKeepUpdateProductMessageConsumer implements RocketMQListener<ProductChangeEvent> {

    private final ProductClient productClient;

    private final MaintenanceRuleConditionService maintenanceRuleConditionService;

    @Override
    public void onMessage(ProductChangeEvent message) {
        List<DeviceProductInfoDTO> productList = productClient.getProductList(Collections.singleton(message.getProductId()));
        if (CollectionUtils.isEmpty(productList)) {
            log.warn("product is not found:{}", message.getProductId());
            return;
        }
        DeviceProductInfoDTO productInfo = productList.get(0);
        List<MaintenanceRuleCondition> maintenanceRuleConditions = this.maintenanceRuleConditionService.listByProductId(productInfo.getId());
        if (CollectionUtils.isEmpty(maintenanceRuleConditions)) {
            log.warn("product up_keep_rue_condition is empty:{}", message.getProductId());
            return;
        }
        for (MaintenanceRuleCondition ruleCondition : maintenanceRuleConditions) {
            ruleCondition.setDeviceProdCategoryCode(productInfo.getProductCategory().getCode());
            ruleCondition.setDeviceProdCategoryCodeConcat(productInfo.getProductCategory().getPathCode());
            ruleCondition.setDeviceProdCategoryName(productInfo.getProductCategory().getName());
            ruleCondition.setDeviceProdName(productInfo.getName());
            maintenanceRuleConditionService.updateById(ruleCondition);
        }
    }
}
