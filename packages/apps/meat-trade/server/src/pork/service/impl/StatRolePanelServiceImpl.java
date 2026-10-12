package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.StatRolePanelMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.StatRolePanel;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatRolePanelModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatRolePanelQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.StatRolePanelService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @date 2021/11/22
 */
@Slf4j
@Service("meattrade-statRolePanelService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatRolePanelServiceImpl extends ServiceImpl<StatRolePanelMapper, StatRolePanel> implements StatRolePanelService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull StatRolePanelModifyParam param) {
        // 判断该角色是否设置权限
        List<StatRolePanel> panels = this.lambdaQuery().in(StatRolePanel::getRoleId, param.getRoleId()).list();
        Assert.isConflict(CollectionUtils.isNotEmpty(panels), "该角色已设置权限!");

        List<StatRolePanel> statRolePanels = param.getPanelTypes().stream().map(panelType -> {
            StatRolePanel statRolePanel = new StatRolePanel();
            statRolePanel.setRoleId(param.getRoleId());
            statRolePanel.setPanelType(panelType);
            return statRolePanel;
        }).collect(Collectors.toList());
        this.saveBatch(statRolePanels);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull StatRolePanelModifyParam param) {
        // 删除原有角色权限
        this.baseMapper.delete(Wrappers.<StatRolePanel>lambdaQuery().eq(StatRolePanel::getRoleId, param.getRoleId()));
        // 重新新增
        List<StatRolePanel> statRolePanels = param.getPanelTypes().stream().map(panelType -> {
            StatRolePanel statRolePanel = new StatRolePanel();
            statRolePanel.setEnabled(param.getEnabled());
            statRolePanel.setRoleId(param.getRoleId());
            statRolePanel.setPanelType(panelType);
            return statRolePanel;
        }).collect(Collectors.toList());
        this.saveBatch(statRolePanels);
    }

    @Override
    public Page<StatRolePanel> listByPage(@NonNull StatRolePanelQueryParam queryParam) {
        return this.lambdaQuery()
                .in(!CollectionUtils.isEmpty(queryParam.getRoleIds()), StatRolePanel::getRoleId, queryParam.getRoleIds())
                .groupBy(StatRolePanel::getRoleId).page(PagingUtils.convert(queryParam));
    }

    @Override
    public List<StatRolePanel> listByRole(@NonNull Long roleId) {
        return this.baseMapper.selectList(Wrappers.<StatRolePanel>lambdaQuery().eq(StatRolePanel::getRoleId, roleId));
    }

    @Override
    public List<StatRolePanel> listByRoles(Collection<? extends Serializable> roleIds) {
        return this.baseMapper.selectList(Wrappers.<StatRolePanel>lambdaQuery()
                                                  .in(!CollectionUtils.isEmpty(roleIds), StatRolePanel::getRoleId, roleIds)
                                                  .eq(StatRolePanel::getEnabled, true));
    }
}
