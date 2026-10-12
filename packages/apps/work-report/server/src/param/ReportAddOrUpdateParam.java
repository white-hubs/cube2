package com.guhecloud.cube.srv.gen.reportmanagement.server.param;

import com.guhecloud.cube.srv.gen.reportmanagement.server.enumeration.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportAddOrUpdateParam {
    private Long id;

    /**
     * 汇报名称
     */
    private String reportName;

    /**
     * 汇报类型id
     */
    private Long typeId;

    /**
     * 汇报内容
     */
    private String content;

    /**
     * 附件id
     */
    private Long appendixId;

    /**
     * 协同问题
     */
    private String question;

    /**
     * 发起人
     */
    private String reportBy;

    /**
     * 汇报对象id
     */
    private Long reportPid;

    /**
     * 汇报对象名称
     */
    private String reportPname;

    /**
     * 发起状态 ，默处理中，已完成，已撤销
     */
    private StatusEnum status;

    /**
     * 发起时间
     */
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
     * 部门id
     */
    private String departmentId;

    /**
     * 流程实例id
     */
    private String processInsId;

}
