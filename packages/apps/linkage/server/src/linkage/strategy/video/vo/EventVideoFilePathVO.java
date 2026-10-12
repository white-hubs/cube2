package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.vo;

import com.guhecloud.apps.linkage.metadata.enumeration.VideoActionTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventVideoFilePathVO {

    private Long id;

    /**
     * 动作记录主键
     */
    private Long eventVideoRecordId;

    /**
     * 事件联动主键
     */
    private Long eventLinkageId;

    /**
     * 事件主键
     */
    private Long eventId;

    /**
     * 联动主键
     */
    private Long linkageId;

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 摄像机token
     */
    private String token;

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 动作类型
     */
    private VideoActionTypeEnum type;

    /**
     * 外网文件路径
     */
    private String extranetFilePath;

    /**
     * 内网文件路径
     */
    private String intranetFilePath;
}
