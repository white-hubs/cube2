package com.guhecloud.cube.base.device.center.open.service;

import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceExpandMapper;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceExpandInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.open.mapper.DeviceCenterMapper;
import com.guhecloud.cube.base.device.center.open.mapper.ProductInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pengjian
 * @date 2024/1/16 15:15
 * @description
 */
@Service
public class DeviceInstanceService {
    @Autowired
    private DeviceCenterMapper deviceCenterMapper;
    @Autowired
    private CenterBizDeviceExpandMapper centerBizDeviceExpandMapper;
    @Autowired
    private ProductInfoMapper productInfoMapper;

    /**
     * 根据设备获取产品信息
     *
     * @param deviceId
     *
     * @return
     */
    public DeviceProductInfoDTO getProductInfoByDeviceId(Long deviceId) {
        return productInfoMapper.selectProductInfoByDeviceId(deviceId);
    }

    public DeviceExpandInfoDTO getExpandInfoByDeviceId(Long deviceId) {
        return centerBizDeviceExpandMapper.getExpandByDeviceId(deviceId);
    }

    public DeviceBaseArchivesInfoDTO getBaseInfoByDeviceId(Long deviceId) {
        return deviceCenterMapper.getDeviceBaseArchivesInfoByDeviceId(deviceId);
    }

    public DeviceBaseArchivesInfoDTO getBaseInfoByDeviceCode(String deviceCode) {
        return deviceCenterMapper.getDeviceBaseArchivesInfoByDeviceCode(deviceCode);
    }

    /**
     * 获取设备详情
     *
     * @param id
     *
     * @return
     */
    public DeviceDetailDTO getById(Long id) {

        DeviceBaseArchivesInfoDTO baseInfoByDevice = getBaseInfoByDeviceId(id);
        DeviceProductInfoDTO productInfoByDevice = getProductInfoByDeviceId(id);
        return new DeviceDetailDTO(
                baseInfoByDevice,
                getExpandInfoByDeviceId(id),
                productInfoByDevice
        );
    }
}
