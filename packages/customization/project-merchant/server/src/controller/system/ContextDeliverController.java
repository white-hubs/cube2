package com.guhecloud.cube.deliver.project.merchant.controller.system;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.biz.ouaa.client.dto.MenuDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.dto.UserInfoDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.system.MenuDeliverDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.system.UserProjectDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Project;
import com.guhecloud.cube.deliver.project.merchant.entity.ProjectUser;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDeliver;
import com.guhecloud.cube.deliver.project.merchant.entity.system.PurchaserDeliver;
import com.guhecloud.cube.deliver.project.merchant.entity.system.Resource;
import com.guhecloud.cube.deliver.project.merchant.entity.system.UserLink;
import com.guhecloud.cube.deliver.project.merchant.enums.ProjectEnum;
import com.guhecloud.cube.deliver.project.merchant.enums.UserTypeEnum;
import com.guhecloud.cube.deliver.project.merchant.service.UserDeliverService;
import com.guhecloud.cube.deliver.project.merchant.service.system.ProjectService;
import com.guhecloud.cube.deliver.project.merchant.service.system.ProjectUserService;
import com.guhecloud.cube.deliver.project.merchant.service.system.PurchaserDeliverService;
import com.guhecloud.cube.deliver.project.merchant.service.system.ResourceService;
import com.guhecloud.cube.deliver.project.merchant.service.system.UserLinkService;

import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ContextDeliverController {

    private static final String UNIT_WAN = "万";
    private static final String TERMINAL_TYPE_PC = "pc";
    private static final String TERMINAL_TYPE_APP = "app";

    private final UserDeliverService userDeliverService;
    //    private final RoleService roleService;
    //    private final UserRoleService userRoleService;
    //    private final ResourcePermissionService resourcePermissionService;
    private final ResourceService resourceService;

    //    private final DeptService deptService;
    //    private final PostService postService;
    private final ProjectService projectService;
    private final ProjectUserService projectUserService;
    //    private final DeptUserService deptUserService;
    //    private final PostUserService postUserService;
    //    private final FileService fileService;
    private final PurchaserDeliverService purchaserDeliverService;
    private final UserLinkService userLinkService;

    //    @GetMapping("/context/projects")
    //        //    public List<ProjectDTO> getProjects(LoginUser loginUser) {
    //        List<ProjectDTO> projectDTOS = new ProjectDTO(projectService.lambdaQuery().eq(Project::getEnabled, true).list()).list();
    //        projectDTOS.parallelStream().forEach(projectDTO -> this.extendProjectDTO(projectDTO, loginUser.getId()));
    //        return projectDTOS;
    //    }

    @GetMapping("/context/user-projects")
    public List<UserProjectDTO> getUserProjects(@RequestParam(name = "terminalType", defaultValue = TERMINAL_TYPE_PC) String terminalType,
                                                LoginUser loginUser
                                               ) {
        // 获取当前用户信息
        Principal principal = null;
        try {
            principal = SecurityContextHolder.getPrincipal();
            loginUser.setId(principal.getId());
        } catch (Exception e) {

        }
        List<UserProjectDTO> userProjectDTOs;
        if (UserDeliverService.ADMIN_USERNAME.equals(loginUser.getUsername())) {
            userProjectDTOs = projectService.lambdaQuery().list().parallelStream().map(project -> {
                if (!project.getEnabled()) {
                    return null;
                }
                return buildUserProjectDTO(project, loginUser.getId());
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } else {
            userProjectDTOs = projectUserService.lambdaQuery().eq(ProjectUser::getUserId, loginUser.getId()).list().parallelStream()
                    .map(mapping -> {
                        Project project = projectService.lambdaQuery().eq(Project::getId, mapping.getProjectId()).one();
                        if (!project.getEnabled()) {
                            return null;
                        }
                        return buildUserProjectDTO(project, loginUser.getId());
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        if (TERMINAL_TYPE_APP.equals(terminalType)) {
            userProjectDTOs.removeIf(p -> ProjectEnum.CODE_HQ.getCode().equals(p.getCode()) ||
                                          ProjectEnum.CODE_SNCD.getCode().equals(p.getCode()));
        }
        return userProjectDTOs;
    }

    @GetMapping("/public/context/user-info/type")
    public List<String> getUserTypes(@RequestParam(name = "mobile") String mobile) {
        List<String> userTypes = new ArrayList<>();
        UserDeliver user = userDeliverService.getOne(new LambdaQueryWrapper<UserDeliver>()
                                                             .eq(UserDeliver::getMobile, mobile));
        if (user != null) {
            userTypes.add(UserTypeEnum.USER_TYPE_COMPANY.getCode());
        }
        PurchaserDeliver purchaserDeliver = purchaserDeliverService.lambdaQuery().eq(PurchaserDeliver::getMobile, mobile).one();
        if (purchaserDeliver != null) {
            userTypes.add(purchaserDeliver.getType());
        }
        return userTypes;
    }

    @GetMapping("/context/user-info")
    public UserInfoDTO getUserInfo(@RequestParam(name = "group") String group, @RequestParam(name = "projectCode") String projectCode,
                                   @RequestParam("userType") String userType, LoginUser loginUser
                                  ) {
        // 获取当前用户信息
        Principal principal = null;
        try {
            principal = SecurityContextHolder.getPrincipal();
            loginUser.setId(principal.getId());
        } catch (Exception e) {

        }
        // 构建用户信息
        UserInfoDTO userInfoDTO = null;
        // 系统用户
        UserDeliver user = userDeliverService.getById(loginUser.getId());
        if (user != null) {
            userInfoDTO = Convert.convert(UserInfoDTO.class, user);
        } else {
            // 非系统用户（采购商）
            userInfoDTO = new UserInfoDTO();
            PurchaserDeliver purchaserDeliver = purchaserDeliverService.getById(loginUser.getId());
            if (ObjectUtil.isNotEmpty(purchaserDeliver)) {
                userInfoDTO = Convert.convert(UserInfoDTO.class, purchaserDeliver);
            } else {
                throw new RuntimeException("用户不存在");
            }
        }
        // 获取用户的openid
        if (!ResourceService.GROUP_WEB_SITE.equals(group) && !ResourceService.GROUP_WEB_HQ.equals(group)) {
            UserLink userLink = userLinkService.lambdaQuery().eq(UserLink::getUserId, userInfoDTO.getId()).one();
            userInfoDTO.setWxopenid(userLink == null ? "" : userLink.getWxopenid());
        }
        // 构建项目资源权限
        Project project = projectService.lambdaQuery().eq(Project::getCode, projectCode).one();
        if (project == null) {
            return userInfoDTO;
        }
        // 获取所有菜单和功能，用于后续构建提升性能
        List<Resource> allFunctions = resourceService.lambdaQuery().eq(Resource::getGroup, group)
                .eq(Resource::getType, ResourceService.TYPE_FUNCTION).eq(Resource::getEnabled, true).list();
        List<MenuDeliverDTO> allMenus = resourceService.lambdaQuery()
                .eq(Resource::getGroup, group)
                .eq(Resource::getType, ResourceService.TYPE_MENU)
                .eq(Resource::getEnabled, true)
                .in("userType_purchaser".equals(userType), Resource::getIdentityType, "userType_purchaser", "admin")
                .list().stream().map(resource -> {
                    return MenuDeliverDTO.builder()
                            .id(resource.getId())
                            .pid(resource.getPid())
                            .code(resource.getCode())
                            .name(resource.getName())
                            .icon(resource.getIcon())
                            .path(resource.getPath())
                            .sort(resource.getSort())
                            .build();
                }).collect(Collectors.toList());
        //        this.buildProjectResourceInfo(userInfoDTO, project.getId(), group, allMenus, allFunctions);
        userInfoDTO.setMenus(buildGraph(allMenus));
        userInfoDTO.setFunctionCodes(allFunctions.stream().map(Resource::getCode).collect(Collectors.toSet()));
        return userInfoDTO;
    }

    //    private void extendProjectDTO(ProjectDTO projectDTO, String userId) {
    //        projectDTO.setFileDTOS(new FileDTO(fileService.lambdaQuery().eq(File::getBusinessType, ProjectService.BUSINESS_TYPE).eq(File::getBusinessKey, projectDTO.getId()).list()).list());
    //        if (projectDTO.getLaunched()) {
    //            projectDTO.setAccessible(projectUserService.lambdaQuery().eq(ProjectUser::getProjectId, projectDTO.getId()).eq(ProjectUser::getUserId, userId).one() != null);
    //            if (projectDTO.getLaunchDate() != null && projectDTO.getLaunchDate().before(LocalDateTime.now())) {
    //                projectDTO.setLaunchDay((int) ((System.currentTimeMillis() - projectDTO.getLaunchDate().getTime()) / (1000 * 3600 * 24)));
    //            } else {
    //                projectDTO.setLaunchDay(0);
    //            }
    //        } else {
    //            projectDTO.setAccessible(false);
    //        }
    //    }

    private void buildProjectResourceInfo(UserInfoDTO dto, String projectId, String group, List<MenuDeliverDTO> allMenus,
                                          List<Resource> allFunctions
                                         ) {
        ProjectUser mapping = projectUserService.lambdaQuery().eq(ProjectUser::getProjectId, projectId)
                .eq(ProjectUser::getUserId, dto.getId()).one();
        if (UserDeliverService.ADMIN_USERNAME.equals(dto.getUsername()) || (mapping != null && mapping.getManager())) {
            this.buildManagerProjectResourceInfo(dto, allMenus, allFunctions, group, projectId);
        } else {
            //            this.buildUserProjectResourceInfo(dto, projectId, group, allMenus, allFunctions);
        }
    }

    private void buildManagerProjectResourceInfo(UserInfoDTO result, List<MenuDeliverDTO> allMenus, List<Resource> allFunctions,
                                                 String group, String projectId
                                                ) {
        // 如果是总部的项目管理员，则排除菜单功能、设备类型、项目菜单、数据字典四个菜单
        if (ResourceService.GROUP_WEB_HQ.equals(group) && !UserDeliverService.ADMIN_USERNAME.equals(result.getUsername())) {
            allMenus.removeIf(menuDTO -> ResourceService.CODE_MENU_ZBCDGN_HQ.equals(menuDTO.getCode()));
            allMenus.removeIf(menuDTO -> ResourceService.CODE_MENU_ZBSBLX_HQ.equals(menuDTO.getCode()));
            allMenus.removeIf(menuDTO -> ResourceService.CODE_MENU_XMCD_HQ.equals(menuDTO.getCode()));
            allMenus.removeIf(menuDTO -> ResourceService.CODE_MENU_ZBSJZD_HQ.equals(menuDTO.getCode()));
        } else if (!ResourceService.GROUP_WEB_HQ.equals(group)) {
            // 项目管理员，判断现有的全量资源是否是项目资源
            //            List<ResourcePermission> projectResources = resourcePermissionService.lambdaQuery().eq(ResourcePermission::getObjectType, ResourcePermissionService.OBJECT_TYPE_PROJECT).eq(ResourcePermission::getObjectId, projectId).list();
            //            allMenus.removeIf(menuDTO -> projectResources.stream().noneMatch(projectResource -> projectResource.getResourceId().equals(menuDTO.getId())));
        }
        // admin赋予全部
        //        result.setMenus(buildGraph(allMenus));
        //        result.setFunctionCodes(allFunctions.stream().map(Resource::getCode).collect(Collectors.toSet()));
    }

    //    private void buildUserProjectResourceInfo(UserInfoDTO dto, String projectId, String group, List<MenuDeliverDTO> allMenus, List<Resource> allFunctions) {
    //        Set<String> allFunctionIdsSet = allFunctions.stream().map(Resource::getId).collect(Collectors.toSet());
    //        Set<String> allMenuIdsSet = allMenus.stream().map(MenuDeliverDTO::getId).collect(Collectors.toSet());
    //        // 用户资源id
    //        Set<String> userResourceIds = new HashSet<>();
    //
    //        // 默认赋予的资源权限
    //        if (ResourceService.GROUP_WEB_HQ.equals(group)) {
    //            List<String> menuCodes = new ArrayList<>();
    //            // 赋予：个人设置
    //            menuCodes.add("Personal");
    //            // 赋予菜单及其下功能权限
    //            this.buildDefaultUserProjectResourceInfo(userResourceIds, group, this.parseMenuPath(menuCodes, allMenus), allFunctions);
    //        }
    // 查询用户是否拥有角色，如果有则赋予角色权限，没有则给默认权限
    //        List<UserRole> userRoles = userRoleService.lambdaQuery().eq(UserRole::getUserId, dto.getId()).list();
    //        if (ResourceService.GROUP_WEB_SITE.equals(group)) {
    //            List<String> menuCodes = new ArrayList<>();
    //            // 赋予：个人设置
    //            menuCodes.add("Personal-Site");
    //            if (userRoles.size() < 1) {
    //                // 赋予：安全运维
    //            menuCodes.add("WDSQ");
    //            menuCodes.add("WDGD");
    //            menuCodes.add("WDWB");
    //            menuCodes.add("WDXFXJ");
    //            menuCodes.add("DQHZWDRW");
    //            }
    //            // 赋予：系统设置
    //            // 赋予菜单及其下功能权限
    //            this.buildDefaultUserProjectResourceInfo(userResourceIds, group, this.parseMenuPath(menuCodes, allMenus), allFunctions);
    //        }
    //        if (ResourceService.GROUP_WEB_APP.equals(group)) {
    //            List<String> menuCodes = new ArrayList<>();
    //            if (userRoles.size() < 1) {
    //                // 赋予：快捷入口
    //            menuCodes.add("kjrk_xzsq");
    //            menuCodes.add("kjrk_fqgd");
    //            menuCodes.add("kjrk_tjyh");
    //            menuCodes.add("kjrk_sjsb");
    //            // 赋予：工作台
    //            menuCodes.add("app_jkdk2");  // 健康打卡
    //            menuCodes.add("app_wddk");   // 我的打卡
    //            menuCodes.add("app_wdxs");   // 我的消杀
    //            menuCodes.add("app_tjsq");   // 添加诉求
    //            menuCodes.add("app_wdsq");   // 我的诉求
    //            menuCodes.add("app_tjgd");   // 添加工单
    //            menuCodes.add("app_wdgd");   // 我的工单
    //            menuCodes.add("app_wdxfxjrw"); // 我的消防巡检任务
    //            menuCodes.add("app_smxfxj");   // 扫描消防巡检
    //            menuCodes.add("app_xfxjrw");   // 巡检任务
    //            menuCodes.add("app_xfxjjl");   // 巡检记录
    //            menuCodes.add("app_cfxjjh");   // 巡检计划
    //            menuCodes.add("app-tjyh");     // 添加隐患
    //            menuCodes.add("app-wdyh");     // 我的隐患
    //            menuCodes.add("app_bjsj");     // 报警事件
    //            }
    // 赋予菜单及其下功能权限
    //            this.buildDefaultUserProjectResourceInfo(userResourceIds, group, this.parseMenuPath(menuCodes, allMenus), allFunctions);
    //        }
    // 商户端
    //        if (ResourceService.GROUP_WEB_STORE.equals(group)) {
    //            List<String> menuCodes = new ArrayList<>();
    //            // 查询当前用户类型的菜单权限
    //            List<String> allProjectStoreResourceIds2 = resourcePermissionService.lambdaQuery().eq(ResourcePermission::getObjectId, projectId).eq(ResourcePermission::getObjectType, dto.getType()).list().stream().map(ResourcePermission::getResourceId).collect(Collectors.toList());
    //            allProjectStoreResourceIds2.forEach(resourceId -> {
    //
    //            });
    // 不管分配与否都给默认菜单，以防没有菜单
    // 赋予默认菜单
    //            menuCodes.add("SH_SBSQ");
    //            menuCodes.add("SH_WDSQ");
    //            menuCodes.add("SH_WDXX");
    //            if (UserTypeEnum.USER_TYPE_COMPANY.getCode().equals(dto.getType())) {
    //                menuCodes.add("SH_JKDK");
    //            }
    // 赋予菜单及其下功能权限
    //            this.buildDefaultUserProjectResourceInfo(userResourceIds, group, this.parseMenuPath(menuCodes, allMenus), allFunctions);
    //        }
    //
    //        List<MenuDeliverDTO> userMenuDTOs = new ArrayList<>();
    //        Set<String> userFunctionCodes = new HashSet<>();
    //        for (String userResourceId : userResourceIds) {
    //            Resource userResource = resourceService.getById(userResourceId);
    //            if (!userResource.getEnabled()) {
    //                continue;
    //            }
    //            if (ResourceService.TYPE_MENU.equals(userResource.getType())) {
    //                userMenuDTOs.add(MenuDeliverDTO.builder()
    //                        .id(userResource.getId())
    //                        .pid(userResource.getPid())
    //                        .code(userResource.getCode())
    //                        .name(userResource.getName())
    //                        .icon(userResource.getIcon())
    //                        .path(userResource.getPath())
    //                        .sort(userResource.getSort())
    //                        .build());
    //            }
    //            if (ResourceService.TYPE_FUNCTION.equals(userResource.getType())) {
    //                userFunctionCodes.add(userResource.getCode());
    //            }
    //        }
    //        dto.setMenus(buildGraph(userMenuDTOs));
    //        dto.setFunctionCodes(userFunctionCodes);
    //    }

    private List<String> parseMenuPath(List<String> menuCodes, List<MenuDeliverDTO> allMenus) {
        List<String> result = new ArrayList<>();
        for (String menuCode : menuCodes) {
            MenuDeliverDTO menuDTO = allMenus.stream().filter(menu -> menu.getCode().equals(menuCode)).findAny().orElse(null);
            if (menuDTO == null) {
                continue;
            }
            result.add(menuCode);
            MenuDeliverDTO parentMenuDTO = null;
            if (StringUtils.isNotEmpty(menuDTO.getPid())) {
                parentMenuDTO = allMenus.stream().filter(menu -> menu.getId().equals(menuDTO.getPid())).findAny().orElse(null);
            }
            while (parentMenuDTO != null) {
                result.add(parentMenuDTO.getCode());
                String pid = parentMenuDTO.getPid();
                if (StringUtils.isEmpty(pid)) {
                    parentMenuDTO = null;
                } else {
                    parentMenuDTO = allMenus.stream().filter(menu -> menu.getId().equals(pid)).findAny().orElse(null);
                }
            }
        }
        return result;
    }

    private void buildDefaultUserProjectResourceInfo(Set<String> userResourceIds, String group, List<String> menuCodes,
                                                     List<Resource> allFunctions
                                                    ) {
        for (String menuCode : menuCodes) {
            Resource menuResource = resourceService.lambdaQuery().eq(Resource::getGroup, group).eq(Resource::getCode, menuCode).one();
            if (menuResource == null) {
                continue;
            }
            MenuDeliverDTO menuDTO = MenuDeliverDTO.builder()
                    .id(menuResource.getId())
                    .pid(menuResource.getPid())
                    .code(menuResource.getCode())
                    .name(menuResource.getName())
                    .icon(menuResource.getIcon())
                    .path(menuResource.getPath())
                    .sort(menuResource.getSort())
                    .build();
            //            Set<String> menuFunctionIds = allFunctions.stream().filter(function -> isFunctionOfMenu(function, Arrays.asList(menuDTO))).map(function -> function.getId()).collect(Collectors.toSet());
            //            userResourceIds.add(menuResource.getId());
            //            userResourceIds.addAll(menuFunctionIds);
        }
    }

    private UserProjectDTO buildUserProjectDTO(Project project, Long userId) {
        UserProjectDTO result = new UserProjectDTO();
        // 项目基本信息
        result.setId(project.getId());
        result.setCode(project.getCode());
        result.setName(project.getName());
        result.setAbbr(project.getAbbr());
        result.setModel(project.getModel());
        result.setLongitude(project.getLongitude());
        result.setLatitude(project.getLatitude());
        result.setProvinceCode(project.getProvinceCode());
        result.setCityCode(project.getCityCode());
        result.setAddress(project.getAddress());
        result.setUrl(project.getUrl());
        result.setApi(project.getApi());
        result.setLaunchDate(project.getLaunchDate());
        result.setHasLaunch(project.getLaunched());
        result.setFigures(project.getFigures());
        result.setAcreage(project.getAcreage());
        //        result.setAcreageStr(this.getAcreageStr(project.getAcreage()));
        // 部门
        //        Dept dept = determineUserDept(project.getId(), userId);
        //        if (dept != null) {
        //            result.setDeptId(dept.getId());
        //            result.setDeptName(dept.getName());
        //            result.setIsDeptManager(userId.equals(dept.getManagerId()));
        //        }
        // 岗位
        //        result.setPostDTOS(determineUserPost(project.getId(), userId));
        // 分管部门
        //        result.setLeadDeptIds(deptService.lambdaQuery().eq(Dept::getProjectId, project.getId()).eq(Dept::getLeaderId, userId).list().stream().map(Dept::getId).collect(Collectors.toList()));
        return result;
    }
    //
    //    private String getAcreageStr(int acreage) {
    //        if (acreage < 10000) {
    //            return String.valueOf(acreage);
    //        } else {
    //            // 保留2位小数
    //            BigDecimal bg = new BigDecimal((double) acreage / 10000).setScale(0, RoundingMode.HALF_UP).stripTrailingZeros();
    //            return bg.toPlainString() + UNIT_WAN;
    //        }
    //    }
    //
    //    private Dept determineUserDept(String projectId, String userId) {
    //        DeptUser deptUser = deptUserService.lambdaQuery().eq(DeptUser::getProjectId, projectId).eq(DeptUser::getUserId, userId).one();
    //        if (deptUser != null) {
    //            return deptService.getById(deptUser.getDeptId());
    //        }

    /// /        List<DeptUser> userDepts = deptUserService.lambdaQuery().eq(DeptUser::getUserId, userId).list();
    /// /        for (DeptUser userDept : userDepts) {
    /// /            Dept dept = deptService.getById(userDept.getDeptId());
    /// /            if (projectId.equals(dept.getProjectId())) {
    /// /                return dept;
    /// /            }
    /// /        }
    //        return null;
    //    }
    //
    //    private List<PostDTO> determineUserPost(String projectId, String userId) {
    //        List<PostDTO> postDTOS = new ArrayList<>();
    //        List<PostUser> userPosts = postUserService.lambdaQuery().eq(PostUser::getProjectId, projectId).eq(PostUser::getUserId, userId).list();
    //        for (PostUser userPost : userPosts) {
    //            Post post = postService.getById(userPost.getPostId());
    //            if (post != null) {
    //                postDTOS.add(new PostDTO(post));
    //            }
    //        }
    //        return postDTOS;
    //    }
    //
    private List<MenuDeliverDTO> buildGraph(List<MenuDeliverDTO> menus) {
        List<MenuDeliverDTO> rootMenuDTOS = menus.stream().filter(menu -> StringUtils.isEmpty(menu.getPid()))
                .sorted(Comparator.comparing(MenuDeliverDTO::getSort).thenComparing(MenuDeliverDTO::getId)).collect(Collectors.toList());
        rootMenuDTOS.forEach(rootMenuDTO -> getChildren(rootMenuDTO, menus));
        return rootMenuDTOS;
    }

    private void getChildren(MenuDeliverDTO menuDTO, List<MenuDeliverDTO> menus) {
        List<MenuDeliverDTO> childrenMenuDTOS = menus.stream().filter(userProjectMenu -> userProjectMenu.getPid().equals(menuDTO.getId()))
                .sorted(Comparator.comparing(MenuDeliverDTO::getSort).thenComparing(MenuDeliverDTO::getId)).collect(Collectors.toList());
        childrenMenuDTOS.forEach(childrenMenuDTO -> getChildren(childrenMenuDTO, menus));
        menuDTO.setChildren(childrenMenuDTOS);
    }

    private boolean isFunctionOfMenu(Resource function, List<MenuDTO> menus) {
        List<MenuDTO> children = new ArrayList<>();
        for (MenuDTO menu : menus) {
            if (menu.getId().equals(function.getPid())) {
                return true;
            }
            if (menu.getChildren() != null) {
                children.addAll(menu.getChildren());
            }
        }
        if (children.size() > 0) {
            return isFunctionOfMenu(function, children);
        }
        return false;
    }

}
