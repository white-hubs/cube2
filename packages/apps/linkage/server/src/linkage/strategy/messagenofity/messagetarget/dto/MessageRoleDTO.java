package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 角色
 *
 * @author lanchun
 * @date 2022/10/31 16:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageRoleDTO {

    /**
     * 角色主键
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;
}
