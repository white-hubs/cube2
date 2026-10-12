package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.result;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.enums.InspectOfflineSyncSessionStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 离线同步会话响应。
 * <p>
 * 用于返回构建进度、游标、删除任务、分片清单或内联小增量。
 */
@Data
@Builder
public class InspectOfflineSyncSessionResult {
    private String sessionId;
    private String snapshotId;
    private InspectOfflineSyncSessionStatusEnum status;
    private String cursor;
    private LocalDateTime expiresAt;
    private Integer progress;
    private Integer pollIntervalSeconds;
    private List<Long> deletedTaskIds;
    private List<InspectOfflineChunkManifestResult> chunks;
    private Map<String, Object> inlineDelta;
    private String errorCode;
    private String errorMessage;
}
