package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.PermissionTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.converters.OrganizationConverterForOrgVO;
import com.guhecloud.cube.biz.ouaa.server.dto.AppointmentDetailVO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserOrganizationDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.excel.AppointmentExcelDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.excel.OrgExcelDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.excel.PositionExcelDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.excel.RoleExcelDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.excel.UserExcelDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Appointment;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenu;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserManagedOrganization;
import com.guhecloud.cube.biz.ouaa.server.entity.UserOrganization;
import com.guhecloud.cube.biz.ouaa.server.entity.UserRole;
import com.guhecloud.cube.biz.ouaa.server.param.AppointmentQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.OrganizationQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.PosistionQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.RoleQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.PermissionPO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.SubjectPermissionPO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.PermissionService;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.SubjectPermissionService;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.AppointmentCommandService;
import com.guhecloud.cube.biz.ouaa.server.service.AppointmentQueryService;
import com.guhecloud.cube.biz.ouaa.server.service.EnterpriseExportService;
import com.guhecloud.cube.biz.ouaa.server.service.MenuService;
import com.guhecloud.cube.biz.ouaa.server.service.OrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.PositionService;
import com.guhecloud.cube.biz.ouaa.server.service.ProductService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.UserManagedOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.UserOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.vo.OrganizationVO;
import com.guhecloud.cube.biz.ouaa.server.vo.RoleVO;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.SetUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * author: ht
 * create: 2025-06-09 16:32
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EnterpriseExportServiceImpl implements EnterpriseExportService {
    private final UserOrganizationService userOrganizationService;
    private final PositionService positionService;
    private final UserRoleService userRoleService;
    private final OrganizationService organizationService;
    private final AppointmentCommandService appointmentCommandService;
    private final AppointmentQueryService appointmentQueryService;
    private final UserManagedOrganizationService userManagedOrganizationService;
    private final RoleService roleService;
    private final ProductService productService;
    private final ExcelUtils excelUtils;
    private final RoleMenuService roleMenuService;
    private final TerminalService terminalService;
    private final ApplicationService applicationService;
    private final MenuService menuService;
    private final PermissionService permissionService;
    private final UserService userService;
    private final SubjectPermissionService subjectPermissionService;
    private final static int MAX_EXPORT_NUM = 10000;

    @Override
    public void exportStaff(@NonNull UserQueryParam param) {
        String fileName = "员工列表";
        rebuildPageParam(param);
        // 获取用户列表
        Page<UserVO> page = userOrganizationService.listOrganizationUsers(param);
        List<UserVO> users = ObjectUtils.convertOrElse(page, Page::getRecords, Collections.emptyList());
        ListUtils.runOrElse(
                users
                //非空导出员工数据
                , userVOList -> this.exportUserListExcel(userVOList, fileName)
                //如果员工列表为空则导出空列表
                , () -> this.exportEmptyExcel(fileName, UserExcelDTO.class)
                           );
    }

    @Override
    public void exportOrganizations(@NonNull OrganizationQueryParam param) {
        // 获取组织机构列表
        var organizations = this.organizationService.listIntegralOrganizations(param);
        organizations = ListUtils.orElse(organizations, Collections::emptyList);
        // 统计组织机构人数
        var organizationMembers = this.userOrganizationService.listOrganizationUsers(
                        organizations.stream().map(Organization::getId).collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(userOrganization ->
                                                                                                 userOrganization.getOrganizationId() +
                                                                                                 "-" +
                                                                                                 userOrganization.getUserId()))), ArrayList::new
                                                     )).stream()
                .collect(Collectors.groupingBy(UserOrganization::getOrganizationId, Collectors.counting()));
        var trees = TreeUtils.assembleTrees(OrganizationConverterForOrgVO.toOrganizationVOS(organizations));
        this.summaryOrganizationMembers(trees, organizationMembers);
        //转换为excelDTO传输对象
        var orgExcelDTOList = convertToOrgExcelDTO(trees);

        excelUtils.downloadExcel("部门列表", orgExcelDTOList, OrgExcelDTO.class);
    }

    @Override
    public void exportPositions(@NonNull PosistionQueryParam param) {
        String filename = "职位列表";

        //构建查询参数
        rebuildPageParam(param);
        //查询职位
        Page<Position> positionPage = positionService.listPosistions(param);
        List<Position> records = positionPage.getRecords();
        ListUtils.runOrElse(
                records
                , list -> exportPositionData(filename, list)
                , () -> exportEmptyExcel(filename, PositionExcelDTO.class)
                           );

    }

    @Override
    public void exportAppointment(@NonNull AppointmentQueryParam param) {
        String filename = "岗位列表";

        //构建查询参数
        rebuildPageParam(param);
        //查询职位
        Pagination<AppointmentDetailVO> page = appointmentQueryService.getPage(param);
        List<AppointmentDetailVO> records = page.getRecords();
        ListUtils.runOrElse(
                records
                , list -> exportAppointmentData(filename, list)
                , () -> exportEmptyExcel(filename, AppointmentExcelDTO.class)
                           );
    }

    @Override
    public void exportRoles(@NonNull RoleQueryParam param) {

        //重新构建分页参数
        rebuildPageParam(param);
        //获取角色分页数据
        Pagination<RoleVO> rolesPage = getRolesPage(param);
        List<RoleVO> records = rolesPage.getRecords();
        //转换vo到excelDTO
        List<RoleExcelDTO> roleExcelDTOList = ObjectUtils.copying(records, RoleExcelDTO.class);
        //设置角色产品信息
        buildRoleProduct(roleExcelDTOList, records);
        //设置角色菜单权限
        buildRoleMenus(roleExcelDTOList);
        //设置角色数据权限
        buildRoleDataPermissions(roleExcelDTOList);
        //设置角色关联的人员
        buildRoleRelationUsers(roleExcelDTOList);
        //设置角色功能权限
        buildRoleFunctionPermissions(roleExcelDTOList);
        //设置角色状态
        buildRoleEnable(roleExcelDTOList);
        excelUtils.downloadExcel("角色列表", roleExcelDTOList, RoleExcelDTO.class);
    }

    private void buildRoleEnable(@NonNull List<RoleExcelDTO> roleExcelDTOList) {
        roleExcelDTOList.forEach(roleExcelDTO -> {
            roleExcelDTO.setEnableText(roleExcelDTO.getEnabled() ? "启用" : "禁用");
        });
    }

    private void buildRoleProduct(@NonNull List<RoleExcelDTO> roleExcelDTOList, @NonNull List<RoleVO> records) {
        if (ListUtils.isEmpty(roleExcelDTOList)) {
            return;
        }
        Map<Long, RoleVO.Product> productMap = MapUtils.listToMap(records.stream().filter(roleVO -> Objects.nonNull(roleVO.getProduct()))
                                                                          .collect(Collectors.toList()), RoleVO::getId, RoleVO::getProduct);
        roleExcelDTOList.forEach(roleExcelDTO -> {
            RoleVO.Product product = productMap.get(roleExcelDTO.getId());
            if (Objects.nonNull(product)) {
                roleExcelDTO.setProduct(product.getName());
            }
        });
    }

    private void buildRoleFunctionPermissions(@NonNull List<RoleExcelDTO> roleExcelDTOList) {
        //查询类型是功能的功能权限的权限列表
        List<PermissionPO> permissionPOList = permissionService.lambdaQuery().eq(PermissionPO::getType, PermissionTypeEnum.FUNCATION)
                .list();
        //查询角色绑定的功能权限
        List<SubjectPermissionPO> subjectPermissionPOList = subjectPermissionService.getBindRolesPermission(ListUtils.convertItem(roleExcelDTOList, RoleExcelDTO::getId), ListUtils.convertItem(permissionPOList, PermissionPO::getCode));
        Map<Long, List<SubjectPermissionPO>> functionGroupMap = MapUtils.listGroupToMap(subjectPermissionPOList, po -> Long.valueOf(po.getSubjectId()));
        Map<String, List<PermissionPO>> permissionMap = MapUtils.listGroupToMap(permissionPOList, PermissionPO::getCode);
        //设置功能权限
        roleExcelDTOList.forEach(roleExcelDTO -> {
            List<SubjectPermissionPO> functionList = functionGroupMap.get(roleExcelDTO.getId());
            List<String> names = ListUtils.convertItem(functionList, subjectPermissionPO -> {
                String code = subjectPermissionPO.getPermissionCode();
                List<PermissionPO> permissionPOS = permissionMap.get(code);
                //分组而来，所以一定不是空list
                PermissionPO permissionPO = permissionPOS.get(0);
                if (Objects.nonNull(permissionPO)) {
                    return permissionPO.getDescription();
                }
                return "";
            });

            roleExcelDTO.setRoleFunctionPermissions(String.join(",", names));
        });
    }

    private void buildRoleRelationUsers(@NonNull List<RoleExcelDTO> roleExcelDTOList) {
        //查询用户绑定的人员
        List<UserRole> bindUserIds = userRoleService.getBindUser(ListUtils.convertItem(roleExcelDTOList, RoleExcelDTO::getId));
        Map<Long, List<UserRole>> userRoleMap = MapUtils.listGroupToMap(bindUserIds, UserRole::getRoleId);
        //查询用户信息
        List<User> userList = userService.listByIds(ListUtils.convertItem(bindUserIds, UserRole::getUserId));
        Map<Long, String> userNameMap = MapUtils.listToMap(userList, User::getId, User::getName);
        roleExcelDTOList.forEach(roleExcelDTO -> {
            List<UserRole> bindUserRoles = userRoleMap.getOrDefault(roleExcelDTO.getId(), Collections.emptyList());
            List<String> names = ListUtils.convertItem(bindUserRoles, bindUser -> {
                return userNameMap.getOrDefault(bindUser.getUserId(), "");
            });
            roleExcelDTO.setRoleRelationUsers(String.join(",", names));
        });
    }

    private void buildRoleDataPermissions(@NonNull List<RoleExcelDTO> roleExcelDTOList) {
        //查询类型是功能的功能权限的权限列表
        List<PermissionPO> permissionPOList = permissionService.lambdaQuery().eq(PermissionPO::getType, PermissionTypeEnum.DATA).list();
        Map<String, PermissionPO> permissionPOMap = MapUtils.listToMap(permissionPOList, PermissionPO::getCode, permissionPO -> permissionPO);
        //查询角色绑定的数据权限
        List<SubjectPermissionPO> roleDataPermissions = this.subjectPermissionService.getBindRolesPermission(ListUtils.convertItem(roleExcelDTOList, RoleExcelDTO::getId), ListUtils.convertItem(permissionPOList, PermissionPO::getCode));
        //根据不同角色分组
        Map<Long, List<SubjectPermissionPO>> roleMenusDataPermissionMap = MapUtils.listGroupToMap(roleDataPermissions, subjectPermissionPO -> Long.valueOf(subjectPermissionPO.getSubjectId()));
        roleExcelDTOList.forEach(roleExcelDTO -> {
            List<SubjectPermissionPO> dataPermissionList = roleMenusDataPermissionMap.getOrDefault(roleExcelDTO.getId(), Collections.emptyList());
            dataPermissionList = ListUtils.removeItem(dataPermissionList, subjectPermissionPO -> {
                String permissionExtentConfig = subjectPermissionPO.getPermissionExtentConfig();
                Map<String, String> permissionExtentConfigMap = JsonUtils.deserialize(permissionExtentConfig, Map.class);
                if (permissionExtentConfigMap == null) {
                    return false;
                }
                return StringUtils.isNotBlank(permissionExtentConfigMap.get("scope"));
            });
            List<String> names = ListUtils.convertItem(dataPermissionList, subjectPermissionPO -> {
                String permissionCode = subjectPermissionPO.getPermissionCode();
                PermissionPO permissionPO = permissionPOMap.get(permissionCode);
                String permissionExtentConfig = subjectPermissionPO.getPermissionExtentConfig();
                Map<String, String> permissionExtentConfigMap = JsonUtils.deserialize(permissionExtentConfig, Map.class);
                return Objects.isNull(permissionPO) ? ""
                                                    : permissionPO.getName() + ":" + permissionExtentConfigMap.getOrDefault("scope", "");
            });
            roleExcelDTO.setRoleDataPermissions(String.join(",", names));
        });
    }

    private void buildRoleMenus(@NonNull List<RoleExcelDTO> roleExcelDTOList) {
        //查询角色绑定的菜单
        List<RoleMenu> roleMenuList = roleMenuService.getBindMenus(ListUtils.convertItem(roleExcelDTOList, RoleExcelDTO::getId));
        //查询所有菜单信息的id映射map，用于获取菜单名称
        List<Menu> menuList = menuService.list();
        Map<Long, Menu> menusMap = MapUtils.listToMap(menuList, Menu::getId, menu -> menu);
        //查询应用信息,用于获取应用名称
        List<Application> applicationList = applicationService.list();
        Map<Long, String> applicationNameMap = MapUtils.listToMap(applicationList, Application::getId, Application::getName);
        //查询访问端信息
        List<Terminal> terminalList = terminalService.list();
        Map<Long, String> terminalNameMap = MapUtils.listToMap(terminalList, Terminal::getId, Terminal::getName);
        //根据角色分组,用于获取每个角色绑定的菜单
        Map<Long, List<RoleMenu>> roleMenuMap = MapUtils.listGroupToMap(roleMenuList, RoleMenu::getRoleId);
        //设置角色信息
        roleExcelDTOList.forEach(roleExcelDTO -> {
            List<RoleMenu> bindMenus = roleMenuMap.getOrDefault(roleExcelDTO.getId(), Collections.emptyList());
            StringBuilder roleMenus = new StringBuilder();
            bindMenus.forEach(roleMenu -> {
                Long menuId = roleMenu.getMenuId();
                Menu menu = menusMap.get(menuId);
                Long applicationId = roleMenu.getApplicationId();
                String applicationName = applicationNameMap.getOrDefault(applicationId, "");
                Long terminalId = roleMenu.getTerminalId();
                String terminalName = terminalNameMap.getOrDefault(terminalId, "");
                String menuNames = flattenMenuNames(menu, menusMap, 100);
                roleMenus.append(terminalName).append("/")
                        .append(applicationName).append("/")
                        .append(menuNames).append(",");

            });
            roleExcelDTO.setRoleMenus(roleMenus.length() > 0 ? roleMenus.substring(0, roleMenus.length() - 1) : "");
        });

    }

    private String flattenMenuNames(Menu menu, @NonNull Map<Long, Menu> menusMap, int count) {
        if (count <= 0 || menu == null) {
            return "";
        }
        count--;
        String name = menu.getName();
        if (menu.getParentId() != null && menu.getParentId() != 0) {
            Menu parentMenu = menusMap.get(menu.getParentId());
            name = flattenMenuNames(parentMenu, menusMap, count) + "/" + name;
        }
        return name;
    }

    private Pagination<RoleVO> getRolesPage(RoleQueryParam param) {
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

    private void exportAppointmentData(@NonNull String filename, @NonNull List<AppointmentDetailVO> list) {
        List<AppointmentExcelDTO> appointmentExcelDTOList = ListUtils.convertItem(list, vo -> ObjectUtils.copying(vo, AppointmentExcelDTO.class));
        //设置职位状态
        convertAppointmentEnabled(appointmentExcelDTOList);
        //导出
        excelUtils.downloadExcel(filename, appointmentExcelDTOList, AppointmentExcelDTO.class);
    }

    private void exportPositionData(@NonNull String filename, @NonNull List<Position> list) {
        List<PositionExcelDTO> positionExcelDTOList = ListUtils.convertItem(list, position -> ObjectUtils.copying(position, PositionExcelDTO.class));
        //设置职位状态
        convertPositionEnabled(positionExcelDTOList);
        //导出
        excelUtils.downloadExcel(filename, positionExcelDTOList, PositionExcelDTO.class);
    }

    private void convertPositionEnabled(@NonNull List<PositionExcelDTO> positionExcelDTOList) {
        positionExcelDTOList.forEach(positionExcelDTO -> {
            positionExcelDTO.setEnabledText(positionExcelDTO.getEnabled() ? "启用" : "禁用");
        });
    }

    private void convertAppointmentEnabled(@NonNull List<AppointmentExcelDTO> excelDTOList) {
        excelDTOList.forEach(excelDTO -> {
            excelDTO.setEnabledText(excelDTO.getEnable() ? "启用" : "禁用");
        });
    }

    private void rebuildPageParam(@NonNull Paging param) {
        Integer pageNumber = param.getPageNumber();
        param.setPageNumber(pageNumber == null || pageNumber == 0 ? 1 : pageNumber);
        param.setPageSize(MAX_EXPORT_NUM);
    }

    private List<OrgExcelDTO> convertToOrgExcelDTO(@NonNull List<OrganizationVO> trees) {
        List<OrganizationVO> flat = flattenOrgVo(trees, 100);
        Map<Long, OrganizationVO> organizationVOMap = MapUtils.listToMap(flat, OrganizationVO::getId, vo -> vo);
        return ListUtils.convertItem(flat, vo -> convertOrgVoToExcelOrgDto(vo, organizationVOMap));
    }

    private List<OrganizationVO> flattenOrgVo(@NonNull List<OrganizationVO> trees, int count) {
        ArrayList<OrganizationVO> result = new ArrayList<>();
        if (ListUtils.isEmpty(trees) || count <= 0) {
            return result;
        }
        count--;
        result.addAll(trees);
        for (OrganizationVO vo : trees) {
            List<OrganizationVO> childList = flattenOrgVo(vo.getChildren(), count);
            result.addAll(childList);
        }
        return result;
    }

    private static OrgExcelDTO convertOrgVoToExcelOrgDto(OrganizationVO vo, Map<Long, OrganizationVO> organizationVOMap) {
        OrgExcelDTO orgExcelDTO = ObjectUtils.copying(vo, OrgExcelDTO.class);
        orgExcelDTO.setEnableText(vo.getEnabled() ? "启用" : "禁用");
        Long parentId = vo.getParentId();
        int whileCount = 100;
        while (parentId != null && parentId != 0 && whileCount > 0) {
            OrganizationVO parent = organizationVOMap.get(parentId);
            String name = parent.getName();
            orgExcelDTO.setName(name + "/" + orgExcelDTO.getName());
            parentId = parent.getParentId();
            whileCount--;
        }
        return orgExcelDTO;
    }

    /**
     * 汇总组织机构人数
     *
     * @param trees    组织机构树列表
     * @param mappings 组织机构/人数映射表
     */
    private void summaryOrganizationMembers(Collection<OrganizationVO> trees, Map<Long, Long> mappings) {
        if (!CollectionUtils.isEmpty(trees) && MapUtils.isNotEmpty(mappings)) {
            trees.forEach(organization -> {
                int members = ObjectUtils.ifNull(mappings.get(organization.getId()), 0L).intValue();
                organization.setMembers(members);
                this.summaryOrganizationMembers(organization.getChildren(), mappings);
                if (!CollectionUtils.isEmpty(organization.getChildren())) {
                    organization.setMembers(members +
                                            organization.getChildren().stream().mapToInt(OrganizationVO::getMembers).sum());
                }
            });
        }
    }

    private void exportEmptyExcel(String fileName, Class<?> clazz) {
        excelUtils.downloadExcel(fileName, Collections.emptyList(), clazz);
    }

    private void exportUserListExcel(@NonNull List<UserVO> users, @NonNull String fileName) {
        List<UserExcelDTO> excelDTOList = users.stream()
                .map(userVO -> ObjectUtils.copying(userVO, UserExcelDTO.class)).collect(Collectors.toList());
        //设置用户管理的组织名称
        buildUserManagedOrganizationName(excelDTOList);
        //设置用户所属的组织名称
        buildUserOrganizationName(excelDTOList);
        //设置角色信息
        buildUserRoleName(excelDTOList);
        //设置岗位信息
        buildUserPositionName(excelDTOList);
        //设置职位信息
        buildUserAppointmentName(excelDTOList);
        //设置是否启用
        buildUserEnabled(excelDTOList);
        //导出
        excelUtils.downloadExcel(fileName, excelDTOList, UserExcelDTO.class);
    }

    private void buildUserEnabled(@NonNull List<UserExcelDTO> excelDTOList) {
        excelDTOList.forEach(userExcelDTO -> userExcelDTO.setEnableText(userExcelDTO.getEnabled() ? "启用" : "禁用"));
    }

    private void buildUserAppointmentName(@NonNull List<UserExcelDTO> excelDTOList) {
        Set<Long> userIds = SetUtils.listToSet(excelDTOList, UserExcelDTO::getId);
        List<UserOrganizationDTO> organizations = this.userOrganizationService.listUserOrganizations(userIds);
        Set<Long> positionIds = organizations.stream().map(UserOrganizationDTO::getPositionId).collect(Collectors.toSet());
        Map<Long, List<UserOrganizationDTO>> userPositionMap = MapUtils.listGroupToMap(organizations, UserOrganizationDTO::getUserId);
        List<Position> positionList = this.positionService.listPositions(positionIds);
        Map<Long, String> positionNameMap = MapUtils.listToMap(positionList, Position::getId, Position::getName);
        excelDTOList.forEach(dto -> {
            List<UserOrganizationDTO> organizationDTOList = userPositionMap.getOrDefault(dto.getId(), Collections.emptyList());
            List<Long> positionIdList = ListUtils.convertItem(organizationDTOList, UserOrganizationDTO::getPositionId);
            List<String> names = positionIdList.stream()
                    .map(positionNameMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            dto.setAppointmentList(String.join(",", names));
        });
    }

    private void buildUserPositionName(@NonNull List<UserExcelDTO> excelDTOList) {

        Set<Long> userIds = SetUtils.listToSet(excelDTOList, UserExcelDTO::getId);
        List<UserOrganizationDTO> organizations = this.userOrganizationService.listUserOrganizations(userIds);
        if (ListUtils.isEmpty(organizations)) {
            return;
        }
        Set<Long> appointmentIds = organizations.stream().map(UserOrganizationDTO::getAppointmentId).filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (ListUtils.isEmpty(appointmentIds)) {
            return;
        }
        Map<Long, List<UserOrganizationDTO>> userOrganizationMap = MapUtils.listGroupToMap(organizations, UserOrganizationDTO::getUserId);

        List<Appointment> appointmentList = this.appointmentCommandService.listByIds(appointmentIds);
        Map<Long, String> appointmentMap = MapUtils.listToMap(appointmentList, Appointment::getId, Appointment::getName);
        excelDTOList.forEach(dto -> {
            List<UserOrganizationDTO> organizationDTOList = userOrganizationMap.getOrDefault(dto.getId(), Collections.emptyList());
            List<Long> appointmentIdList = ListUtils.convertItem(organizationDTOList, UserOrganizationDTO::getAppointmentId);
            List<String> names = appointmentIdList
                    .stream()
                    .map(appointmentMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            dto.setPositions(String.join(",", names));
        });

    }

    private void buildUserRoleName(@NonNull List<UserExcelDTO> excelDTOList) {

        Set<Long> userIds = SetUtils.listToSet(excelDTOList, UserExcelDTO::getId);
        Map<Long, List<UserRoleDTO>> userRoleMappings = this.userRoleService.listUserRoles(userIds).stream()
                .collect(Collectors.groupingBy(UserRoleDTO::getUserId));
        excelDTOList.forEach(dto -> {
            // 设置用户角色列表
            List<UserRoleDTO> roles = userRoleMappings.get(dto.getId());
            List<String> names = ListUtils.convertItem(roles, UserRoleDTO::getName);
            dto.setRoles(String.join(",", names));
        });

    }

    private void buildUserOrganizationName(@NonNull List<UserExcelDTO> excelDTOList) {

        Set<Long> userIds = SetUtils.listToSet(excelDTOList, UserExcelDTO::getId);
        List<UserOrganizationDTO> userOrganizations = this.userOrganizationService.listUserOrganizations(userIds);

        Map<Long, List<UserOrganizationDTO>> userOrganizationGroups = userOrganizations.stream()
                .collect(Collectors.groupingBy(UserOrganizationDTO::getUserId));
        // 设置用户组织机构
        excelDTOList.forEach(dto -> {
            List<UserOrganizationDTO> organizations = userOrganizationGroups.getOrDefault(dto.getId(), Collections.emptyList());
            UserOrganizationDTO organization = organizations.stream().findAny().orElse(null);
            ObjectUtils.runOrElse(organization, org -> {
                List<Long> ids = Arrays.stream(org.getSequence().split("-")).map(Long::parseLong).collect(Collectors.toList());
                List<Organization> organizationList = this.organizationService.listOrganizations(ids);
                List<String> names = ListUtils.convertItem(organizationList, Organization::getName);
                dto.setOrganizations(String.join(",", names));
            }, () -> dto.setOrganizations(""));
        });

    }

    private void buildUserManagedOrganizationName(@NonNull List<UserExcelDTO> excelDTOList) {
        // 获取用户相关组织机构和岗位及角色
        Set<Long> userIds = SetUtils.listToSet(excelDTOList, UserExcelDTO::getId);

        List<UserManagedOrganization> userManagedOrganizations = this.userManagedOrganizationService.list(
                Wrappers.<UserManagedOrganization>lambdaQuery()
                        .in(UserManagedOrganization::getUserId, userIds)
                                                                                                         );
        Map<Long, List<UserManagedOrganization>> userIdToOrganMap = userManagedOrganizations.stream()
                .collect(Collectors.groupingBy(UserManagedOrganization::getUserId));

        Map<Long, String> managedOrganizationsNameMap = this.organizationService.listOrganizations(
                        userManagedOrganizations.stream().map(UserManagedOrganization::getOrganizationId).collect(Collectors.toList())
                                                                                                  ).stream()
                .collect(Collectors.toMap(Organization::getId, Organization::getName));
        excelDTOList.forEach(dto -> {
            List<UserManagedOrganization> managedOrganizations = userIdToOrganMap.getOrDefault(dto.getId(), Collections.emptyList());
            if (ListUtils.isEmpty(managedOrganizations)) {
                return;
            }
            List<Long> ids = ListUtils.convertItem(managedOrganizations, UserManagedOrganization::getOrganizationId);
            List<String> names = ListUtils.convertItem(ids, id -> managedOrganizationsNameMap.getOrDefault(id, ""));
            dto.setManagedOrganizations(String.join(",", names));
        });

    }
}
