package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 构建记录VO
 */
@Data
public class BuildRecordVO {

    private Long id;

    /**
     * Generator Service 构建任务ID
     */
    private String taskId;

    /**
     * Generator Service 部署任务ID
     */
    private String deployId;

    /**
     * 产物文件名
     */
    private String artifactName;

    /**
     * 产物在 Nexus 中的访问地址
     */
    private String artifactUrl;

    /**
     * 构建状态：0=生成中，1=成功，2=失败，3=已发布
     */
    private String status;

    /**
     * 构建耗时（毫秒）
     */
    private Long durationMs;

    /**
     * 构建失败时的错误信息
     */
    private String errorMessage;

    /**
     * ZIP 产物文件地址
     */
    private String fileUrl;

    /**
     * 部署状态：0=部署中，1=部署成功，2=部署失败
     */
    private String deployStatus;

    /**
     * 发布状态
     */
    private String releaseStatus;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
