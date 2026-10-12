package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LineLossNodeVO {
    private Long id;
    private String code;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点类型
     */
    private String type;
    /**
     * 节点关联设备 ids
     */
    private String deviceIds;
    /**
     * 节点关联设备列表
     */
    private List<DeviceBaseArchivesInfoDTO> deviceList;
    /**
     * 节点关联子节点ids
     */
    private String childrenIds;
    /**
     * 所在位置
     */
    private String location;
    private String description;
    /**
     * 网页样式 (前端定位用)
     */
    private String webStyle;
    private String createBy;
    private LocalDateTime createTime;
}
