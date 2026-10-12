package com.guhecloud.cube.biz.ouaa.server.controller.tenant;

import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.enumeration.DataPermissionEnum;
import com.guhecloud.cube.biz.ouaa.server.param.MenuQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.FunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.MenuService;
import com.guhecloud.cube.biz.ouaa.server.utils.CustomConfigUtil;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuFunctionVO;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.database.util.EntityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单管理接口
 *
 * @author Woody
 * @date 2021/7/19
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuUnderTenantController {
    private final FunctionService functionService;
    private final MenuService menuService;
    private final CustomConfigUtil customConfigUtil;
    private final ApplicationService applicationService;

    @GetMapping("/menu-under-tenant")
    public List<MenuVO> listMenus(MenuQueryParam param) {
        List<Menu> menus = menuService.getList(param);
        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }
        // 获取上级sequence
        List<String> sequences = EntityUtils.getAllSequences(menus);
        List<Menu> list = menuService.getSequence(sequences);
        Application application = applicationService.getById(param.getApplicationId());
        // 获取下级sequence
        List<MenuVO> menuVOList = ObjectUtils.copying(list, MenuVO.class);
        Map<Long, String> applicationMap = new HashMap<>();
        applicationMap.put(application.getId(), application.getCode());
        customConfigUtil.menuCustomConfigResolving(applicationMap, menuVOList, true);
        menuVOList = TreeUtils.assembleTrees(menuVOList);
        //查询菜单下的按钮
        List<MenuFunctionVO> allMenuFunction = functionService.getAllMenuFunction();
        Map<Long, List<MenuFunctionVO>> menuFunctionGroup = allMenuFunction.stream()
                .collect(Collectors.groupingBy(MenuFunctionVO::getMenuId));
        this.extendFunctionAndDataPermUnderMenu(menuVOList, menuFunctionGroup);

        return menuVOList;
    }

    private void extendFunctionAndDataPermUnderMenu(List<MenuVO> menuList, Map<Long, List<MenuFunctionVO>> menuFunctions) {
        for (MenuVO menu : menuList) {
            if (menu.getChildren() == null || menu.getChildren().isEmpty()) {
                menu.setFunctions(menuFunctions.get(menu.getId()));
                //设置数据权限标识
                menu.setDataPermissions(DataPermissionEnum.getList());
            } else {
                extendFunctionAndDataPermUnderMenu(menu.getChildren(), menuFunctions);
            }
        }
    }

}
