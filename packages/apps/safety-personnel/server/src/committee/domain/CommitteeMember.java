package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 安全生产委员会成员。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sp_committee_member")
public class CommitteeMember extends BasicEnhancedTenantEntity<Long> {

    /**
     * 成员关系ID。
     */
    private Long id;

    /**
     * 安委会ID。
     */
    private Long committeeId;

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 用户姓名快照。
     */
    private String userName;

    /**
     * 手机号快照。
     */
    private String phone;

    /**
     * OUAA部门名称快照。
     */
    private String deptName;

    /**
     * OUAA岗位名称快照。
     */
    private String postName;

    /**
     * 加入时间。
     */
    private LocalDateTime joinTime;

    /**
     * 是否在任。
     */
    private Boolean active;
}
