package com.guhecloud.cube.base.file.server.entity;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.file.server.enumeration.FileStorageStrategyEnum;
import com.guhecloud.cube.base.file.server.minio.domain.FileName;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件名后缀
     */
    private String suffix;

    /**
     * URL地址
     */
    private String path;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 关联数量
     */
    private Integer refCount;

    /**
     * 文件类型
     */
    private String type;

    private String accessPrefix;
    private Long configId;
    private String bucketFileName;
    /**
     * 场景code
     */
    private String sceneCode;
    /**
     * 清理时间
     */
    private LocalDateTime cleaningTime;
    /**
     * 存储策略
     */
    private String storageStrategy;

    public String getAccessPrefix() {
        if (Objects.isNull(this.accessPrefix)) {
            return "";
        }
        return accessPrefix;
    }

    public static File getFile(Long fileId, FileName fileName, Long size, String url, String accessPrefix, Long configId, String sceneCode,
                               String storageStrategy
                              ) {
        File f = File.builder()
                .id(ObjectUtils.isNotEmpty(fileId) ? fileId : IdWorker.getId())
                .name(fileName.getOriginalName())
                .suffix(fileName.getFileExtName())
                .size(size)
                .path(url + fileName.getProcessedName())
                .type(fileName.getFileExtName())
                .refCount(0)
                .accessPrefix(accessPrefix)
                .configId(configId)
                .sceneCode(sceneCode)
                .storageStrategy(FileStorageStrategyEnum.getFileStorageStrategy(storageStrategy))
                .build();
        f.setDeleted(false);
        EntityAuditor.populateInnerAudit(f);
        // 拆分路径，获取文件名称
        String[] path = f.getPath().split("/");
        if (ObjectUtils.isNotEmpty(path)) {
            f.setBucketFileName(path[path.length - 1]);
        }
        return f;
    }

    public static File getFile(FileName fileName, Long size, String url, String accessPrefix, Long configId, String sceneCode,
                               String storageStrategy
                              ) {
        return getFile(null, fileName, size, url, accessPrefix, configId, sceneCode, storageStrategy);
    }

    public static File getFile(FileName fileName, Long size, String url, String accessPrefix, Long configId) {
        return getFile(null, fileName, size, url, accessPrefix, configId, null, null);
    }

}
