package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.MenuFunctionMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.MenuFunction;
import com.guhecloud.cube.biz.ouaa.server.service.MenuFunctionService;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuFunctionVO;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单功能关联业务操作实现
 *
 * @author Woody
 * @date 2021/7/1
 */
@Service
public class MenuFunctionServiceImpl extends ServiceImpl<MenuFunctionMapper, MenuFunction> implements MenuFunctionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull Long menuId, @NonNull Long functionId) {
        MenuFunction menuFunction = new MenuFunction();
        menuFunction.setMenuId(menuId);
        menuFunction.setFunctionId(functionId);
        this.baseMapper.insert(menuFunction);
        return menuFunction.getId();
    }

    @Override
    public List<MenuFunction> listMenuFunctions(@NonNull Long menuId) {
        return this.lambdaQuery().eq(MenuFunction::getMenuId, menuId).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long functionId) {
        this.baseMapper.delete(Wrappers.<MenuFunction>lambdaQuery().eq(MenuFunction::getFunctionId, functionId));
    }

    @Override
    public List<MenuFunctionVO> getFunctionUnderMenu(@NonNull Long menuId) {
        return this.baseMapper.getFunctionUnderMenu(menuId);
    }
}
