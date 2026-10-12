package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Data
public class ProjectUpsertParam {

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    /**
     * 项目类型ID
     */
    @NotNull(message = "项目类型ID不能为空")
    private Long projectTypeId;

    /**
     * 项目类型名称
     */
    private String projectTypeName;

    /**
     * 业主单位
     */
    private String company;

    /**
     * 项目开始时间
     */
    @NotNull(message = "项目开始时间不能为空")
    private LocalDateTime beginTime;

    /**
     * 项目结束时间
     */
    @NotNull(message = "项目结束时间不能为空")
    private LocalDateTime endTime;

    /**
     * 项目负责人ID
     */
    @NotNull(message = "项目负责人ID不能为空")
    private Long leaderId;

    /**
     * 项目面积
     */
    private String disinfectionArea;
    /**
     * 项目所在地名称
     */
    private String disinfectionLocationName;
    /**
     * 项目所在地经纬度
     */
    @NotBlank(message = "项目所在地经纬度不能为空")
    private String disinfectionLocation;

    /**
     * 项目消杀范围经纬度，多个
     */
    @NotBlank(message = "项目消杀范围经纬度不能为空")
    private String disinfectionScope;

    /**
     * 项目说明
     */
    private String description;

    /**
     * 项目图片
     */
    private Long image;
}
