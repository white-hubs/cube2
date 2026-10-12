package com.guhecloud.subsystem.requester.sdk.vo.drone;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 无人机航线VO
 */
@Data
public class DroneRouteVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 文件URL（原始文件访问地址）
     */
    private String fileUrl;
    /**
     * Web端文件URL（Web端访问地址）
     */
    private String webFileUrl;
    /**
     * 文件MD5值（用于文件校验）
     */
    private String fileMd5;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 航线名称
     */
    private String trackName;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 状态标识（航线状态）
     */
    private Integer status;
    /**
     * 航线长度（米）
     */
    private BigDecimal trackLength;
    /**
     * 航点数量（航线包含的航点总数）
     */
    private Integer waypointCount;
    /**
     * 预计执行时间（秒）
     */
    private Integer estimatedTime;
    /**
     * 类型标识（航线类型）
     */
    private Integer type;
    /**
     * 机型标识（适用的无人机型号）
     */
    private Integer model;
    /**
     * 创建方式（标识航线是如何创建的）
     */
    private Integer createType;
    /**
     * 照片总数（航线计划拍摄的照片数量）
     */
    private Integer photoTotal;
    /**
     * 高度模式（飞行高度控制模式）
     */
    private Integer altitudeMode;
    /**
     * 机库序列号（关联的机库设备）
     */
    private String nestSn;
    /**
     * 机库名称
     */
    private String nestName;
    /**
     * 飞行高度（米）
     */
    private Integer height;
    /**
     * 飞行速度（米/秒）
     */
    private Integer speed;
    /**
     * 俯仰角度（相机俯仰角，度）
     */
    private Integer pitchAngle;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
