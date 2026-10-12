package com.guhecloud.cube.base.file.server.minio.domain;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * @author pengjian
 * @date 2022/7/18 17:26
 * @description 文件名称
 */
@Getter
public class FileName {

    /**
     * 原始名称
     */
    private String originalName;

    /**
     * 加工后的名称
     */
    private String processedName;

    /**
     * 文件扩展名
     */
    private String fileExtName;

    public FileName(String originalName, String processedName) {
        this.originalName = originalName;
        this.fileExtName = StringUtils.getFilenameExtension(originalName);
        this.processedName = null != processedName ? processedName : IdWorker.getId() + "." + fileExtName;
    }
}
