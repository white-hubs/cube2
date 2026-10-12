package com.guhecloud.cube.base.file.server.param;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lzy
 * @date 2022/1/7
 */
@Data
public class FileModifyParam {

    /**
     * 市场租户编号
     */
    private Integer tenant = 0;

    /**
     * 业务编号不能为空
     */
    private String businessKey;

    /**
     * 文件
     */
    private MultipartFile file;

    private String tag;
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
