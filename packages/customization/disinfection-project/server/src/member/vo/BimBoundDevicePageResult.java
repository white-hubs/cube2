package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BIM 看板-绑定设备分页结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BimBoundDevicePageResult {
    private Long id;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备状态
     */
    private DeviceBaseArchivesInfoDTO.Status status;
    /**
     * 绑定坐标-纬度
     */
    private Double latitude;
    /**
     * 绑定坐标-经度
     */
    private Double longitude;
    /**
     * 设备产品名称
     */
    private String productName;
    /**
     * 详细位置
     */
    private String locationDetail;
    /**
     * 剩余药水量
     */
    private String remainingLiquid;
    /**
     * 剩余药水量单位
     */
    private String remainingLiquidUnit;
}

