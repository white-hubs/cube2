package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * 线路设备信息 VO
 */
@Data
public class LineLossDeviceVO {
    /**
     * 线路 ID
     */
    private Long lineId;
    
    /**
     * 线路名称
     */
    private String lineName;
    
    /**
     * 起始节点 ID
     */
    private Long fromNodeId;
    
    /**
     * 起始节点名称
     */
    private String fromNodeName;
    
    /**
     * 起始节点设备列表
     */
    private List<DeviceBaseArchivesInfoDTO> fromDeviceList;
    
    /**
     * 结束节点 ID
     */
    private Long toNodeId;
    
    /**
     * 结束节点名称
     */
    private String toNodeName;
    
    /**
     * 结束节点设备列表
     */
    private List<DeviceBaseArchivesInfoDTO> toDeviceList;
}
