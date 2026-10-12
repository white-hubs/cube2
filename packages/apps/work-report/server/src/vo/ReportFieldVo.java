package com.guhecloud.cube.srv.gen.reportmanagement.server.vo;

import annotation.FormField;
import com.guhecloud.cube.srv.gen.reportmanagement.server.enumeration.StatusEnum;
import lombok.Data;

import jakarta.validation.constraints.Min;

@Data
public class ReportFieldVo {

    /**
     * 汇报名称
     */
    @FormField(label = "汇报名称")
    private String reportName;

    /**
     * 汇报类型id
     */
    @FormField(label = "汇报类型")
    private Long typeId;

    /**
     * 汇报类型id
     */
    @FormField(label = "汇报类型")
    private String typeName;

    /**
     * 汇报内容
     */
    @FormField(label = "汇报内容")
    private String content;

    /**
     * 附件id
     */
    @Min(value = 1, message = "文件主键不能小于1")
    @FormField(label = "附件id")
    private Long appendixId;

    /**
     * 发起人id（汇报人id）
     */
    @FormField(label = "发起人ID（汇报人ID）")
    private String reportBy;

    /**
     * 发起人（汇报人）
     */
    @FormField(label = "发起人（汇报人）")
    private String reportByName;

    /**
     * 汇报对象名称
     */
    @FormField(label = "汇报对象")
    private String reportPname;

    /**
     * 发起状态 ，默认处理中，已完成，已撤销
     */
    @FormField(label = "汇报状态")
    private StatusEnum status;

    /**
     * 部门id
     */
    @FormField(label = "部门选择")
    private String departmentId;

    /**
     * 部门id
     */
    @FormField(label = "汇报标题")
    private String title;

    /**
     * 下一步工作
     */
    @FormField(label = "下一步工作")
    private String nextStep;
}
