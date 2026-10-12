package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.result;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 离线批量提交响应。
 */
@Data
@Builder
public class InspectOfflineSubmitBatchResult {
    private String clientBatchId;
    private List<InspectOfflineSubmitItemResult> results;
}
