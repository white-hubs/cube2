package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.StatRolePanelEnum;
import lombok.Data;

/**
 * @author lzy
 * @date 2021/11/22
 */
@Data
public class StatRolePanel {

    /**
     * 主键
     */
    private Long id;

    /**
     * 角色主键
     */
    private Long roleId;

    /**
     * 面板类型
     */
    private StatRolePanelEnum panelType;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
