package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class DisinfectionUserPageParam extends Paging {

    /**
     * 名称
     */
    private String keyword;

    /**
     * 部门ID
     */
    private Long organizationId;

    /**
     * 岗位ID
     */
    private Long positionId;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 消杀小组ID
     */
    private Long disinfectionGroupId;

    /**
     * 用户id集合（后端使用）
     */
    private Set<Long> userIds;

}
