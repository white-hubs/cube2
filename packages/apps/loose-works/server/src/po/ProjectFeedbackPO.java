package com.guhecloud.cube.srv.gen.loose.works.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 工程反馈
 * @date 2023/9/14 17:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_feedback")
public class ProjectFeedbackPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 进度id
     */
    private Long scheduleId;
    /**
     * 进度
     */
    private Integer schedule;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 验收结果
     */
    private String acceptanceResult;
    /**
     * 描述
     */
    private String description;
    /**
     * 文件
     */
    private String files;
    /**
     * 工程是否存在问题
     */
    private Boolean hasProblem;
    /**
     * 问题描述
     */
    private String problemDes;
    /**
     * 问题文件
     */
    private String problemFiles;

}
