package com.guhecloud.cube.biz.ouaa.server.converters;

import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class MenuConverterForMenuVO {

    private MenuConverterForMenuVO() {}

    public static MenuVO toMenuVO(Menu menu) {
        var menuVO = new MenuVO();
        menuVO.setId(menu.getId());
        menuVO.setCode(menu.getCode());
        menuVO.setName(menu.getName());
        menuVO.setParentId(menu.getParentId());
        menuVO.setLevel(menu.getLevel());
        menuVO.setTerminalId(menu.getTerminalId());
        menuVO.setApplicationId(menu.getApplicationId());
        menuVO.setCategoryId(menu.getCategoryId());
        menuVO.setIcon(menu.getIcon());
        menuVO.setPath(menu.getPath());
        menuVO.setSort(menu.getSort());
        menuVO.setRemark(menu.getRemark());
        menuVO.setEnabled(menu.getEnabled());
        return menuVO;
    }

    public static List<MenuVO> toMenuVOS(List<Menu> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return CollectionUtils.emptyList();
        }
        return menus.stream().map(MenuConverterForMenuVO::toMenuVO).toList();
    }
}
