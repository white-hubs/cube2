package com.guhecloud.cube.biz.emergency.server.rule.infrastructure.consumer;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceChange;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceInfo;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceChangeEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionDeviceService;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionDevicePO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
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
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "emergency_device_change",
                         topic = Const.DEVICE_CHANGE_TOPIC, selectorType = SelectorType.TAG
)
public class EmergencyUpdateDeviceInstanceMessageConsumer implements RocketMQListener<DeviceChange> {

    private final EmergencyRuleConditionDeviceService emergencyRuleConditionDeviceService;
    private final AreaClient areaClient;
    private final DeviceCenterStdClient deviceCenterClient;

    @Override
    public void onMessage(DeviceChange message) {
        log.debug("预案规则设备数据同步{}", message.toString());
        List<Pair<DeviceInfo, DeviceChangeEnum>> deviceChangePairs = message.getDeviceChangePairs();
        List<Pair<DeviceInfo, DeviceChangeEnum>> pairs = deviceChangePairs.stream()
                .filter(deviceChangePair -> deviceChangePair.getVal() == DeviceChangeEnum.UPDATE).toList();
        pairs.forEach(pair -> {
            DeviceInfo key = pair.getKey();
            DeviceDetailDTO deviceDetail = deviceCenterClient.getDeviceById(key.getDeviceId());
            List<EmergencyRuleConditionDevicePO> emergencyRuleConditionDevicePOS = emergencyRuleConditionDeviceService
                    .listByDeviceIds(Collections.singleton(key.getDeviceId()));
            if (CollectionUtils.isNotEmpty(emergencyRuleConditionDevicePOS)) {
                EmergencyRuleConditionDevicePO emergencyRuleConditionDevicePO = emergencyRuleConditionDevicePOS.getFirst();
                SessionContextHolder.requestTenant(emergencyRuleConditionDevicePO.getTenant());
                //修改条件设备的基础信息
                this.changeEmergencyRuleConditionDeviceInfo(emergencyRuleConditionDevicePOS, deviceDetail);
                this.emergencyRuleConditionDeviceService.updateBatchById(emergencyRuleConditionDevicePOS);
                log.debug("DeviceDetailDTO的实体===>{}", deviceDetail);
            }
        });
    }

    public void changeEmergencyRuleConditionDeviceInfo(List<EmergencyRuleConditionDevicePO> emergencyRuleConditionDevices,
                                                       DeviceDetailDTO deviceDetail
                                                      ) {
        //基础信息
        DeviceBaseArchivesInfoDTO baseArchivesInfo = deviceDetail.getBaseArchivesInfo();
        //设备产品信息
        DeviceProductInfoDTO productInfo = deviceDetail.getProductInfo();
        Pair<Long, String> area = baseArchivesInfo.getArea();
        Long areaId = area.getKey();
        AreaDTO areaById = areaClient.getAreaById(areaId);
        log.debug("areaById的实体===>{}", areaById);
        DeviceProductInfoDTO.ProductCategory productCategory = productInfo.getProductCategory();
        emergencyRuleConditionDevices.forEach(device -> {
            if (Objects.nonNull(areaById)) {
                String newStr = areaById.getSequence().replaceAll("-$", "");
                device.setDeviceAreaId(newStr);
                device.setDeviceAreaName(area.getVal());
            }
            device.setDeviceName(baseArchivesInfo.getName());
            device.setDeviceProdId(productInfo.getId());
            device.setDeviceProdName(productInfo.getName());
            device.setDeviceProdCategoryCode(productCategory.getCode());
            device.setDeviceProdCategoryCodeConcat(productCategory.getPathCode());
            device.setDeviceProdCategoryName(productCategory.getName());
            log.debug("emergencyConditionDevice的实体===>{}", device);
        });
    }
}
