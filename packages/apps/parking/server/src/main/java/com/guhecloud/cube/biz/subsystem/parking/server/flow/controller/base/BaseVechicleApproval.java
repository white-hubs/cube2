package com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.base;

import annotation.FormField;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/10 11:54
 * @Description:
 */

@Data
public class BaseVechicleApproval {
    @FormField(label = "车主姓名")
    @NotNull(message = "流程配置车主姓名未配置")
    private List<Long> userId;
    @NotNull(message = "流程配置身份证号未配置")
    @FormField(label = "身份证号")
    private String idCard;
    @FormField(label = "人员类别")
    private String type;
    @FormField(label = "所在部门")
    private Long deptId;
}
