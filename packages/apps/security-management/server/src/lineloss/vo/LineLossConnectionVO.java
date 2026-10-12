package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LineLossConnectionVO {
    private Long id;
    private String lineName;
    /**
     * 开始节点 id
     */
    private Long fromNodeId;
    /**
     * 结束节点 id
     */
    private Long toNodeId;
    /**
     * 网页样式 (前端定位用)
     */
    private String webStyle;
    /**
     * 起始节点位置
     */
    private String fromNodeLocation;
    /**
     * 结束节点位置
     */
    private String toNodeLocation;
    /**
     * 输入设备id
     */
    private Long fromDeviceId;
    private DeviceBaseArchivesInfoDTO fromDevice;
    /**
     * 输出设备id
     */
    private Long toDeviceId;
    private DeviceBaseArchivesInfoDTO toDevice;
    /**
     * 最近一次的线损率
     */
    private BigDecimal lineLossRate;
    /**
     * 所在位置
     */
    private String location;
    private String createBy;
    private LocalDateTime createTime;
    /**
     * 报警配置的条件,符合和值用逗号隔开，多组条件用分号隔开即：符合,值;符合,值(例如：EQ,10;GE,12)
     */
    private String condition;
}
