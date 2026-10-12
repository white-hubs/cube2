package com.guhecloud.cube.base.file.client.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-11  10:05
 * @Description:
 * @Version: 1.0
 */
@Data
public class FileUploadParam {
    /**
     * 市场租户编号
     */
    private Integer tenant = 0;
    @NotNull(message = "场景不能为空")
    private String sceneCode;
    @NotNull(message = "文件不能为空")
    private MultipartFile file;
    /**
     * 水印内容
     */
    private String watermark;
    /**
     * 文件信息id
     */
    private Long fileId;
}
