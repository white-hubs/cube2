package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 消杀任务执行记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("disinfection_task_execute_record")
public class DisinfectionTaskExecuteRecord extends BasicEnhancedTenantEntity<Long> {

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
     * 任务ID
     */
    @TableField("task_id")
    private Long taskId;

    /**
     * 执行者ID
     */
    private Long executorId;
    /**
     * 任务进度
     */
    private BigDecimal process;

    /**
     * 任务说明
     */
    @TableField("task_desc")
    private String taskDesc;

    /**
     * 任务上传的附件
     */
    private String appendix;

    /**
     * 重点场所消杀记录（json）
     */
    @TableField("key_place_record")
    private String keyPlaceRecord;
}