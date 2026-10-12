package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.StatRolePanel;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatRolePanelModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatRolePanelQueryParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2021/11/22
 */
public interface StatRolePanelService extends IService<StatRolePanel> {

    void save(StatRolePanelModifyParam param);

    void update(StatRolePanelModifyParam param);

    Page<StatRolePanel> listByPage(StatRolePanelQueryParam queryParam);

    List<StatRolePanel> listByRole(Long roleId);

    List<StatRolePanel> listByRoles(Collection<? extends Serializable> roleIds);
}
