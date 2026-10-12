package com.guhecloud.cube.base.device.center.metadata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 设备详情, 包含设备基础档案信息, 设备扩展信息, 设备产品信息
 * @date 2024/1/16 15:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceDetailDTO {
    /**
     * 设备基础档案信息
     */
    private DeviceBaseArchivesInfoDTO baseArchivesInfo;
    /**
     * 设备扩展信息
     */
    private DeviceExpandInfoDTO expandInfo;
    /**
     * 设备产品信息
     */
    private DeviceProductInfoDTO productInfo;
}
