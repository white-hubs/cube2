package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class MaterialInsertParam {

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 资料名称
     */
    @NotBlank(message = "资料名称不能为空")
    private String fileName;

    /**
     * 文件类型
     */
    @NotNull(message = "文件类型不能为空")
    private Long fileTypeId;

    /**
     * 上传的文件ID
     */
    @NotNull(message = "上传的文件ID不能为空")
    private Long fileId;

}
