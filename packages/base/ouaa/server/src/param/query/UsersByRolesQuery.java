package com.guhecloud.cube.biz.ouaa.server.param.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author wql
 * @describe 按用户角色分页查用户信息
 * @date 2023/11/30 19:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UsersByRolesQuery extends Paging {
    /**
     * 角色id
     */
    private Set<Long> ids;
    /**
     * 部门id
     */
    private Long organizationId;
    /**
     * 部门ids
     */
    private Set<Long> organizationIds;
    /**
     * 用户id
     */
    private Set<Long> userIds;
    /**
     * 关键子
     */
    private String keyword;
    /**
     * 租户
     */
    private Integer tenant;
}
