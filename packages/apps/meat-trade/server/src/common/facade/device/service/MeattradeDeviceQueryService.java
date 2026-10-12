package com.guhecloud.cube.biz.meattrade.server.common.facade.device.service;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.meattrade.server.common.facade.device.vo.MeattradeDeviceVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MeattradeDeviceQueryService {

    @Autowired
    private DeviceCenterStdClient deviceCenterStdClient;

    public static final String APP_TAG_PRE = "builtin.app::";

    private static final String APPLICATION_CODE = "MEAT_TRADE";

    public List<MeattradeDeviceVO> queryDeviceByIds(Set<Long> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyList();
        }
        List<DeviceBaseDetailsDTO> devicesVo = this.deviceCenterStdClient.getDetailsListByDeviceId(deviceIds);
        return this.convertDevice(devicesVo);
    }

    public List<MeattradeDeviceVO> queryDevices() {
        List<DeviceBaseArchivesInfoDTO> devices = deviceCenterStdClient.getListByTag(Collections.singleton(
                APP_TAG_PRE + APPLICATION_CODE));
        return convertDeviceBaseArchives(devices);
    }

    public List<MeattradeDeviceVO> convertDevice(List<DeviceBaseDetailsDTO> devicesVo) {
        return devicesVo.stream().map(devicesVO ->
                                              new MeattradeDeviceVO(devicesVO.getId(), devicesVO.getDeviceCode(), devicesVO.getDeviceName())
                                     ).collect(Collectors.toList());
    }

    public List<MeattradeDeviceVO> convertDeviceBaseArchives(List<DeviceBaseArchivesInfoDTO> devicesVo) {
        return devicesVo.stream().map(devicesVO ->
                                              new MeattradeDeviceVO(devicesVO.getId(), devicesVO.getCode(), devicesVO.getName())
                                     ).collect(Collectors.toList());
    }
}
