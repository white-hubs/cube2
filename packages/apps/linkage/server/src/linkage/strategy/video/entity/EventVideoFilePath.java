package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity;

import com.guhecloud.apps.linkage.metadata.enumeration.VideoActionTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.enumeration.VideoFileSourceEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EventVideoFilePath extends BasicEnhancedTenantEntity<Long> {

    private Long id;

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
     * 文件id
     */
    private Long fileId;

    /**
     * 摄像机文件来源
     */
    private VideoFileSourceEnum source;

    /**
     * 外网文件路径
     */
    private String extranetFilePath;

    /**
     * 内网文件路径
     */
    private String intranetFilePath;
}
