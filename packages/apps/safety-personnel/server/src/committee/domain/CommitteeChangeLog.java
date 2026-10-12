package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 安全生产委员会变更记录。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sp_committee_change_log")
public class CommitteeChangeLog extends BasicEnhancedTenantEntity<Long> {

    /**
     * 变更记录ID。
     */
    private Long id;

    /**
     * 安委会ID。
     */
    private Long committeeId;

    /**
     * 变更类型。
     */
    private String changeType;

    /**
     * 变更内容JSON。
     */
    private String changeContent;

    /**
     * 操作人OUAA用户ID。
     */
    private Long operatorId;

    /**
     * 操作人姓名。
     */
    private String operatorName;

    /**
     * 变更时间。
     */
    private LocalDateTime changeTime;
}
