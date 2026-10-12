package com.guhecloud.cube.biz.ouaa.server.controller.context;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserOrganizationDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserTenantDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationGroup;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationVisitHistory;
import com.guhecloud.cube.biz.ouaa.server.entity.Card;
import com.guhecloud.cube.biz.ouaa.server.entity.HomeApplication;
import com.guhecloud.cube.biz.ouaa.server.entity.HomeApplicationCard;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleApplication;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenu;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleTerminal;
import com.guhecloud.cube.biz.ouaa.server.entity.Tenant;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.entity.TerminalApplication;
import com.guhecloud.cube.biz.ouaa.server.entity.UserRole;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTel;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.entity.UserWechat;
import com.guhecloud.cube.biz.ouaa.server.param.PasswordUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.param.ResetPasswordParam;
import com.guhecloud.cube.biz.ouaa.server.param.RoleMenusByPathParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserPersonalUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationGroupService;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationVisitHistoryService;
import com.guhecloud.cube.biz.ouaa.server.service.CardService;
import com.guhecloud.cube.biz.ouaa.server.service.HomeApplicationCardService;
import com.guhecloud.cube.biz.ouaa.server.service.HomeApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.MenuFunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.MenuService;
import com.guhecloud.cube.biz.ouaa.server.service.OrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.PositionService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuDataPermissionService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuFunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleTerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantProductService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.UserOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTelService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.cube.biz.ouaa.server.service.UserWechatService;
import com.guhecloud.cube.biz.ouaa.server.service.WechatService;
import com.guhecloud.cube.biz.ouaa.server.utils.CustomConfigUtil;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationCardVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationGroupPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationVO;
import com.guhecloud.cube.biz.ouaa.server.vo.CardVO;
import com.guhecloud.cube.biz.ouaa.server.vo.DataPermissionVO;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuFunctionVO;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuVO;
import com.guhecloud.cube.biz.ouaa.server.vo.TenantVO;
import com.guhecloud.cube.biz.ouaa.server.vo.TerminalVO;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import com.guhecloud.platform.security.permission.cache.PermissionCache;
import com.guhecloud.platform.security.permission.model.PrincipalMethodPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @date 2021/10/26
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAuthenticated()")
public class ContextController {

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final PositionService positionService;
    private final OrganizationService organizationService;
    private final UserOrganizationService userOrganizationService;
    private final FileClient fileClient;
    private final TenantService tenantService;
    private final UserTenantService userTenantService;
    private final TenantProductService tenantProductService;

    private final TerminalService terminalService;
    private final ApplicationService applicationService;
    private final MenuService menuService;
    private final TenantApplicationService tenantApplicationService;
    private final TerminalApplicationService terminalApplicationService;
    private final RoleService roleService;

    private final RoleApplicationService roleApplicationService;
    private final RoleTerminalService roleTerminalService;
    private final RoleMenuService roleMenuService;
    private final HomeApplicationService homeApplicationService;
    private final HomeApplicationCardService homeApplicationCardService;
    private final ApplicationGroupService applicationGroupService;
    private final CardService cardService;
    private final ApplicationVisitHistoryService applicationVisitHistoryService;
    private final RoleMenuFunctionService roleMenuFunctionService;
    private final RoleMenuDataPermissionService roleMenuDataPermissionService;
    private final MenuFunctionService menuFunctionService;
    private final CustomConfigUtil customConfigUtil;
    private final UserTelService userTelService;
    private final UserWechatService userWechatService;
    private final WechatService wechatService;
    private final PermissionCache permissionCache;

    @GetMapping("/context/user")
    public UserVO getContextUser() {
        Long id = SecurityContextHolder.getPrincipal().getId();
        UserVO user = ObjectUtils.copying(this.userService.getById(id), UserVO.class);
        if (user != null) {
            boolean bindWechat = false;

            Set<Long> userIds = Collections.singleton(user.getId());
            List<UserOrganizationDTO> userOrganizations = this.userOrganizationService.listUserOrganizations(userIds);
            //设置用户绑定信息
            boolean binsTelResult = userTelService.checkUserBindExist(user.getId());
            user.setIsBindTel(binsTelResult);
            //如果用户已经绑定了手机号，则设置绑定的手机号
            if (binsTelResult) {
                UserTel userTel = userTelService.getBindByUserId(user.getId());
                Optional.ofNullable(userTel).ifPresent(tel -> user.setMobile(userTel.getTel()));
            }
            UserWechat wechatInfo = null;
            user.setIsBindWechat(false);
            try {

                boolean wechatAbility = wechatService.checkWechatAbility();

                if (wechatAbility) {
                    wechatInfo = userWechatService.getWechatInfo(user.getId());
                    user.setIsBindWechat(Objects.nonNull(wechatInfo));
                    //查询用户是否有进行微信绑定
                    bindWechat = Objects.nonNull(userWechatService.getWechatInfo(user.getId()));
                    if (bindWechat) {
                        //获取微信用户信息
                        Optional.ofNullable(wechatInfo).ifPresent(info -> user.setWechatName(info.getNickname()));
                    }
                }
            } catch (Exception e) {
                log.error("获取微信用户信息异常", e);
            }

            // 获取用户头像
            List<FileVO> fileVOs = this.fileClient.getByBusinessKey(user.getId());
            if (ListUtils.isEmpty(fileVOs)) {
                //查询用户是否有进行微信绑定
                FileVO fileVO = new FileVO();
                if (bindWechat) {
                    Optional
                            .ofNullable(wechatInfo)
                            .ifPresent(userWechat -> {
                                fileVO.setPath(userWechat.getAvatar());
                                user.setFileVO(fileVO);
                            });

                } else {
                    fileVO.setPath(user.getAvatar());
                    user.setFileVO(fileVO);
                }

            } else {
                user.setFileVO(fileVOs.get(0));
            }
            // 设置用户组织机构
            UserOrganizationDTO organization = userOrganizations.stream().findAny().orElse(null);

            if (organization != null) {
                List<Long> ids = Arrays.stream(organization.getSequence().split("-")).map(Long::parseLong).collect(Collectors.toList());
                List<Organization> organizationList = this.organizationService.listOrganizations(ids);
                user.setOrganizations(ObjectUtils.copying(organizationList, UserVO.Organization.class));
            } else {
                user.setOrganizations(Collections.emptyList());
            }

            // 设置用户岗位列表
            Set<Long> positionIds =
                    userOrganizations.stream().map(UserOrganizationDTO::getPositionId).collect(Collectors.toSet());
            List<Position> positions = this.positionService.listPositions(positionIds);
            user.setPositions(ObjectUtils.copying(positions, UserVO.Position.class));

            // 设置用户角色列表
            List<UserRoleDTO> roles = this.userRoleService.listUserRoles(userIds);
            user.setRoles(ObjectUtils.copying(roles, UserVO.Role.class));
            // 设置用户工作状态
            Set<Long> workStatusUserIdParam = new HashSet<>();
            workStatusUserIdParam.add(user.getId());
            String userWorkStatus = null;
            List<UserTenantDTO> userTenantList = userTenantService.getTenantUserWorkStatus(SessionContextHolder.getRequester()
                                                                                                   .getTenant(), workStatusUserIdParam);
            if (userTenantList != null && !userTenantList.isEmpty()) {
                userWorkStatus = userTenantList.get(0).getWorkStatus();
            }
            if (StringUtils.isNotBlank(userWorkStatus)) {
                user.setWorkStatus(userWorkStatus);
            }
            user.setPassword(null);
        }
        return user;
    }

    @GetMapping("/user/mine")
    public UserVO getUser() {
        return this.getContextUser();
    }

    /**
     * 获取当前用户的租户集合
     *
     * @return 租户列表
     */
    @GetMapping("/context/tenants")
    public List<TenantVO> getContextTenants() {
        Principal principal = SecurityContextHolder.getPrincipal();
        List<UserTenant> userTenants = this.userTenantService.listUserTenants(principal.getId());
        List<Integer> tenants = userTenants.stream().map(UserTenant::getTenant).collect(Collectors.toList());
        List<TenantVO> tenantVOS = ObjectUtils.copying(this.tenantService.listTenantsByCode(tenants), TenantVO.class);
        List<Integer> mainEnterpriseTenants = userTenants.stream().filter(UserTenant::getMainEnterprise).map(UserTenant::getTenant)
                .collect(Collectors.toList());
        List<Integer> recentLoginTenants = userTenants.stream().filter(UserTenant::getRecentLogin).map(UserTenant::getTenant)
                .collect(Collectors.toList());
        List<Integer> adminTenants = userTenants.stream().filter(UserTenant::getAdmin).map(UserTenant::getTenant)
                .collect(Collectors.toList());
        return tenantVOS.stream().map(tenantVO -> {
            tenantVO.setRecentLogin(recentLoginTenants.contains(tenantVO.getCode()));
            tenantVO.setMainEnterprise(mainEnterpriseTenants.contains(tenantVO.getCode()));
            tenantVO.setAdmin(adminTenants.contains(tenantVO.getCode()));
            return tenantVO;
        }).collect(Collectors.toList());
    }

    /**
     * 企业管理端获取当前用户为管理员的企业集合
     *
     * @return 租户列表
     */
    @GetMapping("/context/admin/tenants")
    public List<TenantVO> getContextAdminTenants() {
        Principal principal = SecurityContextHolder.getPrincipal();
        List<UserTenant> userTenants = this.userTenantService.getByUserId(principal.getId());
        List<Integer> tenants = userTenants.stream().filter(UserTenant::getAdmin).map(UserTenant::getTenant).collect(Collectors.toList());
        List<TenantVO> tenantVOS = ObjectUtils.copying(this.tenantService.listTenantsByCode(tenants), TenantVO.class);
        List<Integer> mainEnterpriseTenants = userTenants.stream().filter(UserTenant::getMainEnterprise).map(UserTenant::getTenant)
                .collect(Collectors.toList());
        List<Integer> recentLoginTenants = userTenants.stream().filter(UserTenant::getRecentLogin).map(UserTenant::getTenant)
                .collect(Collectors.toList());
        return tenantVOS.stream().map(tenantVO -> {
            tenantVO.setRecentLogin(recentLoginTenants.contains(tenantVO.getCode()));
            tenantVO.setMainEnterprise(mainEnterpriseTenants.contains(tenantVO.getCode()));
            tenantVO.setAdmin(true);
            return tenantVO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取当前用户上次登录租户
     *
     * @return 租户信息
     */
    @GetMapping("/context/recent-login-tenant")
    public TenantVO getRecentLoginTenant() {
        Principal principal = SecurityContextHolder.getPrincipal();
        List<UserTenant> userTenants = this.userTenantService.listUserTenants(principal.getId());
        userTenants = userTenants.stream().filter(UserTenant::getRecentLogin).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userTenants)) {
            return null;
        }
        List<Tenant> tenants = this.tenantService.listTenantsByCode(userTenants.stream().map(UserTenant::getTenant)
                                                                            .collect(Collectors.toList()));
        return ObjectUtils.copying(tenants.get(0), TenantVO.class);
    }

    @GetMapping("/tenant/recent-login")
    public TenantVO getRecentTenant() {
        return this.getRecentLoginTenant();
    }

    /**
     * 获取当前租户拥有的访问端列表
     *
     * @return
     */
    @GetMapping("/tenant/terminals")
    public List<TerminalVO> getTenantTerminals() {
        return terminalService.listTerminals();
    }

    /**
     * 获取登录用户有当前租户下拥有的访问端列表
     *
     * @return
     */
    @GetMapping("/context/user/terminals")
    public List<TerminalVO> getTerminals() {
        // 应用ID
        Set<Long> terminalIds = new HashSet<>();
        // 当前访问端
        String terminalCode = SessionContextHolder.getRequester().getTerminal();
        Terminal terminal = this.terminalService.getByCode(terminalCode);
        // 判断当前用户是否为租户管理员（tenantAdmin）,如果是租户管理员直接返回该产品下的所有应用
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        List<TerminalVO> terminals = terminalService.listTerminals();
        if (!isTenantAdmin) {
            // 通过用户角色和终端获取授权的应用
            var authorities = getUserRole();
            // 获取该平台下的角色列表
            List<Role> roles = this.roleService.list();
            Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
            for (var authority : authorities) {
                if (!StringUtils.isNumeric(authority)) {
                    continue;
                }
                List<RoleTerminal> roleTerminals = this.roleTerminalService.listByRole(Long.valueOf(authority));
                roleTerminals.forEach(roleTerminal -> {
                    if (roleIds.contains(roleTerminal.getRoleId())) {
                        terminalIds.add(roleTerminal.getTerminalId());
                    }
                });
            }
            terminals = ObjectUtils.copying(this.terminalService.listByIds(terminalIds), TerminalVO.class);
        }
        customConfigUtil.terminalCustomConfigResolving(terminals);
        return terminals;
    }

    /**
     * 获取登录用户在访问端下的角色授权应用
     *
     * @param
     *
     * @return
     */
    @GetMapping("/context/terminal-applications")
    public List<ApplicationPageVO> getAuthorizedTerminalApplications(@RequestParam(value = "keyword", required = false) String keyword) {
        // 应用ID
        Set<Long> applicationIds = new HashSet<>();
        // 当前访问端
        String terminalCode = SessionContextHolder.getRequester().getTerminal();
        Terminal terminal = this.terminalService.getByCode(terminalCode);
        // 判断当前用户是否为租户管理员（tenantAdmin）,如果是租户管理员直接返回该产品下的所有应用
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        //租户下产品应用
        List<TenantApplication> tenantApplications = this.tenantApplicationService.listByTenant(SessionContextHolder.getRequester()
                                                                                                        .getTenant());
        if (CollectionUtils.isEmpty(tenantApplications)) {
            return new ArrayList<>();
        }
        Set<Long> productAppIds = tenantApplications.stream().map(TenantApplication::getApplicationId).collect(Collectors.toSet());
        if (isTenantAdmin) {
            applicationIds.addAll(productAppIds);
        } else {
            // 通过用户角色和终端获取授权的应用
            var authorities = getUserRole();
            // 获取该平台下的角色列表
            List<Role> roles = this.roleService.list();
            Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
            for (var authority : authorities) {
                if (!StringUtils.isNumeric(authority)) {
                    continue;
                }
                List<RoleApplication> roleApplications = roleApplicationService.listByRoleTerminal(Long.valueOf(authority), terminal.getId());
                roleApplications.forEach(roleApplication -> {
                    if (roleIds.contains(roleApplication.getRoleId())) {
                        applicationIds.add(roleApplication.getApplicationId());
                    }
                });
            }
        }
        productAppIds.retainAll(applicationIds);
        if (CollectionUtils.isEmpty(productAppIds)) {
            return Collections.emptyList();
        }
        //取这个端下面的所有应用
        Set<Long> terminalApplicationIds = this.terminalApplicationService.list(Wrappers.<TerminalApplication>lambdaQuery()
                                                                                        .eq(TerminalApplication::getTerminalId, terminal.getId())
                                                                                        .in(TerminalApplication::getApplicationId, productAppIds))
                .stream()
                .map(TerminalApplication::getApplicationId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(terminalApplicationIds)) {
            return Collections.emptyList();
        }
        List<Application> applications = applicationService.listByIds(terminalApplicationIds, keyword);
        List<ApplicationPageVO> applicationPageVOS = ObjectUtils.copying(applications, ApplicationPageVO.class);
        customConfigUtil.applicationCustomConfigResolving(applicationPageVOS, true);
        this.setFile(terminal.getId(), applicationPageVOS);
        return applicationPageVOS;
    }

    /**
     * 获取当前用户得历史应用列表
     *
     * @return
     */
    @GetMapping("/context/terminal-history-applications")
    public List<ApplicationPageVO> getHistoryApplication() {
        //        // 当前访问端
        String terminalCode = SessionContextHolder.getRequester().getTerminal();
        Terminal terminal = this.terminalService.getByCode(terminalCode);
        List<ApplicationPageVO> authorizedTerminalApplications = this.getAuthorizedTerminalApplications(null);
        List<Long> terminalApplicationIds = authorizedTerminalApplications.stream().map(ta -> ta.getId()).collect(Collectors.toList());
        List<Application> applicationVisitHistories = this.applicationVisitHistoryService.getUserApplicationVisitHistory(SecurityContextHolder
                                                                                                                                 .getPrincipal()
                                                                                                                                 .getId(), terminal.getCode(), terminalApplicationIds);
        List<ApplicationPageVO> applicationPageVOS = ObjectUtils.copying(applicationVisitHistories, ApplicationPageVO.class);
        this.setFile(terminal.getId(), applicationPageVOS);
        customConfigUtil.applicationCustomConfigResolving(applicationPageVOS, true);
        return applicationPageVOS;
    }

    /**
     * 获取登录用户在访问端下的角色授权应用分组
     *
     * @return
     */
    @GetMapping("/context/terminal-application-groups")
    public List<ApplicationGroupPageVO> getAuthorizedApplicationGroups() {
        Set<Long> applicationIds = new HashSet<>();
        // 当前访问端
        String terminalCode = SessionContextHolder.getRequester().getTerminal();
        Terminal terminal = this.terminalService.getByCode(terminalCode);
        // 判断当前用户是否为租户管理员（tenantAdmin）,如果是租户管理员直接查询产品下的所有应用
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        List<TenantApplication> tenantApplications = this.tenantApplicationService.listByTenant(SessionContextHolder.getRequester()
                                                                                                        .getTenant());
        Set<Long> productAppIds = tenantApplications.stream().map(TenantApplication::getApplicationId).collect(Collectors.toSet());
        if (isTenantAdmin) {
            applicationIds.addAll(productAppIds);
        } else {
            // 通过用户角色和终端获取授权的应用
            var authorities = getUserRole();
            // 获取该平台下的角色列表
            List<Role> roles = this.roleService.list();
            Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
            var roleApplicationList = roleApplicationService.lambdaQuery().eq(RoleApplication::getTerminalId, terminal.getId())
                    .in(RoleApplication::getRoleId, authorities.stream().filter(StringUtils::isNumeric).map(Long::valueOf)
                            .collect(Collectors.toSet())).list();
            var map = Optional.ofNullable(roleApplicationList).orElse(CollectionUtils.emptyList()).stream()
                    .collect(Collectors.groupingBy(r -> String.valueOf(r.getTerminalId()) + r.getRoleId()));
            for (var authority : authorities) {
                if (!StringUtils.isNumeric(authority)) {
                    continue;
                }
                List<RoleApplication> roleApplications = map.get(terminal.getId() +
                                                                 authority);
                if (CollectionUtils.isEmpty(roleApplications)) {
                    continue;
                }
                roleApplications.forEach(roleApplication -> {
                    if (roleIds.contains(roleApplication.getRoleId())) {
                        applicationIds.add(roleApplication.getApplicationId());
                    }
                });
            }
        }
        //求交集
        productAppIds.retainAll(applicationIds);
        if (CollectionUtils.isEmpty(productAppIds)) {
            return Collections.emptyList();
        }
        //取这个端下面的所有应用
        List<Application> applications = getApplications(terminal, productAppIds);
        List<ApplicationPageVO> applicationPageVOS = ObjectUtils.copying(applications, ApplicationPageVO.class);
        this.setFile(terminal.getId(), applicationPageVOS);
        customConfigUtil.applicationCustomConfigResolving(applicationPageVOS, true);
        Map<Long, List<ApplicationPageVO>> applicationGroupMap = applicationPageVOS.stream()
                .filter(applicationPageVO -> Objects.nonNull(applicationPageVO.getApplicationGroupId()))
                .collect(Collectors.groupingBy(ApplicationPageVO::getApplicationGroupId));
        //查询应用分组
        List<ApplicationGroup> applicationGroups = applicationGroupService.listByIds(applicationGroupMap.keySet());
        List<ApplicationGroupPageVO> applicationGroupPageVOS = ObjectUtils.copying(applicationGroups, ApplicationGroupPageVO.class);
        applicationGroupPageVOS.stream().forEach(applicationGroupPageVO -> {
            if (applicationGroupMap.containsKey(applicationGroupPageVO.getId())) {
                applicationGroupPageVO.setApplicationPageVOS(applicationGroupMap.get(applicationGroupPageVO.getId()));
            }
        });
        return applicationGroupPageVOS;
    }

    /**
     * 获取登录用户在访问端下的角色授权应用分组
     *
     * @return
     */
    @GetMapping("/context/have-terminal-application")
    public Boolean getAuthorizedHaveApplication(@RequestParam String applicationCode) {
        Set<Long> applicationIds = new HashSet<>();
        // 当前访问端
        String terminalCode = SessionContextHolder.getRequester().getTerminal();
        Terminal terminal = this.terminalService.getByCode(terminalCode);
        // 判断当前用户是否为租户管理员（tenantAdmin）,如果是租户管理员直接查询产品下的所有应用
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        List<TenantApplication> tenantApplications = this.tenantApplicationService.listByTenant(SessionContextHolder.getRequester()
                                                                                                        .getTenant());
        Set<Long> productAppIds = tenantApplications.stream().map(TenantApplication::getApplicationId).collect(Collectors.toSet());
        if (isTenantAdmin) {
            applicationIds.addAll(productAppIds);
        } else {
            // 通过用户角色和终端获取授权的应用
            var authorities = getUserRole();
            // 获取该平台下的角色列表
            List<Role> roles = this.roleService.list();
            Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
            for (var authority : authorities) {
                if (!StringUtils.isNumeric(authority)) {
                    continue;
                }
                List<RoleApplication> roleApplications = roleApplicationService.listByRoleTerminal(Long.valueOf(authority), terminal.getId());
                roleApplications.forEach(roleApplication -> {
                    if (roleIds.contains(roleApplication.getRoleId())) {
                        applicationIds.add(roleApplication.getApplicationId());
                    }
                });
            }
        }
        //求交集
        productAppIds.retainAll(applicationIds);
        if (CollectionUtils.isEmpty(productAppIds)) {
            return false;
        }
        List<Application> applications = getApplications(terminal, productAppIds);
        return applications.stream().anyMatch(application -> applicationCode.equals(application.getCode()));
    }

    public List<Application> getApplications(Terminal terminal, Set<Long> productAppIds) {
        //取这个端下面的所有应用
        List<TerminalApplication> terminalApplications = this.terminalApplicationService.list(Wrappers.<TerminalApplication>lambdaQuery()
                                                                                                      .eq(TerminalApplication::getTerminalId, terminal.getId())
                                                                                                      .in(TerminalApplication::getApplicationId, productAppIds));
        Set<Long> terminalApplicationIds = terminalApplications.stream()
                .map(TerminalApplication::getApplicationId)
                .collect(Collectors.toSet());
        return applicationService.listByIds(terminalApplicationIds);
    }

    /**
     * 获取登录用户在访问端下的首页应用
     *
     * @return
     */
    @GetMapping("/context/terminal-home-applications")
    public List<ApplicationPageVO> getAuthorizedHomeApplications(@RequestParam(value = "keyword", required = false) String keyword) {
        List<HomeApplication> homeApplications = this.homeApplicationService.listHomeApplications();
        if (CollectionUtils.isEmpty(homeApplications)) {
            return Collections.emptyList();
        }
        Set<Long> applicationIds = new HashSet<>();
        // 当前访问端
        String terminalCode = SessionContextHolder.getRequester().getTerminal();
        Terminal terminal = this.terminalService.getByCode(terminalCode);
        // 判断当前用户是否为租户管理员（tenantAdmin）,如果是租户管理员直接返回查询产品下的所有应用
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        List<TenantApplication> tenantApplications = this.tenantApplicationService.listByTenant(SessionContextHolder.getRequester()
                                                                                                        .getTenant());
        Set<Long> productAppIds = tenantApplications.stream().map(TenantApplication::getApplicationId).collect(Collectors.toSet());
        if (isTenantAdmin) {
            applicationIds.addAll(productAppIds);
        } else {
            // 通过用户角色和终端获取授权的应用
            var authorities = getUserRole();
            // 获取该平台下的角色列表
            List<Role> roles = this.roleService.list();
            Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
            for (var authority : authorities) {
                if (!StringUtils.isNumeric(authority)) {
                    continue;
                }
                List<RoleApplication> roleApplications = roleApplicationService.listByRoleTerminal(Long.valueOf(authority), terminal.getId());
                roleApplications.forEach(roleApplication -> {
                    if (roleIds.contains(roleApplication.getRoleId())) {
                        applicationIds.add(roleApplication.getApplicationId());
                    }
                });
            }
        }
        productAppIds.retainAll(applicationIds);
        if (CollectionUtils.isEmpty(productAppIds)) {
            return Collections.emptyList();
        }
        //取这个端下面的所有应用
        List<TerminalApplication> terminalApplications = this.terminalApplicationService.list(Wrappers.<TerminalApplication>lambdaQuery()
                                                                                                      .eq(TerminalApplication::getTerminalId, terminal.getId())
                                                                                                      .in(TerminalApplication::getApplicationId, productAppIds));
        Set<Long> terminalApplicationIds = terminalApplications.stream()
                .map(TerminalApplication::getApplicationId)
                .collect(Collectors.toSet());
        Set<Long> homeApplicationIds = homeApplications.stream()
                .filter(homeApplication -> terminalApplicationIds.contains(homeApplication.getApplicationId()))
                .map(HomeApplication::getApplicationId).collect(Collectors.toSet());
        List<Application> applications = applicationService.listByIds(homeApplicationIds, keyword);
        List<ApplicationPageVO> applicationPageVOS = ObjectUtils.copying(applications, ApplicationPageVO.class);
        customConfigUtil.applicationCustomConfigResolving(applicationPageVOS, true);
        this.extendHomeApplication(homeApplications, applicationPageVOS, terminalApplications);
        return applicationPageVOS.stream().sorted(Comparator.comparing(
                x -> Optional.ofNullable(((ApplicationPageVO) x).getLastVisitTime()).map(DateUtils::toTimestamp).orElse(0L),
                Comparator.naturalOrder()).reversed()).collect(Collectors.toList());
    }

    /**
     * 获取登录用户在访问端下的首页卡片
     *
     * @return
     */
    @GetMapping("/context/terminal-home-cards")
    public List<CardVO> getAuthorizedHomeApplicationCards() {
        //获取用户在当前访问端下配置的首页卡片
        List<HomeApplicationCard> homeApplicationCards = this.homeApplicationCardService.listHomeApplicationCards();
        if (CollectionUtils.isEmpty(homeApplicationCards)) {
            return Collections.emptyList();
        }
        Terminal terminal = this.terminalService.getByCode(SessionContextHolder.getRequester().getTerminal());
        //获取用户在当前访问端下有权限访问的应用列表
        Set<Long> applicationIds = new HashSet<>();
        // 判断当前用户是否为租户管理员（tenantAdmin）,如果是租户管理员直接返回查询产品下的所有应用
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        List<TenantApplication> tenantApplications = this.tenantApplicationService.listByTenant(SessionContextHolder.getRequester()
                                                                                                        .getTenant());
        Set<Long> productAppIds = tenantApplications.stream().map(TenantApplication::getApplicationId).collect(Collectors.toSet());
        if (isTenantAdmin) {
            applicationIds.addAll(productAppIds);
        } else {
            // 通过用户角色和终端获取授权的应用
            var authorities = getUserRole();
            // 获取该平台下的角色列表
            List<Role> roles = this.roleService.list();
            Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
            for (var authority : authorities) {
                if (!StringUtils.isNumeric(authority)) {
                    continue;
                }
                List<RoleApplication> roleApplications = roleApplicationService.listByRoleTerminal(Long.valueOf(authority), terminal.getId());
                roleApplications.forEach(roleApplication -> {
                    if (roleIds.contains(roleApplication.getRoleId())) {
                        applicationIds.add(roleApplication.getApplicationId());
                    }
                });
            }
        }
        productAppIds.retainAll(applicationIds);
        if (CollectionUtils.isEmpty(applicationIds)) {
            return Collections.emptyList();
        }
        //取这个端下面的所有应用
        Set<Long> terminalApplicationIds = this.terminalApplicationService.list(Wrappers.<TerminalApplication>lambdaQuery()
                                                                                        .eq(TerminalApplication::getTerminalId, terminal.getId())
                                                                                        .in(TerminalApplication::getApplicationId, productAppIds))
                .stream()
                .map(TerminalApplication::getApplicationId)
                .collect(Collectors.toSet());
        //获取有权限访问的应用卡片
        List<Card> cards = this.cardService.listByTerminalApplicationIds(terminal.getId(), null, terminalApplicationIds);
        Set<String> cardCodes = cards.stream().map(card -> card.getCode()).collect(Collectors.toSet());
        List<HomeApplicationCard> homeCards = homeApplicationCards.stream()
                .filter(homeApplicationCard -> cardCodes.contains(homeApplicationCard.getCardCode())).collect(Collectors.toList());
        Set<String> homeCardCodes = homeCards.stream().map(homeApplicationCard -> homeApplicationCard.getCardCode())
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(homeCardCodes)) {
            return Collections.emptyList();
        }
        //获取有权限的卡片信息
        Map<String, Card> cardMap = this.cardService.list(Wrappers.<Card>lambdaQuery().in(Card::getCode, homeCardCodes)).stream()
                .collect(Collectors.toMap(Card::getCode, card -> card, (o, n) -> n));
        List<CardVO> cardVOS = new ArrayList<>();
        homeCards.stream().forEach(homeCard -> {
            Card card = cardMap.get(homeCard.getCardCode());
            if (Objects.nonNull(card)) {
                CardVO cardVO = CardVO.builder()
                        .applicationCode(homeCard.getApplicationCode())
                        .applicationId(homeCard.getApplicationId())
                        .cardSize(homeCard.getCardSize())
                        .code(homeCard.getCardCode())
                        .sort(homeCard.getSort())
                        .name(card.getName())
                        .build();
                cardVOS.add(cardVO);
            }
        });
        return cardVOS;
    }

    /**
     * 获取登录用户在访问端下可配置的卡片列表
     *
     * @return
     */
    @GetMapping("/context/terminal-cards")
    public List<CardVO> getAuthorizedTerminalApplicationCards(@RequestParam(value = "keyword", required = false) String keyword) {
        Terminal terminal = this.terminalService.getByCode(SessionContextHolder.getRequester().getTerminal());
        //获取用户在当前访问端下有权限访问的应用列表
        Set<Long> applicationIds = new HashSet<>();
        // 判断当前用户是否为租户管理员（tenantAdmin）,如果是租户管理员直接返回查询产品下的所有应用
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        List<TenantApplication> tenantApplications = this.tenantApplicationService.listByTenant(SessionContextHolder.getRequester()
                                                                                                        .getTenant());
        Set<Long> productAppIds = tenantApplications.stream().map(TenantApplication::getApplicationId).collect(Collectors.toSet());
        if (isTenantAdmin) {
            applicationIds.addAll(productAppIds);
        } else {
            // 通过用户角色和终端获取授权的应用
            var authorities = getUserRole();
            // 获取该平台下的角色列表
            List<Role> roles = this.roleService.list();
            Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
            for (var authority : authorities) {
                if (!StringUtils.isNumeric(authority)) {
                    continue;
                }
                List<RoleApplication> roleApplications = roleApplicationService.listByRoleTerminal(Long.valueOf(authority), terminal.getId());
                roleApplications.forEach(roleApplication -> {
                    if (roleIds.contains(roleApplication.getRoleId())) {
                        applicationIds.add(roleApplication.getApplicationId());
                    }
                });
            }
        }
        productAppIds.retainAll(applicationIds);
        if (CollectionUtils.isEmpty(applicationIds)) {
            return Collections.emptyList();
        }
        //取这个端下面的所有应用
        Set<Long> terminalApplicationIds = this.terminalApplicationService.list(Wrappers.<TerminalApplication>lambdaQuery()
                                                                                        .eq(TerminalApplication::getTerminalId, terminal.getId())
                                                                                        .in(TerminalApplication::getApplicationId, productAppIds))
                .stream()
                .map(TerminalApplication::getApplicationId)
                .collect(Collectors.toSet());
        //获取有权限访问的应用卡片
        List<Card> cards = this.cardService.listByTerminalApplicationIds(terminal.getId(), keyword, terminalApplicationIds);
        List<CardVO> cardVOS = ObjectUtils.copying(cards, CardVO.class);
        //获取用户在当前访问端下配置的首页卡片
        List<HomeApplicationCard> homeApplicationCards = this.homeApplicationCardService.listHomeApplicationCards();
        if (CollectionUtils.isEmpty(homeApplicationCards)) {
            return cardVOS;
        }
        Set<String> homeCards = homeApplicationCards.stream().map(homeApplicationCard -> homeApplicationCard.getCardCode())
                .collect(Collectors.toSet());
        return cardVOS.stream().filter(cardVO -> !homeCards.contains(cardVO.getCode())).collect(Collectors.toList());
    }

    @GetMapping("/context/application/{code}")
    public ApplicationVO getById(@PathVariable("code") String code) {
        Application application = applicationService.getByCode(code);
        Assert.isNotFound(application, "不存在应用信息");
        ApplicationVO applicationVO = ObjectUtils.copying(application, ApplicationVO.class);
        ApplicationGroup applicationGroup = this.applicationGroupService.getById(applicationVO.getApplicationGroupId());
        if (Objects.nonNull(applicationGroup)) {
            applicationVO.setApplicationGroupName(applicationGroup.getName());
        }
        return applicationVO;
    }

    /**
     * 获取登录用户在访问端下可配置的卡片列表(按应用展示)
     *
     * @return
     */
    @GetMapping("/context/terminal-application-cards")
    public List<ApplicationCardVO> getAuthorizedTerminalApplicationCardsGroups() {
        Terminal terminal = this.terminalService.getByCode(SessionContextHolder.getRequester().getTerminal());
        //获取用户在当前访问端下有权限访问的应用列表
        Set<Long> applicationIds = new HashSet<>();
        // 判断当前用户是否为租户管理员（tenantAdmin）,如果是租户管理员直接返回查询产品下的所有应用
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        List<TenantApplication> tenantApplications = this.tenantApplicationService.listByTenant(SessionContextHolder.getRequester()
                                                                                                        .getTenant());
        Set<Long> productAppIds = tenantApplications.stream().map(TenantApplication::getApplicationId).collect(Collectors.toSet());
        if (isTenantAdmin) {
            applicationIds.addAll(productAppIds);
        } else {
            // 通过用户角色和终端获取授权的应用
            var authorities = getUserRole();
            // 获取该平台下的角色列表
            List<Role> roles = this.roleService.list();
            Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
            for (var authority : authorities) {
                if (!StringUtils.isNumeric(authority)) {
                    continue;
                }
                List<RoleApplication> roleApplications = roleApplicationService.listByRoleTerminal(Long.valueOf(authority), terminal.getId());
                roleApplications.forEach(roleApplication -> {
                    if (roleIds.contains(roleApplication.getRoleId())) {
                        applicationIds.add(roleApplication.getApplicationId());
                    }
                });
            }
        }
        productAppIds.retainAll(applicationIds);
        if (CollectionUtils.isEmpty(applicationIds)) {
            return Collections.emptyList();
        }
        //取这个端下面的所有应用
        Set<Long> terminalApplicationIds = this.terminalApplicationService.list(Wrappers.<TerminalApplication>lambdaQuery()
                                                                                        .eq(TerminalApplication::getTerminalId, terminal.getId())
                                                                                        .in(TerminalApplication::getApplicationId, productAppIds))
                .stream()
                .map(TerminalApplication::getApplicationId)
                .collect(Collectors.toSet());
        //获取有权限访问的应用卡片按应用分组
        List<Card> cards = this.cardService.listByTerminalApplicationIds(terminal.getId(), null, terminalApplicationIds);
        //获取用户在当前访问端下配置的首页卡片
        List<HomeApplicationCard> homeApplicationCards = this.homeApplicationCardService.listHomeApplicationCards();
        if (!CollectionUtils.isEmpty(homeApplicationCards)) {
            //只保留还未被用户配置的卡片
            Set<String> homeCards = homeApplicationCards.stream().map(homeApplicationCard -> homeApplicationCard.getCardCode())
                    .collect(Collectors.toSet());
            cards = cards.stream().filter(cardVO -> !homeCards.contains(cardVO.getCode())).collect(Collectors.toList());
        }
        Map<Long, List<Card>> cardMaps = cards.stream().collect(Collectors.groupingBy(Card::getApplicationId));
        Set<Long> cardApplicationIds = cardMaps.keySet();
        List<Application> applications = this.applicationService.listByIds(cardApplicationIds);
        List<ApplicationCardVO> applicationCardVOS = ObjectUtils.copying(applications, ApplicationCardVO.class);
        applicationCardVOS.stream().forEach(applicationCardVO -> {
            applicationCardVO.setCardVOS(ObjectUtils.copying(cardMaps.get(applicationCardVO.getId()), CardVO.class));
        });
        return applicationCardVOS;
    }

    /**
     * 获取登录用户在访问端应用下的角色授权菜单
     *
     * @param applicationCode
     *
     * @return
     */
    @GetMapping("/context/terminal-application-menus")
    public List<MenuVO> getAuthorizedTerminalApplicationMenus(@RequestParam("applicationCode") String applicationCode,
                                                              @RequestParam(value = "alisaFlag", defaultValue = "true") boolean alisaFlag
                                                             ) {
        Application application = this.applicationService.getByCode(applicationCode);
        Assert.isNotFound(application, "应用不存在或已被删除");
        Map<Long, String> applicationMap = new HashMap<>();
        applicationMap.put(application.getId(), application.getCode());
        // 应用ID
        Set<Long> menuIds = new HashSet<>();
        // 当前访问端
        String terminalCode = SessionContextHolder.getRequester().getTerminal();
        Terminal terminal = this.terminalService.getByCode(terminalCode);
        // 判断是否为租户管理员，如果为租户管理员则直接返回产品应用下的所有菜单
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        if (isTenantAdmin) {
            List<Menu> menus = this.menuService.listByTerminalApplication(terminal.getId(), application.getId()).stream()
                    .filter(menu -> menu.getEnabled()).collect(Collectors.toList());
            List<MenuVO> menuVOList = ObjectUtils.copying(menus, MenuVO.class);
            //菜单主键
            List<Long> menuIdList = menuVOList.stream().map(MenuVO::getId).collect(Collectors.toList());
            BusinessKeysParam param = new BusinessKeysParam();
            param.setBusinessKeys(menuIdList);
            menuVOList.stream().forEach(menuVO -> {
                menuVO.setApplicationCode(application.getCode());
                menuVO.setApplicationName(application.getName());
                this.extendMenuFunctionAndDataPermission(null, menuVO, terminal.getId(), application.getId());
            });
            this.applicationVisitHistoryService.save(application.getCode(), application.getName());
            customConfigUtil.menuCustomConfigResolving(applicationMap, menuVOList, alisaFlag);
            return TreeUtils.assembleTrees(menuVOList);
        }
        // 通过用户角色和终端和应用获取授权的应用
        var authorities = getUserRole();
        List<Long> roleIds = new ArrayList<>();
        for (var authority : authorities) {
            if (!StringUtils.isNumeric(authority)) {
                continue;
            } else {
                roleIds.add(Long.valueOf(authority));
            }
            List<RoleMenu> roleMenus = roleMenuService.listByRoleTerminalApplication(Long.valueOf(authority), terminal.getId(), application.getId());
            roleMenus.forEach(roleMenu -> {
                menuIds.add(roleMenu.getMenuId());
            });
        }
        // 转换为应用值对象
        List<Menu> menus = menuService.listByIds(menuIds).stream().filter(menu -> menu.getEnabled()).collect(Collectors.toList());
        List<MenuVO> menuVOList = ObjectUtils.copying(menus, MenuVO.class);
        if (CollectionUtils.isEmpty(menuVOList)) {
            return Collections.emptyList();
        }
        //菜单主键
        List<Long> menuIdList = menuVOList.stream().map(MenuVO::getId).collect(Collectors.toList());
        BusinessKeysParam param = new BusinessKeysParam();
        param.setBusinessKeys(menuIdList);
        menuVOList.stream().forEach(menuVO -> {
            menuVO.setApplicationCode(application.getCode());
            menuVO.setApplicationName(application.getName());
            this.extendMenuFunctionAndDataPermission(roleIds, menuVO, terminal.getId(), application.getId());
        });
        this.applicationVisitHistoryService.save(application.getCode(), application.getName());
        customConfigUtil.menuCustomConfigResolving(applicationMap, menuVOList, alisaFlag);
        return TreeUtils.assembleTrees(menuVOList);
    }

    /**
     * 返回扩展的功能和数据权限
     *
     * @param menuVO
     * @param terminalId
     * @param applicationId
     */
    private void extendMenuFunctionAndDataPermission(List<Long> roleIds, MenuVO menuVO, Long terminalId, Long applicationId) {
        //查询菜单下的所有功能按钮
        List<MenuFunctionVO> functionUnderMenu = menuFunctionService.getFunctionUnderMenu(menuVO.getId());
        if (CollectionUtils.isEmpty(roleIds)) {
            //角色为空，说明是管理员  默认菜单下的所有功能按钮
            menuVO.setFunctions(functionUnderMenu);
        } else {
            //带角色的人员
            //设置菜单下的功能按钮
            List<MenuFunctionVO> roleMenuFunctions = roleMenuFunctionService.getRoleMenuFunctions(roleIds, terminalId, applicationId, menuVO.getId());
            menuVO.setFunctions(roleMenuFunctions);
            //设置菜单下的数据权限
            List<DataPermissionVO> roleMenuDataPermission = roleMenuDataPermissionService.getRoleMenuDataPermission(roleIds, terminalId, applicationId, menuVO.getId());
            //如果有多个角色，需要排序，找到最大的权限
            //须要结合UserDetailsProvider中对自定义部门的处理，多个角色的自定义部门会合并
            if (!CollectionUtils.isEmpty(roleMenuDataPermission) && roleMenuDataPermission.size() > 1) {
                DataPermissionVO dataPermissionVO = roleMenuDataPermission.stream()
                        .sorted(Comparator.comparing(DataPermissionVO::getSortNo).reversed())
                        .findFirst().orElse(null);
                roleMenuDataPermission = dataPermissionVO != null ? Arrays.asList(dataPermissionVO) : null;
            }
            menuVO.setDataPermissions(roleMenuDataPermission);
        }
    }

    /**
     * 更新用户最近登录租户信息
     *
     * @param tenant 租户编号
     */
    @PutMapping("/tenant/{tenant}/recent-login")
    public void updateRecentLoginTenant(@PathVariable("tenant") Integer tenant) {
        this.tenantService.updateRecentLoginTenant(tenant);
    }

    /**
     * 更新用户租户主企业信息
     *
     * @param tenant 租户编号
     */
    @PutMapping("/tenant/{tenant}/main-enterprise")
    public void updateMainEnterpriseTenant(@PathVariable("tenant") Integer tenant) {
        this.tenantService.updateMainEnterpriseTenant(tenant);
    }

    /**
     * 新增用户租户关联关系
     *
     * @param tenant 租户编号
     */
    @PostMapping("/user-tenant/{tenant}/{userId}")
    public void addUserTenant(@PathVariable("tenant") Integer tenant, @PathVariable("userId") Long userId) {
        this.userTenantService.addUserTenant(tenant, userId);
    }

    /**
     * 删除用户租户关联关系
     *
     * @param tenant 租户编号
     * @param userId 用户主键
     */
    @DeleteMapping("/user-tenant/{tenant}/{userId}")
    public void removeUserTenant(@PathVariable("tenant") Integer tenant, @PathVariable("userId") Long userId) {

    }

    /**
     * 用户个人信息修改
     *
     * @param param
     */
    @PutMapping("/user/personal")
    public void updateUserPersonal(@RequestBody @Valid UserPersonalUpdateParam param) {
        this.userService.updateUserPersonal(param);
    }

    /**
     * 重置用户密码
     *
     * @param id    用户主键
     * @param param 密码修改参数
     */
    @PutMapping("/user/{id}/new-password")
    public void updatePassword(@PathVariable("id") Long id, @RequestBody @Valid PasswordUpdateParam param) {
        this.userService.updatePassword(id, param);
    }

    @PutMapping("/user/{id}/password/reset")
    public void resetPassword(@PathVariable("id") Long id, @RequestBody @Valid ResetPasswordParam resetPasswordParam) {
        this.userService.resetPassword(id, resetPasswordParam.getNewPassword());
    }

    private void setFile(Long terminal, List<ApplicationPageVO> applicationPageVOS) {
        //终端下的所有应用
        Map<Long, List<TerminalApplication>> terminalApplications = this.terminalApplicationService.listByTerminal(terminal).stream()
                .collect(Collectors.groupingBy(TerminalApplication::getApplicationId));
        applicationPageVOS.forEach(applicationPageVO -> {
            if (terminalApplications.containsKey(applicationPageVO.getId())) {
                TerminalApplication terminalApplication = terminalApplications.get(applicationPageVO.getId()).get(0);
                applicationPageVO.setPath(terminalApplication.getPath());
            }
        });

    }

    private void extendHomeApplication(List<HomeApplication> homeApplications, List<ApplicationPageVO> applicationPageVOS,
                                       List<TerminalApplication> terminalApplications
                                      ) {
        //查询历史应用
        Set<Long> homeAppIds = applicationPageVOS.stream().map(ApplicationPageVO::getId).collect(Collectors.toSet());
        Map<String, ApplicationVisitHistory> applicationVisitHistoryMap = this.applicationVisitHistoryService
                .getUserApplicationVisitHistories(SecurityContextHolder.getPrincipal().getId(),
                                                  SessionContextHolder.getRequester().getTerminal(),
                                                  homeAppIds)
                .stream()
                .collect(Collectors.toMap(ApplicationVisitHistory::getApplicationCode, applicationVisitHistory -> applicationVisitHistory, (o, n) -> n));
        //终端下的所有应用
        Map<Long, List<TerminalApplication>> terminalApplicationMap = terminalApplications.stream()
                .collect(Collectors.groupingBy(TerminalApplication::getApplicationId));
        Map<Long, HomeApplication> homeApplicationMap = homeApplications.stream()
                .collect(Collectors.toMap(HomeApplication::getApplicationId, m -> m, (o, n) -> n));
        applicationPageVOS.forEach(applicationPageVO -> {
            HomeApplication homeApplication = homeApplicationMap.get(applicationPageVO.getId());
            if (Objects.nonNull(homeApplication)) {
                applicationPageVO.setSort(homeApplication.getSort());
            }
            if (applicationVisitHistoryMap.containsKey(applicationPageVO.getCode())) {
                ApplicationVisitHistory applicationVisitHistory = applicationVisitHistoryMap.get(applicationPageVO.getCode());
                applicationPageVO.setLastVisitTime(applicationVisitHistory.getVisitTime());
            }
            if (terminalApplicationMap.containsKey(applicationPageVO.getId())) {
                TerminalApplication terminalApplication = terminalApplicationMap.get(applicationPageVO.getId()).get(0);
                applicationPageVO.setPath(terminalApplication.getPath());
            }
        });
    }

    /**
     * @Description 根据路径判断是否有权限
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @GetMapping("/role-menus-by-path")
    public ApplicationVO getRoleMenusByPath(RoleMenusByPathParam param) {
        return menuService.getRoleMenus(param.getPath());
    }

    /**
     * 根据用户ID 查询用户在某个端下分配的应用
     *
     * @param userId
     *
     * @return
     */
    @GetMapping("/user/{id}/applications")
    public List<ApplicationVO> getApplicationsByUserId(@PathVariable("id") Long userId) {
        //查询用户的所有角色
        List<UserRole> userRoles = this.userRoleService.list(Wrappers.<UserRole>lambdaQuery()
                                                                     .eq(UserRole::getUserId, userId));
        if (!CollectionUtils.isEmpty(userRoles)) {
            List<Long> roleIds = userRoles.stream().map(r -> r.getRoleId()).collect(Collectors.toList());
            //通过角色和端 查询应用
            String terminalCode = SessionContextHolder.getRequester().getTerminal();
            Terminal terminal = this.terminalService.getByCode(terminalCode);
            List<RoleApplication> roleApplications = this.roleApplicationService.list(Wrappers.<RoleApplication>lambdaQuery()
                                                                                              .in(RoleApplication::getRoleId, roleIds)
                                                                                              .eq(RoleApplication::getTerminalId, terminal.getId()));
            //查询应用
            if (!CollectionUtils.isEmpty(roleApplications)) {
                List<Long> applicationIds = roleApplications.stream().map(a -> a.getApplicationId()).collect(Collectors.toList());
                List<Application> applications = this.applicationService.listByIds(applicationIds);
                return ObjectUtils.copying(applications, ApplicationVO.class);
            }
        }
        return Collections.emptyList();
    }

    /**
     * 获取登录用户在访问端下的角色授权应用菜单
     *
     * @param
     *
     * @return
     */
    @GetMapping("/context/terminal-applications/menus")
    public List<ApplicationPageVO> getAuthorizedTerminalApplications() {
        List<ApplicationPageVO> applications = this.getAuthorizedTerminalApplications(StringUtils.EMPTY_STRING);
        // 应用ID
        Set<Long> menuIds = new HashSet<>();
        // 当前访问端
        String terminalCode = SessionContextHolder.getRequester().getTerminal();
        Terminal terminal = this.terminalService.getByCode(terminalCode);
        // 判断是否为租户管理员，如果为租户管理员则直接返回产品应用下的所有菜单
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        List<MenuVO> menuVOList;
        if (isTenantAdmin) {
            List<Menu> menus = this.menuService.listByTerminal(terminal.getId()).stream().filter(menu -> menu.getEnabled())
                    .collect(Collectors.toList());
            menuVOList = ObjectUtils.copying(menus, MenuVO.class);
        } else {
            // 通过用户角色和终端和应用获取授权的应用
            var authorities = getUserRole();
            List<Long> roleIds = new ArrayList<>();
            for (var authority : authorities) {
                if (!StringUtils.isNumeric(authority)) {
                    continue;
                } else {
                    roleIds.add(Long.valueOf(authority));
                }
                List<RoleMenu> roleMenus = roleMenuService.listByRoleTerminalApplication(Long.valueOf(authority), terminal.getId());
                roleMenus.forEach(roleMenu -> {
                    menuIds.add(roleMenu.getMenuId());
                });
            }
            // 转换为应用值对象
            List<Menu> menus = menuService.listByIds(menuIds).stream().filter(menu -> menu.getEnabled()).collect(Collectors.toList());
            menuVOList = ObjectUtils.copying(menus, MenuVO.class);
        }
        if (CollectionUtils.isEmpty(menuVOList)) {
            return Collections.emptyList();
        }
        Map<Long, String> applicationMap = applications.stream()
                .collect(Collectors.toMap(ApplicationPageVO::getId, ApplicationPageVO::getCode));
        customConfigUtil.menuCustomConfigResolving(applicationMap, menuVOList, true);
        applications.stream().forEach(applicationPageVO -> {
            List<MenuVO> menuVOS = menuVOList.stream().filter(menuVO -> applicationPageVO.getId().equals(menuVO.getApplicationId()))
                    .map(menuVO -> {
                        menuVO.setApplicationCode(applicationPageVO.getCode());
                        menuVO.setApplicationName(applicationPageVO.getName());
                        return menuVO;
                    }).collect(Collectors.toList());
            applicationPageVO.setMenuVOS(TreeUtils.assembleTrees(menuVOS));
        });
        return applications;
    }

    /**
     * 保存应用访问历史
     *
     * @param applicationCode
     */
    @PostMapping("/context/application/visit/history/save")
    public void saveApplicationVisitHistory(@RequestParam("applicationCode") String applicationCode) {
        Application application = this.applicationService.getByCode(applicationCode);
        Assert.isNotFound(application, "应用不存在或已被删除");
        applicationVisitHistoryService.save(applicationCode, application.getName());
    }

    /**
     * 获取当前应用分组下的菜单
     *
     * @param id 应用分组id
     */
    @GetMapping("/context/application/group/menus")
    public List<ApplicationPageVO> getApplicationGroupMenus(@RequestParam("id") Long id) {

        List<Application> applicationList = applicationService.lambdaQuery()
                .eq(Application::getApplicationGroupId, id)
                .list();
        if (CollectionUtils.isEmpty(applicationList)) {
            return new ArrayList<>();
        }
        Set<Long> applicationIdByGroupId = applicationList.stream().map(Application::getId).collect(Collectors.toSet());
        Set<Long> applicationIds = new HashSet<>();
        // 当前访问端
        String terminalCode = SessionContextHolder.getRequester().getTerminal();
        Terminal terminal = this.terminalService.getByCode(terminalCode);
        // 判断当前用户是否为租户管理员（tenantAdmin）,如果是租户管理员直接查询产品下的所有应用
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        List<TenantApplication> tenantApplications = this.tenantApplicationService.listByTenantAndApplicationId(SessionContextHolder
                                                                                                                        .getRequester()
                                                                                                                        .getTenant(), applicationIdByGroupId);
        Set<Long> productAppIds = tenantApplications.stream().map(TenantApplication::getApplicationId).collect(Collectors.toSet());
        List<MenuVO> menuVOList;
        if (isTenantAdmin) {
            applicationIds.addAll(productAppIds);
            List<Menu> menus = this.menuService.lambdaQuery()
                    .eq(Menu::getTerminalId, terminal.getId())
                    .in(Menu::getApplicationId, applicationIds)
                    .orderByAsc(Menu::getSort).list();
            menuVOList = ObjectUtils.copying(menus, MenuVO.class);
        } else {
            // 通过用户角色和终端获取授权的应用
            var authorities = getUserRole();
            // 获取该平台下的角色列表
            List<Role> roles = this.roleService.list();
            Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
            Set<Long> authority = new HashSet<>();
            for (var grantedAuthority : authorities) {
                if (StringUtils.isNumeric(grantedAuthority)) {
                    authority.add(Long.valueOf(grantedAuthority));
                }
            }
            List<RoleMenu> roleMenus = roleMenuService.listByRoleTerminalApplicationBatch(authority, Collections.singletonList(terminal.getId()), productAppIds);
            Set<Long> menuIds = new HashSet<>();
            roleMenus.forEach(roleMenu -> {
                if (roleIds.contains(roleMenu.getRoleId())) {
                    applicationIds.add(roleMenu.getApplicationId());
                    menuIds.add(roleMenu.getMenuId());
                }
            });
            // 转换为应用值对象
            List<Menu> menus = menuService.listByIds(menuIds).stream().filter(Menu::getEnabled).collect(Collectors.toList());
            menuVOList = ObjectUtils.copying(menus, MenuVO.class);
        }
        //求交集
        productAppIds.retainAll(applicationIds);
        if (CollectionUtils.isEmpty(productAppIds)) {
            return Collections.emptyList();
        }
        //取这个端下面的所有应用
        List<Application> applications = applicationList.stream().filter(t -> productAppIds.contains(t.getId()))
                .collect(Collectors.toList());
        List<ApplicationPageVO> applicationPageVOS = ObjectUtils.copying(applications, ApplicationPageVO.class);
        this.setFile(terminal.getId(), applicationPageVOS);
        customConfigUtil.applicationCustomConfigResolving(applicationPageVOS, true);
        //菜单解析
        Map<Long, String> applicationMap = applications.stream().collect(Collectors.toMap(Application::getId, Application::getCode));
        customConfigUtil.menuCustomConfigResolving(applicationMap, menuVOList, true);
        applicationPageVOS.forEach(applicationPageVO -> {
            List<MenuVO> menuVOS = menuVOList.stream().filter(menuVO ->
                                                                      menuVO.getEnabled() &&
                                                                      applicationPageVO.getId().equals(menuVO.getApplicationId()))
                    .peek(menuVO -> {
                        menuVO.setApplicationCode(applicationPageVO.getCode());
                        menuVO.setApplicationName(applicationPageVO.getName());
                    }).collect(Collectors.toList());
            applicationPageVO.setMenuVOS(TreeUtils.assembleTrees(menuVOS));
        });

        return applicationPageVOS;
    }

    private Set<String> getUserRole() {
        return permissionCache.getPrincipalPermissions("ROLE")
                .stream().map(PrincipalMethodPermission::getCode).collect(Collectors.toSet());
    }

}
