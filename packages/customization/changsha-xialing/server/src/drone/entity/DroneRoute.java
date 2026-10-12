package com.guhecloud.cube.customization.xialing.server.drone.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 霞凝无人机航迹实体
 * <p>
 * 对应数据库表 drone_route，存储从霞凝平台同步的航迹数据。
 * 航迹是无人机飞行的路径规划，包含航点、高度、速度等配置信息。
 * </p>
 *
 * @author system
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("drone_route")
public class DroneRoute extends BasicEnhancedTenantEntity<Long> {
    /** 主键ID */
    private Long id;
    /** 霞凝平台的航迹ID（第三方ID） */
    private Long thirdRouteId;
    /** 航迹名称 */
    private String trackName;
    /** 项目名称 */
    private String projectName;
    /** 机巢序列号 */
    private String nestSn;
    /** 机巢名称 */
    private String nestName;
    /** 航迹文件URL */
    private String fileUrl;
    /** Web端航迹文件URL */
    private String webFileUrl;
    /** 航迹文件MD5校验值 */
    private String fileMd5;
    /** 航迹文件名 */
    private String fileName;
    /** 航迹状态 */
    private Integer trackStatus;
    /** 航迹类型 */
    private Integer trackType;
    /** 航迹模型 */
    private Integer trackModel;
    /** 创建类型 */
    private Integer createType;
    /** 高度模式 */
    private Integer altitudeMode;
    /** 航迹长度（米） */
    private BigDecimal trackLength;
    /** 航点数量 */
    private Integer waypointCount;
    /** 预计执行时间（秒） */
    private Integer estimatedTime;
    /** 照片总数 */
    private Integer photoTotal;
    /** 飞行高度（米） */
    private Integer height;
    /** 飞行速度（米/秒） */
    private Integer speed;
    /** 云台俯仰角（度） */
    private Integer pitchAngle;
    /** 霞凝平台的创建时间 */
    private LocalDateTime thirdCreateTime;
    /** 霞凝平台的更新时间 */
    private LocalDateTime thirdUpdateTime;
    /** 原始JSON数据，用于调试和追溯 */
    private String rawJson;
    /** 最后一次同步时间 */
    private LocalDateTime lastSyncTime;
}
