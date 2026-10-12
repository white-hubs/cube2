package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.MenuFunction;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuFunctionVO;

import java.util.List;

/**
 * 菜单功能关联业务操作接口
 *
 * @author Woody
 * @date 2021/7/1
 */
public interface MenuFunctionService extends IService<MenuFunction> {

    Long save(Long menuId, Long functionId);

    List<MenuFunction> listMenuFunctions(Long menuId);

    void remove(Long functionId);

    List<MenuFunctionVO> getFunctionUnderMenu(Long menuId);
}
