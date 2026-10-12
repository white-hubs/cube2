package com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 课件新增命令
 *
 * @author lanchun
 * @date 2023/4/18 11:35
 */
@Data
public class IncreaseCoursewareLibraryCommand {

    /**
     * 课件名称
     */
    @NotEmpty(message = "课件名称不能为空")
    @Size(max = 20, message = "课件名称不能大于20")
    private String name;

    /**
     * 主题主键
     */
    @NotNull(message = "主题不能为空")
    private Long themeId;

    /**
     * 文件主键
     */
    @NotNull(message = "文件不能为空")
    private Long fileId;

    /**
     * 课件描述
     */
    @Size(max = 200, message = "课件名称不能大于200")
    private String remark;

    /**
     * 时长
     */
    private Integer duration;
}
