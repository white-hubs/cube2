package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectLevelEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.platform.job.jobtask.enums.JobLoopTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlanAddParam {

    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 类型
     */
    @NotNull(message = "类型不能为空")
    private Long typeId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 级别 紧急/普通
     */
    private InspectLevelEnum level;

    /**
     * 生效开始时间
     */
    private LocalDateTime validBeginTime;

    /**
     * 生效结束时间
     */
    private LocalDateTime validEndTime;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 1开启 0关闭
     */
    private Boolean enable;

    /**
     * json字符串
     */
    private String limitTimeJson;

    /**
     * 循环类型 0=单次 1=循环
     */
    private Boolean cycle;

    /**
     * 表达式ID
     */
    private Long cronId;
    /**
     * 执行周期;周期类型:单次;每日;每周;每月
     */
    private JobLoopTypeEnum cycleType;

    /**
     * 执行日期 周一、周二、 一号、二号
     */
    private String selection;

    /**
     * 执行时间
     */
    private String hour;
    /**
     * 是否需要拆解(2.0)
     */
    @NotNull(message = "是否需要拆解不能为空")
    private boolean isSplit;

    /**
     * 是否需要稽查(2.0)
     */
    @NotNull(message = "是否需要稽查不能为空")
    private boolean isCheck;

    /**
     * 稽查人员类型1个人，2部门(2.0)
     */
    private String checkUserType;

    /**
     * 稽查人部门
     */
    private String checkDeptIds;

    /**
     * 稽查人岗位
     */
    private String checkPostIds;

    /**
     * 稽查角色
     */
    private String checkRoleIds;

    /**
     * 稽查人ids
     */
    private String checkUserIds;

    /**
     * 接收类型，1＝个人，2＝部门
     */
    private String receiveType;

    /**
     * 接收部门
     */
    private String receiveDeptIds;

    /**
     * 接收岗位
     */
    private String receivePostIds;

    /**
     * 接受角色
     */
    private String receiveRoleIds;

    /**
     * 接收人ID
     */
    private String receiveIds;

    /**
     * 备注
     */
    //@Length(max = 100)
    private String remark;

    /**
     * 巡检对象数总数
     */
    private Integer targetNumTotal;

    /**
     * 巡检方式
     */
    private String method;

    /**
     * 计划的巡检对象-巡检标准-巡检项关系的集合
     */
    @NotEmpty(message = "巡检标准和巡检项不能为空！")
    List<PlanRelationPO> planRelationList;
}
