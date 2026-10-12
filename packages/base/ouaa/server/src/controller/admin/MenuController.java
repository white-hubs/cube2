package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.guhecloud.cube.biz.ouaa.server.converters.MenuConverterForMenuVO;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.param.MenuModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.MenuQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.MenuUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.service.MenuService;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.database.util.EntityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 菜单管理接口
 *
 * @author Woody
 * @date 2021/7/19
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAdmin()")
public class MenuController {

    private final MenuService menuService;

    /**
     * 菜单新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/menu")
    public Long save(@Valid @RequestBody MenuModifyParam param) {
        return menuService.save(param);
    }

    /**
     * 菜单修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/menu/{id}")
    public void update(@Min(value = 1, message = "菜单主键不能小于1") @PathVariable("id") Long id,
                       @Valid @RequestBody MenuUpdateParam param
                      ) {
        menuService.update(id, param);
    }

    /**
     * 菜单详情
     *
     * @param id 菜单主键
     *
     * @return
     */
    @GetMapping("/menu/{id}")
    public MenuVO getMenu(@Min(value = 1, message = "菜单主键不能小于1") @PathVariable("id") Long id) {
        Menu menu = menuService.getById(id);
        Assert.isNotFound(menu, "菜单不存在");
        return ObjectUtils.copying(menu, MenuVO.class);
    }

    /**
     * 通过路径查询菜单详情
     *
     * @param path
     *
     * @return
     */
    @GetMapping("/menu/path/{path}")
    public MenuVO getMenuByPath(@PathVariable("path") String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        Menu menu = menuService.getByPath(path);
        Assert.isNotFound(menu, "菜单不存在");
        return ObjectUtils.copying(menu, MenuVO.class);
    }

    /**
     * 菜单删除
     *
     * @param id
     */
    @DeleteMapping("/menu/{id}")
    public void remove(@Min(value = 1, message = "菜单主键不能小于1") @PathVariable("id") Long id) {
        menuService.remove(id);
    }

    /**
     * 菜单列表
     *
     * @param param 查询参数
     *
     * @return
     */
    @GetMapping("/menus")
    public List<MenuVO> listMenus(MenuQueryParam param) {
        var menus = menuService.getList(param);
        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }
        // 获取上级sequence
        var sequences = EntityUtils.getAllSequences(menus);
        // 获取下级sequence
        var list = menuService.getSequence(sequences);
        var menuVOS = MenuConverterForMenuVO.toMenuVOS(list);
        return TreeUtils.assembleTrees(menuVOS);
    }

}
