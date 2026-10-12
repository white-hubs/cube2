package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.result;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.enums.InspectOfflineChunkTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * 离线同步 manifest 中的分片摘要。
 */
@Data
@Builder
public class InspectOfflineChunkManifestResult {
    private String chunkId;
    private InspectOfflineChunkTypeEnum type;
    private Long taskId;
    private Long revision;
    private Integer partNo;
    private Integer partCount;
    private Long compressedSize;
    private String sha256;
}
