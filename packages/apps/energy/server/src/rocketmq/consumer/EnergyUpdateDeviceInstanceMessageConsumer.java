package com.guhecloud.cube.srv.gen.energymanagement.server.rocketmq.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceChange;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceInfo;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceChangeEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterDeviceMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.database.entity.BasicEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = Const.DEVICE_CHANGE_TOPIC + "_energy_device_update",
                         topic = Const.DEVICE_CHANGE_TOPIC, selectorType = SelectorType.TAG
)
public class EnergyUpdateDeviceInstanceMessageConsumer implements RocketMQListener<DeviceChange> {

    private final DeviceCenterStdClient deviceCenterClient;

    private final MeterDeviceMapper meterDeviceMapper;

    private final AreaClient areaClient;

    @Override
    public void onMessage(DeviceChange message) {
        //数据同步 品牌code去除（和iot商量）
        List<Pair<DeviceInfo, DeviceChangeEnum>> deviceChangePairs = message.getDeviceChangePairs();
        List<Pair<DeviceInfo, DeviceChangeEnum>> collect = deviceChangePairs.stream()
                .filter(deviceChangePair -> deviceChangePair.getVal() == DeviceChangeEnum.UPDATE).collect(Collectors.toList());
        for (Pair<DeviceInfo, DeviceChangeEnum> deviceInfoDeviceChangeEnumPair : collect) {
            DeviceInfo deviceInfo = deviceInfoDeviceChangeEnumPair.getKey();
            MeterDevice meterDevice = meterDeviceMapper.selectOne(Wrappers.<MeterDevice>lambdaQuery()
                                                                          .eq(MeterDevice::getDeviceId, deviceInfo.getDeviceId())
                                                                          .eq(BasicEntity::getDeleted, false));
            SessionContextHolder.requestTenant(meterDevice.getTenant());
            DeviceDetailDTO deviceById = deviceCenterClient.getDeviceById(deviceInfo.getDeviceId());

            DeviceBaseArchivesInfoDTO baseArchivesInfo = deviceById.getBaseArchivesInfo();
            DeviceProductInfoDTO productInfo = deviceById.getProductInfo();
            meterDevice.setCode(baseArchivesInfo.getCode());
            meterDevice.setDeviceName(baseArchivesInfo.getName());
            meterDevice.setDeviceTypeId(productInfo.getProductCategory().getCode());
            meterDevice.setDeviceType(productInfo.getProductCategory().getName());
            if (Objects.nonNull(baseArchivesInfo.getArea())) {
                Long areaId = baseArchivesInfo.getArea().getKey();
                String areaNamesById = areaClient.getAreaNamesById(areaId);
                meterDevice.setDeviceArea(areaNamesById);
                AreaDTO areaById = areaClient.getAreaById(areaId);
                meterDevice.setDeviceAreaId(areaById.getSequence());
            }
            meterDeviceMapper.updateById(meterDevice);
        }
    }
}
