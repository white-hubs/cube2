package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskIdentificationPageParam extends Paging {
    /**
     * 关键词，按风险对象名称模糊匹配。
     */
    private String keyword;

    /**
     * 风险区域 ID。
     */
    private Long areaId;

    /**
     * 风险单元 ID。
     */
    private Long unitId;

    /**
     * 风险对象 ID。
     */
    private Long objectId;

    /**
     * 辨识人用户 ID。
     */
    private String identifierUserId;

    /**
     * 辨识提交开始时间。
     */
    private LocalDateTime startTime;

    /**
     * 辨识提交结束时间。
     */
    private LocalDateTime endTime;
}
