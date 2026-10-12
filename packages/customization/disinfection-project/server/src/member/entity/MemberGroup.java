package com.guhecloud.cube.srv.gen.disinfection.server.member.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分组(家)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberGroup extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 所属人id
     */
    private Long memberUserId;
    /**
     * 分组名
     */
    private String groupName;
    /**
     * 分组人数
     */
    private Integer userCount;
    /**
     * 分组设备数
     */
    private Integer deviceCount;
    /**
     * 描述
     */
    private String description;
}
