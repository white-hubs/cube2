package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class InspectPlanCommandModel {

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
    private String typeId;

    /**
     * 类型名称
     */
    private String typeName;

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
    /*
     *//**
     * 表达式名
     *//*
    private String cronName;

    *//**
     * cron表达式
     *//*
    private String cronExpression;*/

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
     * 接收对象ID
     */
    private String receiveIds;

    /**
     * 备注
     */
    //@Length(max = 100)
    private String remark;

    @NotEmpty
    List<InspectPlanTargetReferCommandModel> planTargets;
}
