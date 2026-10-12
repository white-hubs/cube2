package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 消杀资料实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("disinfection_material")
public class DisinfectionMaterial extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 资料名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件类型
     */
    @TableField("file_type_id")
    private Long fileTypeId;

    /**
     * 文件分类名称
     */
    @TableField("file_type_name")
    private String fileTypeName;

    /**
     * 上传的文件ID
     */
    @TableField("file_id")
    private Long fileId;

    /**
     * 上传人ID
     */
    @TableField("upload_user_id")
    private Long uploadUserId;

    /**
     * 上传时间
     */
    @TableField("upload_time")
    private LocalDateTime uploadTime;
}