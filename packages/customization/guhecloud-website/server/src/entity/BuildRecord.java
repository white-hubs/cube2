package com.guhecloud.cube.customized.guhecloudwebsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 构建记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BuildRecord extends BasicEnhancedTenantEntity<Long> {

    @TableId(type = IdType.ASSIGN_ID)
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
     * 产物文件名，如 Products-20260422
     */
    private String artifactName;

    /**
     * 产物在 Nexus 中的访问地址
     */
    private String artifactUrl;

    /**
     * 构建状态：0=生成中，1=成功，2=失败
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
     * ZIP 产物文件地址（上传到文件服务器后的 URL）
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

}
