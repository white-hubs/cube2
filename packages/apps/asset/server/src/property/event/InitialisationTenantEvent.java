package com.guhecloud.cube.srv.gen.property.management.server.property.event;

import com.guhecloud.cube.base.dictionary.client.CascadeDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.CascadeDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.CascadeDictionaryAddParam;
import com.guhecloud.cube.biz.ouaa.client.message.TenantInitMessage;
import com.guhecloud.cube.srv.gen.property.management.server.common.enumeration.PropertyTypeEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-12  20:11
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = TenantInitMessage.TOPIC + "_PROPERTY", topic = TenantInitMessage.TOPIC)
public class InitialisationTenantEvent implements RocketMQListener<TenantInitMessage> {
    private final CascadeDictionaryClient cascadeDictionaryClient;
    private final static String itemCode = "risk-source-type";

    @Override
    public void onMessage(TenantInitMessage message) {
        SessionContextHolder.requestTenant(message.getTenant());
        // 消防设备
        CascadeDictionaryDTO firefightingDTO = cascadeDictionaryClient.addCascadeDictionary(this.getCascadeDictionaryAddParam(PropertyTypeEnum.FIRE_FIGHTING_EQUIPMENT));
        cascadeDictionaryClient.addCascadeDictionary(this.getCascadeDictionaryAddParam(2, PropertyTypeEnum.FIRE_FIGHTING_EQUIPMENT, firefightingDTO.getId()));
        cascadeDictionaryClient.addCascadeDictionary(this.getCascadeDictionaryAddParam(2, PropertyTypeEnum.FIRE_HYDRANT, firefightingDTO.getId()));
        // 易燃设备FLAMMABLE_EQUIPMENT
        CascadeDictionaryDTO flammableDTO = cascadeDictionaryClient.addCascadeDictionary(this.getCascadeDictionaryAddParam(PropertyTypeEnum.FLAMMABLE_EQUIPMENT));
        cascadeDictionaryClient.addCascadeDictionary(this.getCascadeDictionaryAddParam(2, PropertyTypeEnum.OXYGEN_CYLINDER, flammableDTO.getId()));
        // 现场运输设备
        CascadeDictionaryDTO transportDTO = cascadeDictionaryClient.addCascadeDictionary(this.getCascadeDictionaryAddParam(PropertyTypeEnum.TRANSPORT_DEVICES));
        cascadeDictionaryClient.addCascadeDictionary(this.getCascadeDictionaryAddParam(2, PropertyTypeEnum.ELECTRO_MOBILE, transportDTO.getId()));
    }

    private CascadeDictionaryAddParam getCascadeDictionaryAddParam(PropertyTypeEnum propertyTypeEnum) {
        return this.getCascadeDictionaryAddParam(1, propertyTypeEnum, null);
    }

    private CascadeDictionaryAddParam getCascadeDictionaryAddParam(Integer level, PropertyTypeEnum propertyTypeEnum, Long superiorsId) {
        CascadeDictionaryAddParam addParam = new CascadeDictionaryAddParam();
        addParam.setItemCode(itemCode);
        addParam.setCode(propertyTypeEnum.name());
        addParam.setName(propertyTypeEnum.getRemarks());
        addParam.setLevel(level);
        if (ObjectUtils.isNotEmpty(superiorsId)) {
            addParam.setSuperiorsId(superiorsId);
        }
        return addParam;
    }
}
