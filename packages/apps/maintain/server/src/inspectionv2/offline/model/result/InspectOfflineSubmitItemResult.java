package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.result;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.enums.InspectOfflineSubmitStatusEnum;
import lombok.Builder;
import lombok.Data;

/**
 * 离线提交单项结果。
 */
@Data
@Builder
public class InspectOfflineSubmitItemResult {
    private String clientOperationId;
    private Long recordId;
    private Long taskId;
    private InspectOfflineSubmitStatusEnum status;
    private Long serverRecordId;
    private Long serverRecordRevision;
    private String taskStatus;
    private String errorCode;
    private String errorMessage;
}
