package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/13 10:59
 * @Description:
 */
@Data
@TableName("loose_works_reform")
public class ProjectReform {
    private Long id;
    /**
     * 小散工程名称
     */
    private String projectScheduleId;
    /**
     * 工程监管名称
     */
    private String projectScheduleName;
    /**
     * 整改单名称
     */
    private String processInsName;

    /**
     * 流程实例id
     */
    private String processInsId;
    /**
     * 申请人
     */
    private Long startUser;
    /**
     * 申请部门
     */
    private Long startDep;
    /**
     * 审批完成时间
     */
    private LocalDateTime passTime;
    /**
     * 审批结果
     */
    private String approveResult;
    /**
     * 工单编号
     */
    private String reformNum;

    //市场租户
    private Integer marketTenant;
    //市场名称
    private String marketName;

    private Integer tenant;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean deleted;
}
