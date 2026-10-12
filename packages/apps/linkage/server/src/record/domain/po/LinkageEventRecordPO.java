package com.guhecloud.cube.biz.linkage.server.record.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("linkage_event_record")
public class LinkageEventRecordPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 事件编号
     */
    private String code;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 事件id
     */
    private Long eventId;

    /**
     * 触发时间
     */
    private LocalDateTime eventTime;

    /**
     * 触发级别
     */
    private Long levelId;

    /**
     * 级别编号
     */
    private String levelCode;

    /**
     * 级别名称
     */
    private String levelName;

    /**
     * 设备id
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
     * 设备区域
     */
    private String deviceAreaName;

    /**
     * 设备经纬度
     */
    private String deviceLntAndLat;

    /**
     * 设备位置信息
     */
    private String deviceAddress;

    /**
     * 事件分类主键
     */
    private Long eventTypeId;

    /**
     * 事件分类编号
     */
    private String eventTypeCode;

    /**
     * 事件分类名称
     */
    private String eventTypeName;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * iot快照
     */
    private String snapshot;
}
