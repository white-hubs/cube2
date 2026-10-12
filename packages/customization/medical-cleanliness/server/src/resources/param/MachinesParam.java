package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

/**
 * 机组入参
 */
@Data
public class MachinesParam {
    Long id;
    /**
     * 编码
     */
    @NotEmpty(message = "机组编码不能为空！")
    private String code;
    /**
     * 参数
     */
    private String arguments;
    /**
     * 附件（文件ids）
     */
    private String fileIds;
    /**
     * 手术室ids
     */
    private String roomIds;
    private Integer tenant;
}
