package com.guhecloud.cube.biz.vmonitor.server.param.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 角色
 *
 * @author lanchun
 * @date 2023/3/22 10:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleAuthParam {

    /**
     * 角色主键
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;
}
