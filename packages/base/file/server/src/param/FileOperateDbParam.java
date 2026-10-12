package com.guhecloud.cube.base.file.server.param;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.file.server.entity.File;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.enumeration.FileStorageStrategyEnum;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-11  10:24
 * @Description:
 * @Version: 1.0
 */
@Data
public class FileOperateDbParam {
    /**
     * 市场租户编号
     */
    private Integer tenant;
    /**
     * 场景编号
     */
    private String sceneCode;
    /**
     * 文件
     */
    private MultipartFile file;
    /**
     * 水印内容
     */
    private String watermark;
    /**
     * 文件信息id
     */
    private Long fileId;
    /**
     * 存储策略
     */
    private String storageStrategy;

    public File getFile(FileConfig fileConfig) {
        Long fileId = ObjectUtils.isNotEmpty(this.fileId) ? this.fileId : IdWorker.getId();
        String suffix = StringUtils.getFilenameExtension(file.getOriginalFilename());
        File f = File.builder()
                .id(fileId)
                .name(file.getOriginalFilename())
                .suffix(suffix)
                .size(file.getSize())
                .path("/" + fileConfig.getBucket() + "/" + fileId + "." + suffix)
                .type(suffix)
                .bucketFileName(fileId + "." + suffix)
                .refCount(0)
                .accessPrefix(fileConfig.getAccessPrefix())
                .configId(fileConfig.getId())
                .sceneCode(this.sceneCode)
                .storageStrategy(FileStorageStrategyEnum.getFileStorageStrategy(storageStrategy))
                .build();
        f.setDeleted(false);
        try {
            EntityAuditor.populateAudit(f);
        } catch (Exception e) {
            EntityAuditor.populateInnerAudit(f);
        }
        // 拆分路径，获取文件名称
        String[] path = f.getPath().split("/");
        if (ObjectUtils.isNotEmpty(path)) {
            f.setBucketFileName(path[path.length - 1]);
        }
        return f;

    }

}
