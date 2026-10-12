package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.enums.InspectOfflineChunkTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 离线同步分片持久化对象。
 * <p>
 * 保存分片元数据和 gzip JSON 正文。storageKey 保留为兼容定位符，当前实现使用数据库 BLOB 存储分片内容。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inspect_offline_sync_chunk")
public class InspectOfflineSyncChunkPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String chunkId;
    private String sessionId;
    private String snapshotId;
    private InspectOfflineChunkTypeEnum chunkType;
    private Long taskId;
    private Long revision;
    private Integer partNo;
    private Integer partCount;
    private Long compressedSize;
    private String sha256;
    private String storageKey;
    private byte[] contentGzip;
    private LocalDateTime expiresAt;
}
