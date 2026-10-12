package com.guhecloud.subsystem.requester.sdk.vo.drone;

import lombok.Data;

/**
 * 无人机任务结果VO
 */
@Data
public class DroneResultVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 航线名称
     */
    private String trackName;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 机库序列号
     */
    private String nestSn;
    /**
     * 无人机序列号
     */
    private String droneSn;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 计划ID
     */
    private String scheduledId;
    /**
     * 文件密钥（用于文件访问）
     */
    private String fileKey;
    /**
     * 缩略图密钥（用于缩略图访问）
     */
    private String thumbKey;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件MD5值（用于文件校验）
     */
    private String fileMd5;
    /**
     * 分析状态（是否已完成分析）
     */
    private Boolean analysisStatus;
    /**
     * 文件大小（字节）
     */
    private Long fileSize;
    /**
     * 上传类型（标识文件上传方式）
     */
    private Integer uploadType;
    /**
     * 坐标值（地理位置信息）
     */
    private String coordValue;
    /**
     * 文件类型（如图片、视频等）
     */
    private Integer fileType;
    /**
     * 负载类型（无人机搭载的设备类型）
     */
    private String podType;
    /**
     * 通道类型（数据传输通道类型）
     */
    private Integer channelType;
    /**
     * 经度坐标
     */
    private Double longitude;
    /**
     * 纬度坐标
     */
    private Double latitude;
    /**
     * 海拔高度（米）
     */
    private Double altitude;
    /**
     * 判定状态（任务结果判定状态）
     */
    private String judgeStatus;
    /**
     * 类型标识
     */
    private String type;
    /**
     * 子类型标识
     */
    private Integer ptype;
    /**
     * 创建时间（时间戳）
     */
    private Long createTime;
}
