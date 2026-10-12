package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 人流区域检测信息返回参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDetectionAreaMonitorVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 区域模型
     */
    private String modelArea;

    /**
     * 备注
     */
    private String remark;

    /**
     * 设备集合
     */
    private List<DeviceBaseArchivesInfoDTO> devices;

}
