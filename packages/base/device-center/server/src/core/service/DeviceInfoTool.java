package com.guhecloud.cube.base.device.center.core.service;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoWithFullAreaDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;

import java.util.Objects;

/**
 * @author pengjian
 * @date 2024/1/19 9:56
 * @description
 */
public class DeviceInfoTool {

    public static void setPicture(DeviceBaseArchivesInfoDTO baseArchivesInfo, DeviceProductInfoDTO productInfo) {
        if (Objects.isNull(baseArchivesInfo) || Objects.isNull(productInfo)) {
            return;
        }
        baseArchivesInfo.setPicture(productInfo.getPicture());
    }

    public static void setPicture(DeviceBaseArchivesInfoWithFullAreaDTO baseArchivesInfo, DeviceProductInfoDTO productInfo) {
        if (Objects.isNull(baseArchivesInfo) || Objects.isNull(productInfo)) {
            return;
        }
        baseArchivesInfo.setPicture(productInfo.getPicture());
    }
}
