package com.guhecloud.cube.srv.gen.securitymanagement.server.common.rocketmq.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceExpandInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceChange;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceInfo;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceChangeEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = Const.DEVICE_CHANGE_TOPIC + "_security_device_update",
                         topic = Const.DEVICE_CHANGE_TOPIC, selectorType = SelectorType.TAG
)
public class SecurityUpdateDeviceInstanceMessageConsumer implements RocketMQListener<DeviceChange> {

    private final SecurityCommonMapper securityCommonMapper;
    private final AreaClient areaClient;
    private final DeviceCenterStdClient deviceCenterClient;

    @Override
    public void onMessage(DeviceChange message) {
        //数据同步 品牌code去除（和iot商量）
        log.debug("数据同步" + message.toString());
        List<Pair<DeviceInfo, DeviceChangeEnum>> deviceChangePairs = message.getDeviceChangePairs();
        List<Pair<DeviceInfo, DeviceChangeEnum>> pairs = deviceChangePairs.stream()
                .filter(deviceChangePair -> deviceChangePair.getVal() == DeviceChangeEnum.UPDATE).collect(Collectors.toList());
        List<SecurityDevice> securityDevices = new ArrayList<>();
        pairs.forEach(pair -> {
            DeviceInfo key = pair.getKey();
            List<SecurityDevice> securityDeviceList = securityCommonMapper.selectList(Wrappers.<SecurityDevice>lambdaQuery()
                                                                                              .eq(SecurityDevice::getDeviceId, key.getDeviceId()));
            if (CollectionUtils.isNotEmpty(securityDeviceList)) {
                SecurityDevice srouceSecurityDevice = securityDeviceList.get(0);
                SessionContextHolder.requestTenant(srouceSecurityDevice.getTenant());
            }
            DeviceDetailDTO deviceById = deviceCenterClient.getDeviceById(key.getDeviceId());
            log.debug("DeviceDetailDTO的实体===>{}", deviceById);
            if (Objects.nonNull(deviceById)) {
                SecurityDevice securityDevice = new SecurityDevice();
                securityDevice.setDeviceId(key.getDeviceId());
                //基础信息
                DeviceBaseArchivesInfoDTO baseArchivesInfo = deviceById.getBaseArchivesInfo();
                //扩展信息
                DeviceExpandInfoDTO expandInfo = deviceById.getExpandInfo();
                //设备产品信息
                DeviceProductInfoDTO productInfo = deviceById.getProductInfo();
                Pair<Long, String> area = baseArchivesInfo.getArea();
                Long areaId = area.getKey();
                AreaDTO areaById = areaClient.getAreaById(areaId);
                log.debug("areaById的实体===>{}", areaById);
                if (Objects.nonNull(areaById)) {
                    String newStr = areaById.getSequence().replaceAll("-$", "");
                    securityDevice.setAreaId(newStr);
                    securityDevice.setAreaName(area.getVal());
                }
                securityDevice.setDeviceName(baseArchivesInfo.getName());
                securityDevice.setThirdPartyId(baseArchivesInfo.getThirdPartyId());
                securityDevice.setLntAndLat(expandInfo.getLntAndLat());
                securityDevice.setDeviceProdId(productInfo.getId());
                securityDevice.setDeviceProdName(productInfo.getName());
                securityDevice.setBrandName(productInfo.getBrandName());
                DeviceProductInfoDTO.ProductCategory productCategory = productInfo.getProductCategory();
                securityDevice.setDeviceProdCategoryCode(productCategory.getCode());
                securityDevice.setDeviceProdCategoryCodeConcat(productCategory.getPathCode());
                securityDevice.setDeviceProdCategoryName(productCategory.getName());
                securityDevice.setCode(deviceById.getBaseArchivesInfo().getCode());
                securityDevice.setDeviceName(deviceById.getBaseArchivesInfo().getName());
                securityDevices.add(securityDevice);
                log.debug("securityDevice的实体===>{}", securityDevice);
            }

        });
        log.debug("securityDevices===>{}", securityDevices);
        if (CollectionUtils.isNotEmpty(securityDevices)) {
            // 批量更新
            securityCommonMapper.updateDeviceBatch(securityDevices);
        }
    }
}
