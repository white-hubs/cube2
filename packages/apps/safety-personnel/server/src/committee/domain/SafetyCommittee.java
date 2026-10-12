package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安全生产委员会。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sp_safety_committee")
public class SafetyCommittee extends BasicEnhancedTenantEntity<Long> {

    /**
     * 安委会ID。
     */
    private Long id;

    /**
     * 安委会名称。
     */
    private String committeeName;

    /**
     * 负责单位/部门ID，来源OUAA组织。
     */
    private Long deptId;

    /**
     * 负责单位/部门名称快照。
     */
    private String deptName;

    /**
     * 联系电话。
     */
    private String contactPhone;

    /**
     * 简介。
     */
    private String introduction;

    /**
     * 成员数量。
     */
    private Integer memberCount;
}
