package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceExpandInfoDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.dto.DeviceLifeCycleDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author pengjian
 * @date 2022/8/30 14:38
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceLifeCycleService {

    public DeviceLifeCycleDTO get(@NotNull Long deviceId) {

        DeviceCenterStdClient deviceCenterStdClient = ApplicationContextHolder.getBean(DeviceCenterStdClient.class);
        final List<DeviceExpandInfoDTO> detail = deviceCenterStdClient.getExpandListByDeviceId(Collections.singleton(deviceId));
        if (ObjectUtils.isEmpty(detail) || ObjectUtils.isEmpty(detail.getFirst().getFactoryInfo())) {
            return null;
        }
        DeviceExpandInfoDTO.FactoryInfo factoryInfo = detail.getFirst().getFactoryInfo();
        Integer usefulLife = factoryInfo.getUsefulLife();
        //todo:待确认计算设备生命周期的时间字段是哪个
        var createTime = factoryInfo.getProductionDate();
        if (Objects.isNull(usefulLife) || Objects.isNull(createTime)) {
            return null;
        }
        var date = createTime.plusMonths(usefulLife);
        return new DeviceLifeCycleDTO(createTime, date);
    }

}
