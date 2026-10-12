package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskAreaPageParam extends Paging {
    /**
     * 关键词，按风险区域编号、名称、负责人用户 ID 模糊匹配。
     */
    private String keyword;

    /**
     * 所属部门 ID，用于按风险区域所属部门筛选。
     */
    private String departmentId;

    /**
     * 区域类型，用于按室内、室外、综合等区域物理空间类型筛选。
     */
    private String areaType;

    /**
     * 现状风险等级筛选，取值：MAJOR=重大风险，LARGE=较大风险，GENERAL=一般风险，LOW=低风险。
     * 按风险区域下风险单元最新控制风险等级的最高等级筛选。
     */
    private String riskLevel;
}
