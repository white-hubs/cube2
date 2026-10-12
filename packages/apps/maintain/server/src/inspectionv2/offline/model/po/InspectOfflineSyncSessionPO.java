package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.enums.InspectOfflineSyncSessionStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 离线同步会话持久化对象。
 * <p>
 * 一条记录代表 App 发起的一次离线数据准备过程。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inspect_offline_sync_session")
public class InspectOfflineSyncSessionPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String sessionId;
    private Long userId;
    private String clientId;
    private String appVersion;
    private Integer offlineSchemaVersion;
    private String baseCursor;
    private String snapshotId;
    private InspectOfflineSyncSessionStatusEnum status;
    private Integer progress;
    private String cursor;
    private String manifestJson;
    private String inlineDeltaJson;
    private String errorCode;
    private String errorMessage;
    private LocalDateTime expiresAt;
}
