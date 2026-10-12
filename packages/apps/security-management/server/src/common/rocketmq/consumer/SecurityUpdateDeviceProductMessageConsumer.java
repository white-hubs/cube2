package com.guhecloud.cube.srv.gen.securitymanagement.server.common.rocketmq.consumer;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.std.devcenter.ProductClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.rocketmq.message.ProductChangeEvent;
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
@RocketMQMessageListener(consumerGroup = Const.DEVICE_CHANGE_TOPIC + "_security_device_product_update",
                         topic = Const.PRODUCT_CHANGE_TOPIC, selectorType = SelectorType.TAG
)
public class SecurityUpdateDeviceProductMessageConsumer implements RocketMQListener<ProductChangeEvent> {

    private final SecurityCommonMapper securityCommonMapper;

    private final ProductClient productClient;

    @Override
    public void onMessage(ProductChangeEvent message) {
        List<DeviceProductInfoDTO> productList = this.productClient.getProductList(Collections.singleton(message.getProductId()));
        if (CollectionUtils.isEmpty(productList)) {
            log.warn("产品信息不存在,securitymanagement不做任何处理,产品id:{}", message.getProductId());
        } else {
            securityCommonMapper.updateProduct(productList.get(0));
        }
    }
}
