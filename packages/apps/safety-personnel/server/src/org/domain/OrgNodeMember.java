package com.guhecloud.cube.srv.gen.safety.personnel.server.org.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安全管理组织展示成员。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sp_org_node_member")
public class OrgNodeMember extends BasicEnhancedTenantEntity<Long> {

    /**
     * 成员关系ID。
     */
    private Long id;

    /**
     * 安全管理组织节点ID。
     */
    private Long orgNodeId;

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 用户姓名快照。
     */
    private String userName;

    /**
     * OUAA部门名称快照，可为空。
     */
    private String deptName;

    /**
     * 手机号快照，可为空。
     */
    private String mobile;
}
