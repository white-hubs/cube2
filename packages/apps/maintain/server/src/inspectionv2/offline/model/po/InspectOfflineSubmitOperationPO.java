package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.enums.InspectOfflineSubmitStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 离线提交幂等操作持久化对象。
 * <p>
 * 用 clientOperationId 和 payloadHash 识别重复提交、响应丢失查询和 payload 冲突。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inspect_offline_submit_operation")
public class InspectOfflineSubmitOperationPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long userId;
    private String clientId;
    private String clientBatchId;
    private String clientOperationId;
    private Long recordId;
    private Long taskId;
    private Long recordRevision;
    private String payloadHash;
    private String requestJson;
    private InspectOfflineSubmitStatusEnum status;
    private Long serverRecordId;
    private Long serverRecordRevision;
    private String errorCode;
    private String errorMessage;
}
