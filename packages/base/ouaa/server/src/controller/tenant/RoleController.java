package com.guhecloud.cube.biz.ouaa.server.controller.tenant;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.dto.RoleDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Function;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.entity.MenuFunction;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleApplication;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenu;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenuDataPermission;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenuFunction;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleTerminal;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.enumeration.DataPermissionEnum;
import com.guhecloud.cube.biz.ouaa.server.param.PermissionModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.RoleAssignUsersParam;
import com.guhecloud.cube.biz.ouaa.server.param.RoleModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.RoleQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.EnterpriseExportService;
import com.guhecloud.cube.biz.ouaa.server.service.FunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.MenuFunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.MenuService;
import com.guhecloud.cube.biz.ouaa.server.service.ProductService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuDataPermissionService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuFunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleTerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.DataPermissionVO;
import com.guhecloud.cube.biz.ouaa.server.vo.FunctionVO;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuFunctionVO;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuVO;
import com.guhecloud.cube.biz.ouaa.server.vo.RoleVO;
import com.guhecloud.cube.biz.ouaa.server.vo.TerminalPageVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色管理接口
 *
 * @author Woody
 * @date 2021/7/17
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController {

    private final RoleService roleService;
    private final ProductService productService;
    private final ApplicationService applicationService;
    private final TerminalService terminalService;
    private final MenuService menuService;
    private final RoleTerminalService roleTerminalService;
    private final RoleApplicationService roleApplicationService;
    private final RoleMenuService roleMenuService;
    private final RoleMenuFunctionService roleMenuFunctionService;
    private final RoleMenuDataPermissionService roleMenuDataPermissionService;
    private final FunctionService functionService;
    private final MenuFunctionService menuFunctionService;
    private final UserRoleService userRoleService;
    private final EnterpriseExportService enterpriseExportService;

    /**
     * 新增角色
     *
     * @param param 新增参数
     */
    @PostMapping("/role")
    public void addRole(@RequestBody @Valid RoleModifyParam param) {
        this.roleService.addRole(param);
    }

    /**
     * 修改角色
     *
     * @param id    角色主键
     * @param param 修改参数
     */
    @PutMapping("/role/{id}")
    public void updateRole(@PathVariable("id") Long id, @RequestBody @Valid RoleModifyParam param) {
        this.roleService.updateRole(id, param);
    }

    /**
     * 删除角色
     *
     * @param id 角色主键
     */
    @DeleteMapping("/role/{id}")
    public void deleteRole(@PathVariable("id") Long id) {
        this.roleService.deleteRole(id);
    }

    /**
     * 查询角色
     *
     * @param param 查询参数
     *
     * @return 角色列表分页
     */
    @GetMapping("/roles")
    public Pagination<RoleVO> listRoles(@Valid RoleQueryParam param) {
        Page<Role> page = this.roleService.listRoles(param);
        List<RoleVO> roles = ObjectUtils.copying(page.getRecords(), RoleVO.class);

        // 关联产品信息
        if (!CollectionUtils.isEmpty(roles)) {
            Set<Long> productIds = page.getRecords().stream().map(Role::getProductId)
                    .filter(Objects::nonNull).collect(Collectors.toSet());
            Map<Long, Product> products = this.productService.listByIds(productIds).stream()
                    .collect(Collectors.toMap(Product::getId, obj -> obj, (o, n) -> n));
            Map<Long, Product> roleProducts = page.getRecords().stream()
                    .filter(role -> products.containsKey(role.getProductId()))
                    .collect(Collectors.toMap(Role::getId, role -> products.get(role.getProductId()), (o, n) -> n));
            roles.forEach(role -> {
                Product product = roleProducts.get(role.getId());
                role.setProduct(ObjectUtils.copying(product, RoleVO.Product.class));
            });
        }
        return PagingUtils.convert(page, roles);
    }

    /**
     * 获取角色信息
     *
     * @return 角色列表
     */
    @GetMapping("/role/list-by-product")
    public List<RoleDTO> listRoles() {
        // 获取访问端主键
        Terminal terminal = this.terminalService.getByCode(SessionContextHolder.getRequester().getTerminal());
        Assert.isNotFound(terminal, "不存在访问端信息!");
        List<Role> roles = this.roleService.listProductRoles();
        return ObjectUtils.copying(roles, RoleDTO.class);
    }

    @GetMapping("/role/{id}/terminals")
    public List<TerminalPageVO> listRoleTerminals(@PathVariable("id") Long roleId) {
        List<RoleTerminal> roleTerminals = this.roleTerminalService.listByRole(roleId);
        List<Long> terminalIds = roleTerminals.stream().map(item -> item.getTerminalId()).collect(Collectors.toList());
        List<Terminal> terminals = this.terminalService.listByIds(terminalIds);
        if (CollectionUtils.isEmpty(terminals)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(terminals, TerminalPageVO.class);
    }

    @PostMapping("/role/{id}/terminal-mapping")
    public void saveRoleTerminalMapping(@PathVariable("id") Long roleId, @RequestParam("terminalId") Long terminalId) {
        this.roleTerminalService.save(roleId, terminalId);
    }

    @DeleteMapping("/role/{id}/terminal-mapping")
    public void removeRoleTerminalMapping(@PathVariable("id") Long roleId, @RequestParam("terminalId") Long terminalId) {
        this.roleTerminalService.remove(roleId, terminalId);
    }

    @GetMapping("/role/{id}/terminal-applications")
    public List<ApplicationPageVO> listRoleTerminalApplications(@PathVariable("id") Long roleId,
                                                                @RequestParam("terminalId") Long terminalId
                                                               ) {
        List<RoleApplication> roleTerminalApplications = this.roleApplicationService.listByRoleTerminal(roleId, terminalId);
        List<Long> applicationIds = roleTerminalApplications.stream().map(item -> item.getApplicationId()).collect(Collectors.toList());
        List<Application> applications = this.applicationService.listByIds(applicationIds);
        if (CollectionUtils.isEmpty(applications)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(applications, ApplicationPageVO.class);
    }

    @PostMapping("/role/{id}/terminal-application-mapping")
    public void saveRoleTerminalApplicationMapping(@PathVariable("id") Long roleId, @RequestParam("terminalId") Long terminalId,
                                                   @RequestParam("applicationId") Long applicationId
                                                  ) {
        this.roleApplicationService.save(roleId, terminalId, applicationId);
    }

    @DeleteMapping("/role/{id}/terminal-application-mapping")
    public void removeRoleTerminalApplicationMapping(@PathVariable("id") Long roleId, @RequestParam("terminalId") Long terminalId,
                                                     @RequestParam("applicationId") Long applicationId
                                                    ) {
        this.roleApplicationService.remove(roleId, terminalId, applicationId);
    }

    @GetMapping("/role/{id}/terminal-application-menus")
    public List<MenuVO> listRoleTerminalApplicationMenus(@PathVariable("id") Long roleId, @RequestParam("terminalId") Long terminalId,
                                                         @RequestParam("applicationId") Long applicationId
                                                        ) {
        List<RoleMenu> roleTerminalApplicationMenus = this.roleMenuService.listByRoleTerminalApplication(roleId, terminalId, applicationId);
        List<Long> menuIds = roleTerminalApplicationMenus.stream().map(item -> item.getMenuId()).collect(Collectors.toList());
        List<Menu> menus = this.menuService.listByIds(menuIds);
        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }
        List<MenuVO> menuVOS = ObjectUtils.copying(menus, MenuVO.class);
        //查询菜单配置的功能权限
        List<RoleMenuFunction> menuFunctions = roleMenuFunctionService.list(Wrappers.<RoleMenuFunction>lambdaQuery()
                                                                                    .eq(RoleMenuFunction::getRoleId, roleId)
                                                                                    .eq(RoleMenuFunction::getTerminalId, terminalId)
                                                                                    .eq(RoleMenuFunction::getApplicationId, applicationId));
        Map<Long, List<MenuFunctionVO>> selectedFunctions = menuFunctions.stream()
                .map(mf -> new MenuFunctionVO(mf.getMenuId(), mf.getFunctionId()))
                .collect(Collectors.groupingBy(MenuFunctionVO::getMenuId));

        //查询菜单配置的数据权限
        List<RoleMenuDataPermission> menuDataPermissions = roleMenuDataPermissionService.list(Wrappers.<RoleMenuDataPermission>lambdaQuery()
                                                                                                      .eq(RoleMenuDataPermission::getRoleId, roleId)
                                                                                                      .eq(RoleMenuDataPermission::getTerminalId, terminalId)
                                                                                                      .eq(RoleMenuDataPermission::getApplicationId, applicationId));
        Map<Long, List<DataPermissionVO>> selectedDataPerms = menuDataPermissions.stream()
                .map(dp -> new DataPermissionVO(dp.getMenuId(), dp.getPermission(), dp.getBusinessKeys()))
                .collect(Collectors.groupingBy(DataPermissionVO::getMenuId));

        //组装数据
        for (MenuVO menu : menuVOS) {
            menu.setFunctions(selectedFunctions.get(menu.getId()));
            menu.setDataPermissions(selectedDataPerms.get(menu.getId()));
        }
        return menuVOS;
    }

    @PostMapping("/role/{id}/terminal-application-menu-mapping")
    public void saveRoleTerminalApplicationMenuMapping(@PathVariable("id") Long roleId, @RequestParam("terminalId") Long terminalId,
                                                       @RequestParam("applicationId") Long applicationId,
                                                       @RequestParam("menuId") Long menuId
                                                      ) {
        this.roleMenuService.save(roleId, terminalId, applicationId, menuId);
    }

    /**
     * 保存菜单与功能的关系
     *
     * @param roleId
     * @param terminalId
     * @param applicationId
     * @param menuId
     * @param functionId
     */
    @PostMapping("/role/{id}/terminal-application-menu-function-mapping")
    public void saveRoleTerminalApplicationMenuFunctionMapping(@PathVariable("id") Long roleId,
                                                               @RequestParam("terminalId") Long terminalId,
                                                               @RequestParam("applicationId") Long applicationId,
                                                               @RequestParam("menuId") Long menuId,
                                                               @RequestParam("functionId") Long functionId
                                                              ) {
        this.roleMenuFunctionService.save(roleId, terminalId, applicationId, menuId, functionId);
    }

    @DeleteMapping("/role/{id}/terminal-application-menu-function-mapping")
    public void removeRoleRoleMenuFunctionMapping(@PathVariable("id") Long roleId,
                                                  @RequestParam("terminalId") Long terminalId,
                                                  @RequestParam("applicationId") Long applicationId,
                                                  @RequestParam("menuId") Long menuId,
                                                  @RequestParam("functionId") Long functionId
                                                 ) {
        this.roleMenuFunctionService.remove(roleId, terminalId, applicationId, menuId, functionId);
    }

    /**
     * 保存菜单与数据权限的关系
     *
     * @param roleId
     * @param terminalId
     * @param applicationId
     * @param menuId
     * @param permissionCode
     */
    @PostMapping("/role/{id}/terminal-application-menu-data-permission-mapping")
    public void saveRoleTerminalApplicationMenuDataPermissionMapping(@PathVariable("id") Long roleId,
                                                                     @RequestParam("terminalId") Long terminalId,
                                                                     @RequestParam("applicationId") Long applicationId,
                                                                     @RequestParam("menuId") Long menuId,
                                                                     @RequestParam("permissionCode") String permissionCode,
                                                                     @RequestParam(value = "businessKeys", required = false)
                                                                     String businessKeys
                                                                    ) {
        this.roleMenuDataPermissionService.save(roleId, terminalId, applicationId, menuId, permissionCode, businessKeys);
    }

    @DeleteMapping("/role/{id}/terminal-application-menu-data-permission-mapping")
    public void removeRoleTerminalApplicationMenuDataPermissionMapping(@PathVariable("id") Long roleId,
                                                                       @RequestParam("terminalId") Long terminalId,
                                                                       @RequestParam("applicationId") Long applicationId,
                                                                       @RequestParam("menuId") Long menuId,
                                                                       @RequestParam("permissionCode") String permissionCode
                                                                      ) {
        this.roleMenuDataPermissionService.remove(roleId, terminalId, applicationId, menuId, permissionCode);
    }

    @DeleteMapping("/role/{id}/terminal-application-menu-mapping")
    public void removeRoleTerminalApplicationMenuMapping(@PathVariable("id") Long roleId, @RequestParam("terminalId") Long terminalId,
                                                         @RequestParam("applicationId") Long applicationId,
                                                         @RequestParam("menuId") Long menuId
                                                        ) {
        this.roleMenuService.remove(roleId, terminalId, applicationId, menuId);
    }

    @PostMapping("/role/{id}/all-terminal-application-menu-mappings")
    public void saveRoleAllTerminalApplicationMenuMappings(@PathVariable("id") Long roleId, @RequestParam("terminalId") Long terminalId,
                                                           @RequestParam("applicationId") Long applicationId
                                                          ) {
        List<Menu> terminalApplicationMenus = this.menuService.listByTerminalApplication(terminalId, applicationId);
        for (Menu menu : terminalApplicationMenus) {
            this.roleMenuService.save(roleId, terminalId, applicationId, menu.getId());
        }
    }

    @DeleteMapping("/role/{id}/all-terminal-application-menu-mappings")
    public void removeRoleAllTerminalApplicationMenuMappings(@PathVariable("id") Long roleId, @RequestParam("terminalId") Long terminalId,
                                                             @RequestParam("applicationId") Long applicationId
                                                            ) {
        List<Menu> terminalApplicationMenus = this.menuService.listByTerminalApplication(terminalId, applicationId);
        for (Menu menu : terminalApplicationMenus) {
            this.roleMenuService.remove(roleId, terminalId, applicationId, menu.getId());
        }
    }

    @GetMapping("/role/{id}/menu-functions")
    public List<FunctionVO> listRoleMenuFunctions(@PathVariable("id") Long roleId, @RequestParam("menuId") Long menuId) {
        List<RoleMenuFunction> roleMenuFunctions = this.roleMenuFunctionService.listRoleMenuFunctions(roleId, menuId);
        Set<Long> functionIds = roleMenuFunctions.stream().map(roleMenuFunction -> roleMenuFunction.getFunctionId())
                .collect(Collectors.toSet());
        List<Function> functions = this.functionService.listFunctions(functionIds);
        List<FunctionVO> functionVOS = ObjectUtils.copying(functions, FunctionVO.class);
        functionVOS.stream().forEach(functionVO -> {
            functionVO.setMenuId(menuId);
        });
        return functionVOS;
    }

    /**
     * 修改产品角色权限
     *
     * @param id    产品主键
     * @param param 权限更新参数
     */
    @PutMapping("/role/{id}/permission")
    public void updateProductPermission(@PathVariable("id") Long id, @RequestBody @Valid PermissionModifyParam param) {
    }

    /**
     * 初始化角色的菜单功能权限和数据权限
     */
    @PostMapping("/role/init-function-and-data-permission")
    public void initFunctionAndDataPermissionForRole() {
        //查询当前租户下的所有角色
        List<Role> allRoles = this.roleService.list();
        if (!CollectionUtils.isEmpty(allRoles)) {
            List<Long> roleIds = allRoles.stream().map(role -> role.getId()).collect(Collectors.toList());
            //查询当前所有角色的菜单权限
            List<RoleMenu> roleMenus = this.roleMenuService.list(Wrappers.<RoleMenu>lambdaQuery()
                                                                         .in(RoleMenu::getRoleId, roleIds));
            roleMenus.stream().forEach(roleMenu -> {
                //查询功能权限
                List<RoleMenuFunction> menuFunctions = roleMenuFunctionService.list(Wrappers.<RoleMenuFunction>lambdaQuery()
                                                                                            .eq(RoleMenuFunction::getRoleId, roleMenu.getRoleId())
                                                                                            .eq(RoleMenuFunction::getTerminalId, roleMenu.getTerminalId())
                                                                                            .eq(RoleMenuFunction::getApplicationId, roleMenu.getApplicationId())
                                                                                            .eq(RoleMenuFunction::getMenuId, roleMenu.getMenuId()));
                if (CollectionUtils.isEmpty(menuFunctions)) {//如果不为空，说明已经配置过了，就不处理
                    //查询菜单下的按钮
                    List<MenuFunction> functions = this.menuFunctionService.list(Wrappers.<MenuFunction>lambdaQuery()
                                                                                         .eq(MenuFunction::getMenuId, roleMenu.getMenuId()));
                    //保存按钮下的功能权限
                    if (!CollectionUtils.isEmpty(functions)) {
                        functions.stream().forEach(function -> {
                            this.roleMenuFunctionService.save(roleMenu.getRoleId(), roleMenu.getTerminalId(), roleMenu.getApplicationId(), roleMenu.getMenuId(), function.getFunctionId());
                        });
                    }
                }
                //查询数据权限
                List<RoleMenuDataPermission> dataPermissions = roleMenuDataPermissionService.list(Wrappers
                                                                                                          .<RoleMenuDataPermission>lambdaQuery()
                                                                                                          .eq(RoleMenuDataPermission::getRoleId, roleMenu.getRoleId())
                                                                                                          .eq(RoleMenuDataPermission::getTerminalId, roleMenu.getTerminalId())
                                                                                                          .eq(RoleMenuDataPermission::getApplicationId, roleMenu.getApplicationId())
                                                                                                          .eq(RoleMenuDataPermission::getMenuId, roleMenu.getMenuId()));
                if (CollectionUtils.isEmpty(dataPermissions)) {
                    //默认查看所有数据
                    this.roleMenuDataPermissionService.save(roleMenu.getRoleId(), roleMenu.getTerminalId(), roleMenu.getApplicationId(), roleMenu.getMenuId(), DataPermissionEnum.ALL);
                }
            });

        }

    }

    /**
     * 根据ids获取角色信息
     *
     * @param ids
     *
     * @return
     */
    @GetMapping("/authenticated/role/list/by-ids")
    public List<RoleDTO> listRolesByIds(@RequestParam(value = "ids") List<Long> ids) {
        List<Role> list = this.roleService.list(Wrappers.<Role>lambdaQuery()
                                                        .in(Role::getId, ids));
        return ObjectUtils.copying(list, RoleDTO.class);
    }

    /**
     * 批量人员分配角色
     * 注意：如果人员id集合为空会清空角色下分配的人员
     *
     * @param id    角色id
     * @param param param.userIdList 人员id集合
     */
    @PutMapping("/role/{id}/assign/users")
    public void usersRoleEdit(@PathVariable("id") Long id, @RequestBody RoleAssignUsersParam param) {
        Assert.isParameterError(id == null, "请选择角色！");
        userRoleService.roleAssignUsers(id, param.getUserIdList());
    }

    /**
     * 获取角色下的用户id
     *
     * @param id 角色id
     *
     * @return 用户id集合
     */
    @GetMapping("/role/{id}/userid/list")
    public List<Long> listRoleUserId(@PathVariable("id") Long id) {
        Assert.isParameterError(id == null, "请选择角色！");
        Set<Long> userSet = userRoleService.listUserIdsByRoleIds(Collections.singleton(id));
        if (userSet == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(userSet);
    }

    /**
     * 导出角色列表
     */
    @GetMapping("/role/export")
    public void export(@SpringQueryMap @Valid RoleQueryParam param) {
        enterpriseExportService.exportRoles(param);
    }
}
