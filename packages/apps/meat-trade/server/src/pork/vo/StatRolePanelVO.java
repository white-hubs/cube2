package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.StatRolePanelEnum;
import lombok.Data;

import java.util.Collection;

/**
 * @author lzy
 * @date 2021/11/22
 */
@Data
public class StatRolePanelVO {

    /**
     * 角色主键
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 看板类型集合
     */
    private Collection<StatRolePanelEnum> panelTypes;

}
