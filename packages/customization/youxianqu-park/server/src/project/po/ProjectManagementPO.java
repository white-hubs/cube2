package com.guhecloud.cube.customized.youxianqu.park.management.server.project.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 项目管理数据实体模型
 *
 * @author lanchun
 * @date 2024/8/28 16:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
@TableName(value = "PROJECT_MANAGE")
public class ProjectManagementPO {

    /**
     * 顺序号
     */
    @TableField(value = "ID")
    private Long id;

    /**
     * 项目名称
     */
    @TableField(value = "PROJECT_NAME")
    private String projectName;

    /**
     * 申请金额
     */
    @TableField(value = "APPLY_AMOUNT")
    private BigDecimal applyAmount;

    /**
     * 项目主负责人
     */
    @TableField(value = "PROJECT_DUTY1")
    private String projectDuty1;

    /**
     * 项目副负责人
     */
    @TableField(value = "PROJECT_DUTY2")
    private String projectDuty2;

    /**
     * 开始时间
     */
    @TableField(value = "START_DATE")
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    @TableField(value = "END_DATE")
    private LocalDateTime endDate;

    /**
     * 负责部门
     */
    @TableField(value = "DUTY_DETP")
    private String dutyDetp;

    /**
     * 申请时间
     */
    @TableField(value = "APPLY_DATE")
    private LocalDateTime applyDate;

    /**
     * 项目类型
     */
    @TableField(value = "PROJECT_TYPE")
    private String projectType;

    /**
     * 项目编号
     */
    @TableField(value = "PROJECT_NO")
    private String projectNo;

    /**
     * 批准金额
     */
    @TableField(value = "APPROVE_AMOUNT")
    private BigDecimal approveAmount;

    /**
     * 附件
     */
    @TableField(value = "ATTACHMENT")
    private String attachment;

    /**
     * 附件描述
     */
    @TableField(value = "DESCRIPTION")
    private String description;

    /**
     * 送审状态
     */
    @TableField(value = "STATUS")
    private String status;

    /**
     * 是否立项
     * 0.未立项,1.已立项
     */
    @TableField(value = "FOUND_STATUS")
    private Integer foundStatus;

    /**
     * 是否结项
     * 0.未结,1.已结
     */
    @TableField(value = "END_STATUS")
    private Integer endStatus;

    /**
     * 结项时间
     */
    @TableField(value = "RESULTS_DATE")
    private LocalDateTime resultsDate;

    /**
     * 是否鉴定
     * 0.未鉴定,1.已鉴定
     */
    @TableField(value = "IDENTIFY_STATUS")
    private Integer identifyStatus;

    /**
     * 鉴定时间
     */
    @TableField(value = "IDENTIFY_DATE")
    private LocalDateTime identifyDate;

    /**
     * 合计预算
     */
    @TableField(value = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    /**
     * 公司编码
     */
    @TableField(value = "UNITCODE")
    private String unitCode;

    /**
     * 项目模板
     */
    @TableField(value = "TEMPLATE")
    private String template;

}
