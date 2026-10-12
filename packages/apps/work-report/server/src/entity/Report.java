package com.guhecloud.cube.srv.gen.reportmanagement.server.entity;

import annotation.FormField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import jakarta.validation.constraints.Min;
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
@TableName("report")
public class Report extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 汇报名称
     */
    @FormField(label = "汇报名称")
    private String reportName;

    /**
     * 汇报类型id
     */
    @FormField(label = "汇报类型ID")
    private Long typeId;

    /**
     * 汇报内容
     */
    @FormField(label = "汇报内容")
    private String content;

    /**
     * 附件id
     */
    @Min(value = 1, message = "文件主键不能小于1")
    private Long appendixId;

    /**
     * 协同问题
     */
    private String question;

    /**
     * 发起人（汇报人）
     */
    @FormField(label = "发起人（汇报人）")
    private String reportBy;

    /**
     * 汇报对象id
     */
    private Long reportPid;

    /**
     * 汇报对象名称
     */
    @FormField(label = "汇报对象")
    private String reportPname;

    /**
     * 发起状态 ，默认处理中，已完成，已撤销
     */
    @FormField(label = "汇报状态")
    private FlowResult status;

    /**
     * 发起时间
     */
    @FormField(label = "汇报时间")
    private LocalDateTime startTime;

    /**
     * 处理结果
     */
    private String processResult;

    /**
     * 处理意见
     */
    private String processOpinion;

    /**
     * 标题
     */
    private String title;

    /**
     * 下一步工作
     */
    private String nextStep;
    /**
     * 部门id
     */
    @FormField(label = "部门选择")
    private String departmentId;

    /**
     * 流程实例id
     */
    private String processInsId;
}
