package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskControlMeasurePageParam extends Paging {
    /**
     * 关键词，按管控措施内容模糊匹配。
     */
    private String keyword;

    /**
     * 风险单元 ID。
     */
    private Long unitId;

    /**
     * 风险对象 ID。
     */
    private Long objectId;

    /**
     * 风险等级筛选，按风险对象当前控制风险等级过滤。
     */
    private String riskLevel;

    /**
     * 管控类型筛选。
     */
    private String controlType;

    /**
     * 措施执行状态筛选，取值：PENDING=待执行，EXECUTING=执行中，COMPLETED=已完成。
     */
    private String status;

    /**
     * 执行人筛选；传 current 表示当前登录人。
     */
    private String executor;

    /**
     * 输出格式；导出接口固定传 xlsx。
     */
    private String format;
}
