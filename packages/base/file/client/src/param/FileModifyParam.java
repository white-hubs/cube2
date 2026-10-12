package com.guhecloud.cube.base.file.client.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lzy
 * @date 2022/1/7
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileModifyParam {
    /**
     * 市场租户编号
     */
    private Integer tenant;

    /**
     * 业务编号不能为空
     */
    private String businessKey;

    /**
     * 文件
     */
    private MultipartFile file;
    /**
     * 应用code
     */
    private String applicationCode;
    /**
     * 水印内容
     */
    private String watermark;
    /**
     * 文件信息id
     */
    private Long fileId;

}
