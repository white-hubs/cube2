package com.guhecloud.cube.customization.xialing.server.drone.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 霞凝无人机成果数据实体
 * <p>
 * 对应数据库表 drone_result，存储从霞凝平台同步的任务执行成果数据。
 * 成果数据包括照片、视频等任务执行产生的文件及其元数据（如位置、分析状态等）。
 * </p>
 *
 * @author system
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("drone_result")
public class DroneResult extends BasicEnhancedTenantEntity<Long> {
    /** 主键ID */
    private Long id;
    /** 霞凝平台的成果ID（第三方ID） */
    private Long thirdResultId;
    /** 霞凝平台的任务ID（第三方ID） */
    private Long thirdTaskId;
    /** 霞凝平台的调度ID（第三方ID） */
    private Long thirdScheduleId;
    /** 航迹名称 */
    private String trackName;
    /** 任务名称 */
    private String taskName;
    /** 机巢序列号 */
    private String nestSn;
    /** 无人机序列号 */
    private String droneSn;
    /** 文件Key，用于下载文件 */
    private String fileKey;
    /** 缩略图Key，用于下载缩略图 */
    private String thumbKey;
    /** 文件名 */
    private String fileName;
    /** 文件MD5校验值 */
    private String fileMd5;
    /** AI分析状态 */
    private Boolean analysisStatus;
    /** 文件大小（字节） */
    private Long fileSize;
    /** 上传类型 */
    private Integer uploadType;
    /** 坐标值 */
    private String coordValue;
    /** 文件类型（1-照片, 2-视频, 3-其他） */
    private Integer fileType;
    /** 负载类型 */
    private String podType;
    /** 通道类型 */
    private Integer channelType;
    /** 经度 */
    private BigDecimal longitude;
    /** 纬度 */
    private BigDecimal latitude;
    /** 海拔高度 */
    private BigDecimal altitude;
    /** 判定状态 */
    private String judgeStatus;
    /** 成果类型 */
    private String resultType;
    /** 照片类型 */
    private Integer photoType;
    /** 霞凝平台的创建时间 */
    private LocalDateTime thirdCreateTime;
    /** 原始JSON数据，用于调试和追溯 */
    private String rawJson;
    /** 最后一次同步时间 */
    private LocalDateTime lastSyncTime;
}
