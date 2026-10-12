package com.guhecloud.cube.base.device.center.open.interfaces.web.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceExpandInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @date 2024/1/11 14:29
 * @description
 */
@Data
public class DeviceDetailVO {
    /**
     * 基础档案信息
     */
    private DeviceBaseArchivesInfoVO baseArchivesInfo;
    /**
     * 扩展信息
     */
    private DeviceExpandInfoDTO expandInfo;

    private DeviceProductInfoDTO productInfo;
    /**
     * 关联子设备，key为子设备id，value为子设备名称
     */
    private List<SlaveDeviceVo> slaveDeviceList;
}
