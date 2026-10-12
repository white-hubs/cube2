package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import java.util.List;

/**
 * 角色批量分配用户入参对象
 */
@Data
public class RoleAssignUsersParam {
    /**
     * 用户id集合
     */
    private List<Long> userIdList;
}
