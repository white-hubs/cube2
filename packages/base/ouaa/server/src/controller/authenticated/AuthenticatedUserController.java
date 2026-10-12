package com.guhecloud.cube.biz.ouaa.server.controller.authenticated;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.metadata.param.UserRuleConditionParam;
import com.guhecloud.cube.biz.ouaa.server.converters.UserConverterForUserVO;
import com.guhecloud.cube.biz.ouaa.server.dao.UserMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.AppointmentDetailVO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserOrganizationDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserTenantDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserOrganization;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTel;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.entity.UserWechat;
import com.guhecloud.cube.biz.ouaa.server.param.UserBelongsInstitutionParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.param.query.UserMapQuery;
import com.guhecloud.cube.biz.ouaa.server.param.query.UserQuery;
import com.guhecloud.cube.biz.ouaa.server.param.query.UsersByRolesQuery;
import com.guhecloud.cube.biz.ouaa.server.param.validated.PasswordValidated;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.UserUpdateMessage;
import com.guhecloud.cube.biz.ouaa.server.service.AppointmentQueryService;
import com.guhecloud.cube.biz.ouaa.server.service.OrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.PositionService;
import com.guhecloud.cube.biz.ouaa.server.service.UserOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTelService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.cube.biz.ouaa.server.service.UserWechatService;
import com.guhecloud.cube.biz.ouaa.server.utils.CollectionsUtils;
import com.guhecloud.cube.biz.ouaa.server.utils.PasswordUtils;
import com.guhecloud.cube.biz.ouaa.server.vo.UserPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.UnauthenticatedException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户管理接口
 *
 * @author Woody
 * @date 2021/7/4
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAuthenticated()")
public class AuthenticatedUserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    private final PositionService positionService;
    private final AppointmentQueryService appointmentQueryService;
    private final OrganizationService organizationService;
    private final UserOrganizationService userOrganizationService;
    private final UserTenantService userTenantService;
    private final UserMapper userMapper;
    private final FileClient fileClient;
    private final PasswordUtils passwordUtils;
    private final PasswordValidated passwordValidated;
    private final UserWechatService userWechatService;
    private final UserTelService userTelService;

    /**
     * 用户新增存在则返回用户信息
     *
     * @param param 新增参数
     *
     * @return 用户信息
     */
    @PostMapping("/authenticated/user/save")
    public UserDTO save(@RequestBody @Valid UserModifyParam param) {
        User userByMobile = this.userService.lambdaQuery().eq(User::getMobile, param.getMobile()).one();
        if (userByMobile != null) {
            return ObjectUtils.copying(userByMobile, UserDTO.class);
        }
        User user = this.userService.saveByAdmin(param);
        return ObjectUtils.copying(user, UserDTO.class);
    }

    @PostMapping("/authenticated/user-under-tenant")
    public Long saveByTenant(@RequestBody @Valid UserModifyParam param, Requester requester) {
        User user = ObjectUtils.copying(param, User.class);
        user.setId(param.getId() != null ? param.getId() : IdWorker.getId());
        user.setAdmin(false);
        if (StringUtils.isNotEmpty(param.getPassword())) {
            this.passwordValidated.validate(param.getPassword());
            user.setPassword(this.passwordEncoder.encode(param.getPassword()));
        } else {
            //初始密码
            user.setPassword(passwordUtils.getInitializePassword());
        }
        user.setUsername(param.getMobile());
        param.setTenant(requester.getTenant());
        this.userService.saveByTenant(user, param);
        return user.getId();
    }

    @PutMapping("/authenticated/user-under-tenant/{id}")
    public void updateByTenant(@PathVariable("id") Long id, @RequestBody @Valid UserModifyParam param) {
        User user = this.userService.updateNormalByTenant(id, param);
        if (param.getSendMessage()) {
            //用户信息被修改，通知相应的业务模块更新用户信息（例如：采购商和批发商）
            UserUpdateMessage.UserUpdateMessageBuilder builder = UserUpdateMessage.builder()
                    .id(id).name(param.getName()).mobile(param.getMobile()).gender(param.getGender()).positionIds(param.getPositionIds())
                    .enabled(param.getEnabled())
                    .photographKey(user.getPhotographKey())
                    .appointmentIds(param.getAppointmentIds())
                    .photographEdit(!param.getName().equals(param.getOldName()) &&
                                    !CollectionsUtils.eqLongList(param.getPhotographIdList(), param.getOldPhotographIdList()));
            if (CollectionUtils.isNotEmpty(param.getOrganizationIds())) {
                builder.organizationId(param.getOrganizationIds().get(0));
                builder.appointmentIds(param.getAppointmentIds());
            }

            if (param.getTenant() != null && param.getTenant() > 0) {
                builder.tenant(param.getTenant());
            } else if (SessionContextHolder.getRequester() != null) {
                builder.tenant(SessionContextHolder.getRequester().getTenant());
            }
            RocketMQContextHolder.send(UserUpdateMessage.TOPIC, builder.build());
        }
    }

    @PostMapping("/authenticated/user/page/post")
    public Pagination<UserPageVO> postByPage(@RequestBody @Valid UserQueryParam param) {
        return this.getByPage(param);
    }

    @GetMapping("/authenticated/user/page")
    public Pagination<UserPageVO> getByPage(@Valid UserQueryParam param) {
        Page<User> page = this.userService.getByPage(param);
        List<UserPageVO> users = ObjectUtils.copying(page.getRecords(), UserPageVO.class);
        users.forEach(s -> {
            Map<String, String> remap = userService.getUserOrganizationAndPostName(s.getId());
            s.setUserOrganizationName(remap.get("userOrganizationName"));
            s.setUserRoleName(remap.get("userPostName"));
        });
        return PagingUtils.convert(page, users);
    }

    @GetMapping("/authenticated/user/{id}")
    public UserVO getById(@PathVariable(value = "id") Long id) {
        UserVO user = ObjectUtils.copying(this.userService.getById(id), UserVO.class);
        if (Objects.nonNull(user)) {
            // 用户租户
            List<UserTenant> tenants = userTenantService.listUserTenants(id);
            if (ObjectUtils.isNotEmpty(tenants)) {
                user.setTenants(tenants);
            }
            // 获取用户相关组织机构和岗位及角色
            Set<Long> userIds = Collections.singleton(user.getId());
            List<UserOrganizationDTO> userOrganizations = this.userOrganizationService.listUserOrganizations(userIds);
            Set<Long> positionIds =
                    userOrganizations.stream().map(UserOrganizationDTO::getPositionId).collect(Collectors.toSet());
            Map<Long, Position> positionMappings = this.positionService.listPositions(positionIds).stream()
                    .collect(Collectors.toMap(Position::getId, position -> position, (o, n) -> n));
            Map<Long, List<UserOrganizationDTO>> userOrganizationGroups = userOrganizations.stream()
                    .collect(Collectors.groupingBy(UserOrganizationDTO::getUserId));
            // 设置用户组织机构
            List<UserOrganizationDTO> organizations =
                    ObjectUtils.ifNull(userOrganizationGroups.get(user.getId()), Collections.emptyList());
            UserOrganizationDTO organization = organizations.stream().findAny().orElse(null);
            if (organization != null) {
                List<Long> ids = Arrays.stream(organization.getSequence().split("-")).map(Long::parseLong).collect(Collectors.toList());
                List<Organization> organizationList = this.organizationService.listOrganizations(ids);
                user.setOrganizations(ObjectUtils.copying(organizationList, UserVO.Organization.class));
                user.setOrganization(user.getOrganizations().get(organizationList.size() - 1));
            } else {
                user.setOrganizations(Collections.emptyList());
            }
            // 设置用户岗位列表
            List<UserVO.Position> positions = organizations.stream().map(o ->
                                                                                 ObjectUtils.copying(positionMappings.get(o.getPositionId()), UserVO.Position.class))
                    .filter(Objects::nonNull).collect(Collectors.toList());
            user.setPositions(positions);
            // 设置用户角色列表
            List<UserRoleDTO> roles = this.userRoleService.listUserRoles(userIds);
            user.setRoles(ObjectUtils.copying(roles, UserVO.Role.class));
            // 获取用户照片
            if (user.getPhotographKey() != null && user.getPhotographKey() > 0) {
                user.setPhotographList(fileClient.getByBusinessKey(user.getPhotographKey()));
            }
        }
        return user;
    }

    @PutMapping("/authenticated/user")
    public void updateUser(@RequestBody @Valid UserUpdateParam param) {
        this.userService.updateByAdmin(param);
        //用户信息被修改，通知相应的业务模块更新用户信息（例如：采购商和批发商）
        RocketMQContextHolder.send(UserUpdateMessage.TOPIC, UserUpdateMessage.builder()
                .id(param.getId()).name(param.getName()).mobile(param.getMobile())
                .tenant(SessionContextHolder.getRequester().getTenant()).build());
    }

    /**
     * 岗位下用户(内部调用)
     *
     * @param positionId 岗位主键
     *
     * @return
     */
    @GetMapping("/authenticated/position-users/{positionId}")
    public List<UserDTO> positionUsers(@PathVariable("positionId") Long positionId) {
        return this.userService.positionUsers(positionId);
    }

    /**
     * 组织架构下用户(内部调用)
     *
     * @param organizationId 组织架构主键
     *
     * @return
     */
    @GetMapping("/authenticated/organization-users/{organizationId}")
    public List<UserDTO> organizationUsers(@PathVariable("organizationId") Long organizationId) {
        return this.userService.organizationUsers(organizationId);
    }

    /**
     * 组织架构下启用用户(内部调用)
     *
     * @param organizationId 组织架构主键
     *
     * @return
     */
    @GetMapping("/authenticated/organization-enable-users/{organizationId}")
    public List<UserDTO> organizationEnableUsers(@PathVariable("organizationId") Long organizationId) {
        List<UserDTO> userDTOS = this.userService.organizationUsers(organizationId);
        if (CollectionUtils.isEmpty(userDTOS)) {
            return Collections.emptyList();
        }
        Set<Long> userIds = userDTOS.stream().map(userDTO -> userDTO.getId()).collect(Collectors.toSet());
        List<UserOrganizationDTO> userOrganizations = this.userOrganizationService.listUserOrganizations(userIds);
        Map<Long, List<UserOrganizationDTO>> userOrganizationGroups = userOrganizations.stream()
                .collect(Collectors.groupingBy(UserOrganizationDTO::getUserId));
        userDTOS.stream().forEach(userDTO -> {
            // 设置用户组织机构
            List<UserOrganizationDTO> organizations =
                    ObjectUtils.ifNull(userOrganizationGroups.get(userDTO.getId()), Collections.emptyList());
            UserOrganizationDTO organization = organizations.stream().findAny().orElse(null);
            if (organization != null) {
                userDTO.setOrganization(ObjectUtils.copying(organization, UserDTO.Organization.class));
            }
        });
        return userDTOS;
    }

    /**
     * 通过部门和岗位ID 查询用户ID列表
     *
     * @param orgIds  组织机构ID
     * @param postIds 岗位ID
     *
     * @return 用户ID列表
     */
    @GetMapping("/authenticated/user-under-tenant/orgAndPost")
    public List<Long> listUserByOrgAndPost(@RequestParam(value = "orgIds", required = false) List<Long> orgIds,
                                           @RequestParam(value = "postIds", required = false) List<Long> postIds
                                          ) {
        List<UserOrganization> list = this.userOrganizationService.lambdaQuery()
                .in(!CollectionUtils.isEmpty(orgIds), UserOrganization::getOrganizationId, orgIds)
                .in(!CollectionUtils.isEmpty(postIds), UserOrganization::getPositionId, postIds).list();
        return Optional.ofNullable(list).orElse(Collections.emptyList())
                .stream().map(o -> o.getUserId()).distinct().collect(Collectors.toList());
    }

    /**
     * s
     * 通过部门和岗位ID 查询启用用户ID列表
     *
     * @param orgIds  组织机构ID
     * @param postIds 岗位ID
     *
     * @return 用户ID列表
     */
    @GetMapping("/authenticated/enable-user-under-tenant/orgAndPost")
    public List<Long> listEnableUserByOrgAndPost(@RequestParam(value = "orgIds", required = false) List<Long> orgIds,
                                                 @RequestParam(value = "postIds", required = false) List<Long> postIds
                                                ) {
        return userOrganizationService.listEnableUserByOrgAndPost(orgIds, postIds);
    }

    /**
     * 内部调用获取组织结构下某个岗位的用户列表
     *
     * @param organizationId
     * @param postId
     *
     * @return
     */
    @GetMapping("/authenticated/organization-users/{organizationId}/{postId}")
    public List<UserDTO> organizationAndPostUsers(@PathVariable("organizationId") Long organizationId,
                                                  @PathVariable("postId") Long postId
                                                 ) {
        return userService.organizationAndPostUsers(organizationId, postId);
    }

    /**
     * 内部调用获取组织结构下某个岗位的用户列表
     *
     * @param organizationId
     * @param postId
     *
     * @return
     */
    @GetMapping("/authenticated/organization-users/enable/{organizationId}/{postId}")
    public List<UserDTO> organizationAndPostEnabledUsers(@PathVariable("organizationId") Long organizationId,
                                                         @PathVariable("postId") Long postId
                                                        ) {
        List<UserDTO> userDTOS = userService.organizationAndPostEnabledUsers(organizationId, postId);
        if (CollectionUtils.isEmpty(userDTOS)) {
            return Collections.emptyList();
        }
        Set<Long> userIds = userDTOS.stream().map(userDTO -> userDTO.getId()).collect(Collectors.toSet());
        List<UserOrganizationDTO> userOrganizations = this.userOrganizationService.listUserOrganizations(userIds);
        Map<Long, List<UserOrganizationDTO>> userOrganizationGroups = userOrganizations.stream()
                .collect(Collectors.groupingBy(UserOrganizationDTO::getUserId));
        userDTOS.stream().forEach(userDTO -> {
            // 设置用户组织机构
            List<UserOrganizationDTO> organizations =
                    ObjectUtils.ifNull(userOrganizationGroups.get(userDTO.getId()), Collections.emptyList());
            UserOrganizationDTO organization = organizations.stream().findAny().orElse(null);
            if (organization != null) {
                userDTO.setOrganization(ObjectUtils.copying(organization, UserDTO.Organization.class));
            }
        });
        return userDTOS;
    }

    @GetMapping("/authenticated/user-under-tenant/ids/list")
    public List<UserDTO> listUserByIds(@RequestParam(value = "ids") Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<User> users = this.userService.lambdaQuery().in(User::getId, ids).list();
        List<UserDTO> userDTOS = ObjectUtils.copying(users, UserDTO.class);
        if (CollectionUtils.isEmpty(userDTOS)) {
            return Collections.emptyList();
        }
        Set<Long> userIds = userDTOS.stream().map(userDTO -> userDTO.getId()).collect(Collectors.toSet());
        List<UserOrganizationDTO> userOrganizations = this.userOrganizationService.listUserOrganizations(userIds);
        Map<Long, List<UserOrganizationDTO>> userOrganizationGroups = userOrganizations.stream()
                .collect(Collectors.groupingBy(UserOrganizationDTO::getUserId));
        userDTOS.stream().forEach(userDTO -> {
            // 设置用户组织机构
            List<UserOrganizationDTO> organizations =
                    ObjectUtils.ifNull(userOrganizationGroups.get(userDTO.getId()), Collections.emptyList());
            UserOrganizationDTO organization = organizations.stream().findAny().orElse(null);
            if (organization != null) {
                userDTO.setOrganization(ObjectUtils.copying(organization, UserDTO.Organization.class));
            }
        });
        return userDTOS;
    }

    @GetMapping("/authenticated/user-under-tenant/ids/enable/list")
    public List<UserDTO> listEnableUserByIds(@RequestParam(value = "ids") Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<User> users = this.userService.lambdaQuery()
                .in(User::getId, ids)
                .eq(User::getEnabled, true).list();
        List<UserDTO> userDTOS = ObjectUtils.copying(users, UserDTO.class);
        if (CollectionUtils.isEmpty(userDTOS)) {
            return Collections.emptyList();
        }
        return userDTOS;
    }

    @GetMapping("/authenticated/user-under-tenant/username/{username}")
    public UserDTO getUserByUserName(@PathVariable("username") String username) {
        User one = this.userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        return ObjectUtils.copying(one, UserDTO.class);
    }

    /**
     * 通过手机号 获取用户信息
     *
     * @param mobile 用户主键
     *
     * @return 用户信息
     */
    @GetMapping("/authenticated/user-under-tenant/mobile/{mobile}")
    public UserDTO getUserByMobile(@PathVariable("mobile") String mobile) {
        User one = this.userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getMobile, mobile));
        UserDTO userDTO = ObjectUtils.copying(one, UserDTO.class);
        if (Objects.nonNull(one)) {
            List<UserRoleDTO> userRoleDTOS = userRoleService.listUserRoles(Collections.singleton(one.getId()));
            if (CollectionUtils.isNotEmpty(userRoleDTOS)) {
                List<UserDTO.Role> roles = new ArrayList<>();
                for (UserRoleDTO userRoleDTO : userRoleDTOS) {
                    UserDTO.Role role = new UserDTO.Role();
                    role.setId(userRoleDTO.getId());
                    role.setName(userRoleDTO.getName());
                    roles.add(role);
                }
                userDTO.setRoles(roles);
            }
        }

        return userDTO;
    }

    /**
     * 通过手机号 获取在租户下的用户信息
     *
     * @param mobile 用户主键
     * @param tenant 租户
     *
     * @return 用户信息
     */
    @GetMapping("/authenticated/user-under-tenant/{tenant}/mobile/{mobile}")
    public UserDTO getUserByMobile(@PathVariable("mobile") String mobile, @PathVariable("tenant") Integer tenant) {
        User one = this.userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getMobile, mobile));
        if (Objects.nonNull(one)) {
            UserTenant userTenant = userTenantService.getOne(Wrappers.<UserTenant>lambdaQuery().eq(UserTenant::getUserId, one.getId())
                                                                     .eq(UserTenant::getTenant, tenant));
            if (Objects.nonNull(userTenant)) {
                return ObjectUtils.copying(one, UserDTO.class);
            }
        }
        return null;
    }

    @GetMapping("/authenticated/user/list")
    public List<UserDTO> listUsers(@RequestParam("keyword") String keyword) {
        List<User> users = this.userService.lambdaQuery()
                .eq(User::getEnabled, true)
                .and(StringUtils.isNotEmpty(keyword), q -> q
                        .like(User::getName, keyword)
                        .or()
                        .like(User::getMobile, keyword)
                    ).list();
        return ObjectUtils.copying(users, UserDTO.class);
    }

    @GetMapping("/authenticated/user-under-tenant/tenant/{tenant}")
    public UserDTO getTenantAdminByTenant(@PathVariable("tenant") Integer tenant) {
        UserTenant userTenantAdmin = this.userTenantService.getUserTenantAdmin(tenant);
        if (Objects.isNull(userTenantAdmin)) {
            return null;
        }
        User user = this.userService.getById(userTenantAdmin.getUserId());
        return ObjectUtils.copying(user, UserDTO.class);
    }

    @GetMapping("/authenticated/user-under-tenant/page")
    public Pagination<UserVO> listUsers(@Valid UserQueryParam param) {
        // 获取用户列表
        Page<UserVO> page = this.userOrganizationService.listOrganizationUsers(param);
        List<UserVO> users = ObjectUtils.ifNull(page, Page::getRecords);
        if (CollectionUtils.isEmpty(users)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        return PagingUtils.convert(page, extendUsers(users));
    }

    @GetMapping("/authenticated/user-under-tenant/list")
    public List<UserVO> usersForList(@Valid UserQueryParam param) {
        // 获取用户列表
        List<UserVO> users = this.userOrganizationService.userList(param);
        if (!CollectionUtils.isEmpty(users)) {
            return extendUsers(users);
        }
        return Collections.emptyList();
    }

    /**
     * 扩展用户信息
     *
     * @param users
     *
     * @return
     */
    private List<UserVO> extendUsers(List<UserVO> users) {
        // 获取用户相关组织机构和岗位及角色
        Set<Long> userIds = users.stream().map(UserVO::getId).collect(Collectors.toSet());
        List<UserOrganizationDTO> userOrganizations = this.userOrganizationService.listUserOrganizations(userIds);
        Set<Long> positionIds =
                userOrganizations.stream().map(UserOrganizationDTO::getPositionId).collect(Collectors.toSet());
        Map<Long, Position> positionMappings = this.positionService.listPositions(positionIds).stream()
                .collect(Collectors.toMap(Position::getId, position -> position, (o, n) -> n));
        Map<Long, List<UserOrganizationDTO>> userOrganizationGroups = userOrganizations.stream()
                .collect(Collectors.groupingBy(UserOrganizationDTO::getUserId));
        Map<Long, List<UserRoleDTO>> userRoleMappings = this.userRoleService.listUserRoles(userIds).stream()
                .collect(Collectors.groupingBy(UserRoleDTO::getUserId));
        //岗位
        List<Long> appointmentIds =
                userOrganizations.stream().map(UserOrganizationDTO::getAppointmentId).collect(Collectors.toList());
        //查询岗位信息
        List<AppointmentDetailVO> appointmentDetailVOS = appointmentQueryService.get(appointmentIds);
        Map<Long, AppointmentDetailVO> appointmentMap = Optional.ofNullable(appointmentDetailVOS).orElse(Collections.emptyList()).stream()
                .collect(Collectors.toMap(AppointmentDetailVO::getId, a -> a, (o, n) -> n));

        BusinessKeysParam businessKeysParam = new BusinessKeysParam();
        businessKeysParam.setBusinessKeys(Lists.newArrayList(userIds));
        Map<Long, List<FileVO>> userHeadImageMap = fileClient.getByBusinessKeys(businessKeysParam);
        Map<Long, String> userWorkStatusMap = new HashMap<>();
        List<UserTenantDTO> userTenantList = userTenantService.getTenantUserWorkStatus(SessionContextHolder.getRequester()
                                                                                               .getTenant(), userIds);
        if (userTenantList != null) {
            userTenantList.forEach(dto -> userWorkStatusMap.put(dto.getUserId(), dto.getWorkStatus()));
        }

        var extendInfoMap = new HashMap<Long, UserConverterForUserVO.UserVOExtendInfo>();
        for (var userId : userIds) {
            var avatarFile = Optional.ofNullable(userHeadImageMap.getOrDefault(userId, Collections.emptyList()))
                    .orElseGet(Collections::emptyList)
                    .stream().findFirst().orElse(null);
            var organizationDTOs = userOrganizationGroups.getOrDefault(userId, Collections.emptyList());
            var organizationDTO = organizationDTOs.stream().findAny().orElse(null);
            var organizations = Optional.ofNullable(organizationDTO).map((dto) -> {
                var organizationIds = Arrays.stream(dto.getSequence().split("-")).map(Long::parseLong).collect(Collectors.toList());
                return this.organizationService.listOrganizations(organizationIds);
            }).orElse(Collections.emptyList());
            var positions = organizationDTOs.stream().map(o -> positionMappings.get(o.getPositionId())).filter(Objects::nonNull)
                    .collect(Collectors.toList());
            var appointments = organizationDTOs.stream().map(o -> appointmentMap.get(o.getAppointmentId())).filter(Objects::nonNull)
                    .toList();
            extendInfoMap.put(userId, new UserConverterForUserVO.UserVOExtendInfo(avatarFile, organizations, positions, appointments, userRoleMappings.get(userId), userWorkStatusMap.get(userId)));
        }
        UserConverterForUserVO.extendUserVOs(users, extendInfoMap);
        return users;
    }

    @PostMapping("/authenticated/user-under-tenant/page")
    public Pagination<UserVO> postListUsers(@Valid @RequestBody UserQueryParam param) {
        return this.listUsers(param);
    }

    @GetMapping("/authenticated/user-under-tenant/{tenant}/tenant")
    public Pagination<UserDTO> listUserByTenant(@PathVariable("tenant") Integer tenant, @SpringQueryMap UserQueryParam queryParam) {
        // 获取这个租户下的用户信息
        List<UserTenant> userTenants = this.userTenantService.listUserTenantByUserIds(queryParam.getIds(), tenant);
        if (CollectionUtils.isEmpty(userTenants)) {
            return Pagination.empty();
        }
        List<Long> userIds = userTenants.stream().map(UserTenant::getUserId).collect(Collectors.toList());
        queryParam.setIds(userIds);
        Page<User> page = this.userService.getByPage(queryParam);
        Pagination<UserDTO> result = PagingUtils.convert(page, UserDTO.class);
        List<Long> businessKeys = result.getRecords().stream()
                .filter(user -> Objects.nonNull(user.getPhotographKey()))
                .map(UserDTO::getPhotographKey).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(businessKeys)) {
            return result;
        }
        BusinessKeysParam fileParam = new BusinessKeysParam();
        fileParam.setBusinessKeys(businessKeys);
        Map<Long, List<FileVO>> fileMap = fileClient.getByBusinessKeys(fileParam);
        for (UserDTO record : result.getRecords()) {
            if (record.getPhotographKey() != null && fileMap.containsKey(record.getPhotographKey())) {
                record.setPhotographList(fileMap.get(record.getPhotographKey()));
            }
        }
        return result;
    }

    @GetMapping("/authenticated/user/password/check")
    public void userPasswordCheck(@RequestParam String password) {
        Long id = SecurityContextHolder.getPrincipal().getId();
        User currentUser = this.userService.getById(id);
        if (!this.passwordEncoder.matches(password, currentUser.getPassword())) {
            throw new UnauthenticatedException("密码不正确");
        }
    }

    /**
     * 删除用户账户
     *
     * @param mobile
     */
    @PutMapping("/authenticated/del/user/{mobile}")
    void deleteUser(@PathVariable("mobile") String mobile) {
        List<User> users = this.userService.list(Wrappers.<User>lambdaQuery()
                                                         .and(q -> q.eq(User::getUsername, mobile)
                                                                 .or().eq(User::getMobile, mobile)));
        if (!CollectionUtils.isEmpty(users)) {
            DatabaseContextHolder.softDelete(userService, users);
        }
    }

    /**
     * 逻辑删除用户
     *
     * @param id 主键
     */
    @PutMapping("/authenticated/remove/user/{id}")
    public void removeUserById(@PathVariable("id") Long id) {
        List<User> users = this.userService.list(Wrappers.<User>lambdaQuery().eq(User::getId, id));
        if (!CollectionUtils.isEmpty(users)) {
            DatabaseContextHolder.softDelete(userService, users);
        }
        userTenantService.remove(Wrappers.<UserTenant>lambdaQuery().eq(UserTenant::getUserId, id));

        userWechatService.remove(Wrappers.<UserWechat>lambdaQuery().eq(UserWechat::getUserId, id));

        userTelService.remove(Wrappers.<UserTel>lambdaQuery().eq(UserTel::getUserId, id));
    }

    @GetMapping("/authenticated/role/user/list")
    public List<UserDTO> listUsersByRoles(@RequestParam("roleIds") @NotEmpty Set<Long> roleIds,
                                          @RequestParam(value = "keyword", required = false) String keyword
                                         ) {
        List<UserDTO> userDTOS = userMapper.selectUsersByRoles(SessionContextHolder.getRequester().getTenant(), roleIds, keyword);
        if (ObjectUtils.isNotEmpty(userDTOS)) {
            userDTOS.forEach(userDTO ->
                                     userDTO.setOrganizations(organizationService.getAllParentOrganization(userDTO.getOrganization()))
                            );
        }
        return userDTOS;
    }

    /**
     * @Author wql
     * @Description 按角色id分页查用户信息
     * @Date 19:47 2023/11/30
     **/
    @PostMapping("/authenticated/role/user/page")
    public Page<UserDTO> pageUsersByRoles(@Valid @RequestBody UsersByRolesQuery query) {
        query.setTenant(SessionContextHolder.getRequester().getTenant());
        if (query.getOrganizationId() != null) {
            Set<Long> set = Sets.newHashSet(query.getOrganizationId());
            set.addAll(organizationService.getAllSubOrganization(query.getOrganizationId()));
            query.setOrganizationIds(set);
        }
        Page<UserDTO> page = userMapper.pageUsersByRoles(PagingUtils.convert(query), query);
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(userDTO -> {
                userDTO.setOrganizations(organizationService.getAllParentOrganization(userDTO.getOrganization()));
            });
        }
        return page;
    }

    @GetMapping("/tenant/user/list")
    public List<UserDTO> list(UserQuery userQuery) {
        return userMapper.selectUsers(userQuery, SessionContextHolder.getRequester().getTenant());
    }

    @PostMapping("/tenant/user/list")
    public List<UserDTO> postList(@RequestBody @Valid UserQuery userQuery) {
        return userMapper.selectUsers(userQuery, SessionContextHolder.getRequester().getTenant());
    }

    /**
     * 根据指定规则获取用户信息
     */
    @PostMapping("/tenant/user/find/rule")
    public Map<Long, Set<Long>> findUserByRule(@RequestBody List<UserRuleConditionParam> ruleParam) {
        return userService.findUserByRule(ruleParam);
    }

    @PostMapping("/tenant/system/user/list")
    public List<UserDTO> postSystemList(@RequestBody @Valid UserQuery userQuery) {
        return userMapper.selectSystemUsers(userQuery, SessionContextHolder.getRequester().getTenant());
    }

    /**
     * 职位下用户
     *
     * @param appointmentIds 职位主键
     *
     * @return
     */
    @GetMapping("/authenticated/appointment/user/list")
    public List<UserDTO> getUserListByAppointmentIds(@RequestParam("appointmentIds") String appointmentIds) {
        return userService.getUserListByAppointmentIds(appointmentIds);
    }

    /**
     * 判断用户是否属于某个部门、角色、岗位、职位
     *
     * @param userBelongsInstitutionParam
     */
    @GetMapping("/authenticated/user/belongs/institution")
    public Boolean userBelongsInstitution(@SpringQueryMap UserBelongsInstitutionParam userBelongsInstitutionParam) {
        return userService.userBelongsInstitution(userBelongsInstitutionParam);
    }

    @PostMapping("/tenant/user/map")
    public Map<String, UserDTO> getTenantUserMap(@RequestBody @Valid UserMapQuery userQuery) {
        return userService.getTenantUserMap(userQuery);
    }
}
