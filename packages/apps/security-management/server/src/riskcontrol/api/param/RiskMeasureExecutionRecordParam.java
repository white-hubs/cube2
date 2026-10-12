package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

import java.util.List;

@Data
public class RiskMeasureExecutionRecordParam {
    /**
     * 管控措施 ID；离线批量同步时必填，单条创建接口由路径参数提供。
     */
    private Long measureId;

    /**
     * 措施执行结果。
     * 取值建议：COMPLETED=已完成，EXECUTING/PARTIAL=执行中或部分完成，PENDING/NOT_EXECUTED=未完成。
     */
    private String executionResult;

    /**
     * 措施执行说明。
     */
    private String executionDesc;

    /**
     * 执行人用户 ID。
     */
    private String executorUserId;

    /**
     * 执行 GPS 位置 JSON。
     */
    private String gpsLocation;

    /**
     * 附件文件 ID 集合，最多 9 个；文件场景码为 RISK::CONTROL_MEASURE_EXECUTION_ATTACHMENT。
     */
    private List<String> fileIds;
}
