package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.param.MenuModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.MenuQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.MenuUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface MenuService extends IService<Menu> {

    Long save(MenuModifyParam param);

    void update(Long id, MenuUpdateParam param);

    void remove(Long id);

    Menu getById(Serializable id);

    List<Menu> listByIds(Collection<? extends Serializable> idList);

    Menu getByPath(String path);

    List<Menu> getList(MenuQueryParam param);

    List<Menu> getSequence(List<String> sequences);

    List<Menu> listByTerminalApplication(Long terminalId, Long applicationId);

    List<Menu> listByTerminal(Long terminal);

    ApplicationVO getRoleMenus(String path);

}
