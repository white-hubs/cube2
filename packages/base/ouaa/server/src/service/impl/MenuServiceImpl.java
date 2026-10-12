package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.biz.ouaa.server.dao.MenuMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.param.MenuModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.MenuQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.MenuUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.service.MenuService;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTreeEntity;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final FileRefClient fileRefClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull MenuModifyParam param) {
        Menu menu = ObjectUtils.copying(param, Menu.class);
        this.baseMapper.insert(menu);
        return menu.getId();
    }

    @Override
    public void update(@NonNull Long id, @NonNull MenuUpdateParam param) {
        Menu menu = this.getById(id);
        Assert.isNotFound(menu, "菜单不存在");
        ObjectUtils.copying(param, menu);
        menu.setSort(Objects.isNull(param.getSort()) ? 0 : param.getSort());
        this.baseMapper.updateById(menu);
    }

    @Override
    public void remove(@NonNull Long id) {
        Menu menu = this.baseMapper.selectById(id);
        if (menu != null) {
            DatabaseContextHolder.softDelete(this, id);
        }
    }

    @Override
    public Menu getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<Menu> listByIds(Collection<? extends Serializable> idList) {
        return idList.isEmpty() ? Collections.emptyList() :
               this.baseMapper.selectList(Wrappers.<Menu>lambdaQuery().in(Menu::getId, idList).orderByAsc(Menu::getSort));
    }

    @Override
    public Menu getByPath(@NonNull String path) {
        return StringUtils.isEmpty(path) ? null : this.baseMapper.selectOne(Wrappers.<Menu>lambdaQuery().eq(Menu::getPath, path));
    }

    @Override
    public List<Menu> getList(MenuQueryParam param) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(param.getTerminalId()), Menu::getTerminalId, param.getTerminalId())
                .eq(Objects.nonNull(param.getApplicationId()), Menu::getApplicationId, param.getApplicationId())
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(Menu::getCode, param.getKeyword())
                        .or()
                        .like(Menu::getName, param.getKeyword()))
                .orderByAsc(Menu::getSort)
                .list();
    }

    @Override
    public List<Menu> getSequence(@NonNull List<String> sequences) {
        return this.lambdaQuery()
                .in(BasicEnhancedTreeEntity::getSequence, sequences)
                .orderByAsc(Menu::getSort)
                .list();
    }

    @Override
    public List<Menu> listByTerminalApplication(Long terminalId, Long applicationId) {
        return baseMapper.selectList(Wrappers.<Menu>lambdaQuery().eq(Menu::getTerminalId, terminalId)
                                             .eq(Menu::getApplicationId, applicationId).orderByAsc(Menu::getSort));
    }

    @Override
    public List<Menu> listByTerminal(Long terminal) {
        return baseMapper.selectList(Wrappers.<Menu>lambdaQuery().eq(Menu::getTerminalId, terminal).orderByAsc(Menu::getSort));
    }

    @Override
    public ApplicationVO getRoleMenus(String path) {

        return baseMapper.getRoleMenus(String.valueOf(SecurityContextHolder.getPrincipal().getId()), path, SessionContextHolder
                .getRequester().getTenant());
    }

}
