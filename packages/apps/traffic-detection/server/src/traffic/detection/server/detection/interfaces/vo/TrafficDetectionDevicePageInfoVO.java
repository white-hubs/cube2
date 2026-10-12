package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.AreaBaseInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.IdToName;
import lombok.Data;

import java.util.List;

@Data
public class TrafficDetectionDevicePageInfoVO {
    private Long id;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备编码
     */
    private String code;
    /**
     * 设备状态
     */
    private DeviceBaseArchivesInfoDTO.Status status;
    private IdToName department;
    private String thirdPartyId;
    private String lntAndLat;
    /**
     * 区域信息
     */
    private AreaBaseInfoDTO area;
    private Long picture;
    private DeviceProductInfoDTO productInfo;
    /**
     * 标签ID
     */
    private List<Long> tagIds;
    /**
     * 标签名称
     */
    private String tagNames;
    private String typeId;
    private String typeName;
    private List<Long> departmentIds;
    private String departmentNames;
    private String areaId;
    private String areaName;

    /**
     * 人流区域配置
     */
    private TrafficDetectionAreaDeviceVO trafficDetectionAreaConfig;
}
