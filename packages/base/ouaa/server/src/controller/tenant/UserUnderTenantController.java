package com.guhecloud.cube.biz.ouaa.server.controller.tenant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.server.dao.AppointmentMapper;
import com.guhecloud.cube.biz.ouaa.server.dao.UserManagedOrganizationMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.AppointmentDetailVO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserOrganizationDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Appointment;
import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserManagedOrganization;
import com.guhecloud.cube.biz.ouaa.server.entity.UserOrganization;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTel;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.param.UserCenterQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.server.permission.cache.UserPermissionCache;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.DelTenantUserMessage;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.UserUpdateMessage;
import com.guhecloud.cube.biz.ouaa.server.service.EnterpriseExportService;
import com.guhecloud.cube.biz.ouaa.server.service.OrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.PositionService;
import com.guhecloud.cube.biz.ouaa.server.service.UserOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTelService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.cube.biz.ouaa.server.utils.CollectionsUtils;
import com.guhecloud.cube.biz.ouaa.server.utils.PasswordUtils;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.AESUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
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
public class UserUnderTenantController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    private final PositionService positionService;
    private final OrganizationService organizationService;
    private final UserOrganizationService userOrganizationService;
    private final FileClient fileClient;

    private final UserManagedOrganizationMapper userManagedOrganizationMapper;
    private final PasswordUtils passwordUtils;
    private final AppointmentMapper appointmentMapper;
    private final EnterpriseExportService enterpriseExportService;
    private final UserTenantService userTenantService;
    private final UserTelService userTelService;
    private final UserPermissionCache userPermissionCache;

    @PostMapping("/user-under-tenant")
    public Long saveByTenant(@RequestBody @Valid UserModifyParam param, Requester requester) {
        if (StringUtils.isBlank(param.getMobile())) {
            param.setMobile(null);
        }
        User user = ObjectUtils.copying(param, User.class);
        user.setId(IdWorker.getId());
        user.setAdmin(false);
        if (StringUtils.isNotEmpty(param.getPassword())) {
            String password = AESUtils.decrypt("comguheyuncloud1", param.getPassword());
            user.setPassword(this.passwordEncoder.encode(password));
            user.setValidity("PERMANENT");
        } else {
            // 密码加初始值
            user.setPassword(passwordUtils.getInitializePassword());
        }

        Assert.isParameterError(
                StringUtils.isBlank(param.getUsername()) && StringUtils.isBlank(param.getMobile()), "用户名或手机号不能为空");
        if (StringUtils.isBlank(param.getUsername())) {
            user.setUsername(param.getMobile());
        }
        param.setTenant(requester.getTenant());
        this.userService.saveByTenant(user, param);
        //刷新用户权限缓存
        userPermissionCache.cacheUserPermission(Collections.singleton(user.getId()));
        return user.getId();
    }

    @PutMapping("/user-under-tenant/{id}")
    public void updateByTenant(@PathVariable("id") Long id, @RequestBody @Valid UserModifyParam param, Requester requester) {
        param.setTenant(requester.getTenant());
        User user = this.userService.updateByTenant(id, param);
        //用户信息被修改，通知相应的业务模块更新用户信息（例如：采购商和批发商）
        UserUpdateMessage.UserUpdateMessageBuilder builder = UserUpdateMessage.builder()
                .id(id).name(param.getName()).mobile(param.getMobile()).gender(param.getGender()).tenant(param.getTenant())
                .enabled(user.getEnabled()).idCard(user.getIdCard())
                .positionIds(param.getPositionIds())
                .photographKey(user.getPhotographKey()).photographEdit(!param.getName().equals(param.getOldName()) &&
                                                                       !CollectionsUtils.eqLongList(param.getOldPhotographIdList(), param.getPhotographIdList()));
        if (CollectionUtils.isNotEmpty(param.getOrganizationIds())) {
            builder.organizationId(param.getOrganizationIds().get(param.getOrganizationIds().size() - 1));
        }
        if (SessionContextHolder.getRequester() != null) {
            builder.tenant(SessionContextHolder.getRequester().getTenant());
        }
        builder.appointmentIds(param.getAppointmentIds());
        RocketMQContextHolder.send(UserUpdateMessage.TOPIC, builder.build());
    }

    @GetMapping("/user-under-tenant/page")
    public Pagination<UserVO> listUsers(@Valid UserQueryParam param) {
        // 获取用户列表
        Page<UserVO> page = this.userOrganizationService.listOrganizationUsers(param);
        List<UserVO> users = ObjectUtils.ifNull(page, Page::getRecords);
        if (CollectionUtils.isEmpty(users)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        //设置用户手机号
        //获取用户绑定信息,并设置绑定的手机号
        Map<Long, UserTel> userTelMap = userTelService.getBindMapByUserIds(ListUtils.convertItem(users, UserVO::getId));
        users.forEach(userVO -> {
            UserTel userTel = userTelMap.get(userVO.getId());
            if (Objects.nonNull(userTel)) {
                userVO.setMobile(userTel.getTel());
            }
        });
        //查询用户与租户的关系信息
        List<UserTenant> userTenants = userTenantService.listUserTenantByUserIds(ListUtils.convertItem(users, UserVO::getId), SessionContextHolder
                .getRequester().getTenant());
        Map<Long, UserTenant> userTenantMap = MapUtils.listToMap(userTenants, UserTenant::getUserId, userTenant -> userTenant);
        users.forEach(userVO -> {
            UserTenant userTenant = userTenantMap.get(userVO.getId());
            if (Objects.nonNull(userTenant) && Objects.nonNull(userTenant.getEnabled())) {
                userVO.setEnabled(userTenant.getEnabled());
            } else {
                userVO.setEnabled(true);
            }
        });
        // 获取用户相关组织机构和岗位及角色
        Set<Long> userIds = users.stream().map(UserVO::getId).collect(Collectors.toSet());
        List<UserOrganizationDTO> userOrganizations = this.userOrganizationService.listUserOrganizations(userIds);
        Set<Long> positionIds =
                userOrganizations.stream().map(UserOrganizationDTO::getPositionId).collect(Collectors.toSet());
        Map<Long, Position> positionMappings = this.positionService.listPositions(positionIds).stream()
                .collect(Collectors.toMap(Position::getId, position -> position, (o, n) -> n));
        Map<Long, Appointment> appointmentMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(userOrganizations)) {
            Set<Long> appointmentIds = userOrganizations.stream().map(UserOrganizationDTO::getAppointmentId).filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(appointmentIds)) {
                List<Appointment> appointmentList = this.appointmentMapper.selectBatchIds(appointmentIds);
                if (!CollectionUtils.isEmpty(appointmentList)) {
                    appointmentList.forEach(a -> appointmentMap.put(a.getId(), a));
                }
            }
        }
        Map<Long, List<UserOrganizationDTO>> userOrganizationGroups = userOrganizations.stream()
                .collect(Collectors.groupingBy(UserOrganizationDTO::getUserId));
        Map<Long, List<UserRoleDTO>> userRoleMappings = this.userRoleService.listUserRoles(userIds).stream()
                .collect(Collectors.groupingBy(UserRoleDTO::getUserId));

        List<UserManagedOrganization> userManagedOrganizations = this.userManagedOrganizationMapper.selectList(
                Wrappers.<UserManagedOrganization>lambdaQuery()
                        .in(UserManagedOrganization::getUserId, userIds)
                                                                                                              );
        Map<Long, List<UserManagedOrganization>> userIdToOrganMap = userManagedOrganizations.stream()
                .collect(Collectors.groupingBy(UserManagedOrganization::getUserId));

        Map<Long, String> managedOrganizationsNameMap = this.organizationService.listOrganizations(
                        userManagedOrganizations.stream().map(UserManagedOrganization::getOrganizationId).collect(Collectors.toList())
                                                                                                  ).stream()
                .collect(Collectors.toMap(Organization::getId, o -> o.getName()));
        var businessKeysParam = new BusinessKeysParam();
        businessKeysParam.setBusinessKeys(ListUtils.convertItem(users, UserVO::getId).stream().toList());
        var filesMap = fileClient.getByBusinessKeys(businessKeysParam);

        // 构建并返回用户信息
        List<UserVO> vos = users.stream().map(user -> {
            UserVO vo = ObjectUtils.copying(user, UserVO.class);
            List<UserManagedOrganization> managedOrganizations = userIdToOrganMap.get(user.getId());
            if (CollectionUtils.isNotEmpty(managedOrganizations)) {
                List<UserVO.Organization> managedOrganizationList = new ArrayList<>();
                managedOrganizations.forEach(managedOrganization -> {
                    UserVO.Organization organization = new UserVO.Organization();
                    organization.setId(managedOrganization.getId());
                    organization.setName(managedOrganizationsNameMap.get(managedOrganization.getOrganizationId()));
                    managedOrganizationList.add(organization);
                });
                vo.setManagedOrganizations(managedOrganizationList);
            }

            // 设置用户组织机构
            List<UserOrganizationDTO> organizations =
                    ObjectUtils.ifNull(userOrganizationGroups.get(user.getId()), Collections.emptyList());
            UserOrganizationDTO organization = organizations.stream().findAny().orElse(null);
            if (organization != null) {
                List<Long> ids = Arrays.stream(organization.getSequence().split("-")).map(Long::parseLong).collect(Collectors.toList());
                List<Organization> organizationList = this.organizationService.listOrganizations(ids);
                vo.setOrganizations(ObjectUtils.copying(organizationList, UserVO.Organization.class));
            } else {
                vo.setOrganizations(Collections.emptyList());
            }
            // 设置用户岗位列表
            List<UserVO.Position> positions = organizations.stream().map(o ->
                                                                                 ObjectUtils.copying(positionMappings.get(o.getPositionId()), UserVO.Position.class))
                    .filter(Objects::nonNull).collect(Collectors.toList());
            vo.setPositions(positions);
            List<AppointmentDetailVO> appointments = organizations.stream().map(o ->
                                                                                        ObjectUtils.copying(appointmentMap.get(o.getAppointmentId()), AppointmentDetailVO.class))
                    .filter(Objects::nonNull).collect(Collectors.toList());
            vo.setAppointmentList(appointments);
            // 设置用户角色列表
            List<UserRoleDTO> roles = userRoleMappings.get(user.getId());
            vo.setRoles(ObjectUtils.copying(roles, UserVO.Role.class));
            //设置用户照片
            var fileVos = filesMap.get(user.getId());

            vo.setFileVO(CollectionUtils.isNotEmpty(fileVos) ? fileVos.getFirst() : null);

            return vo;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, vos);
    }

    /**
     * 获取用户信息
     *
     * @param id 用户主键
     *
     * @return 用户信息
     */
    @GetMapping("/user-under-tenant/{id}")
    public UserVO getUser(@PathVariable("id") Long id) {
        UserVO user = ObjectUtils.copying(this.userService.getById(id), UserVO.class);
        if (user != null) {
            //查询用户的租户信息
            UserTenant userTenant = userTenantService.getUserTenantByUserIdAndTenant(id, SessionContextHolder
                    .getRequester().getTenant());
            Optional.ofNullable(userTenant).ifPresent(ut -> {
                user.setEnabled(Objects.isNull(ut.getEnabled()) || ut.getEnabled());
            });
            user.setPassword(null);
            Set<Long> userIds = Collections.singleton(user.getId());
            List<UserOrganizationDTO> userOrganizations = this.userOrganizationService.listUserOrganizations(userIds);

            List<UserManagedOrganization> userManagedOrganizations = userManagedOrganizationMapper.selectList(
                    Wrappers.<UserManagedOrganization>lambdaQuery()
                            .eq(UserManagedOrganization::getUserId, user.getId())
                                                                                                             );

            Map<Long, String> managedOrganizationsNameMap = this.organizationService.listOrganizations(
                            userManagedOrganizations.stream().map(UserManagedOrganization::getOrganizationId).collect(Collectors.toList())
                                                                                                      ).stream()
                    .collect(Collectors.toMap(Organization::getId, o -> o.getName()));

            List<UserVO.Organization> organizations = new ArrayList<>();
            userManagedOrganizations.forEach(userManagedOrganization -> {
                UserVO.Organization organization = new UserVO.Organization();
                organization.setId(userManagedOrganization.getOrganizationId());
                organization.setName(managedOrganizationsNameMap.get(userManagedOrganization.getOrganizationId()));
                organizations.add(organization);
            });

            user.setManagedOrganizations(organizations);

            // 获取用户头像
            List<FileVO> fileVO = this.fileClient.getByBusinessKey(user.getId());
            user.setFileVO(CollectionUtils.isEmpty(fileVO) ? null : fileVO.get(0));

            // 获取用户照片
            if (user.getPhotographKey() != null && user.getPhotographKey() > 0) {
                user.setPhotographList(fileClient.getByBusinessKey(user.getPhotographKey()));
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
            // 设置用户职位列表
            Set<Long> appointmentIds = userOrganizations.stream().map(UserOrganizationDTO::getAppointmentId).collect(Collectors.toSet());
            if (ObjectUtils.isNotEmpty(appointmentIds)) {
                List<Appointment> appointment = this.appointmentMapper.selectList(new LambdaQueryWrapper<Appointment>()
                                                                                          .in(Appointment::getId, appointmentIds));
                user.setAppointmentIds(appointment);
            }
            // 设置用户角色列表
            List<UserRoleDTO> roles = this.userRoleService.listUserRoles(userIds);
            user.setRoles(ObjectUtils.copying(roles, UserVO.Role.class));
        }
        return user;
    }

    /**
     * 岗位下用户(内部调用)
     *
     * @param positionId 岗位主键
     *
     * @return
     */
    @GetMapping("/position-users/{positionId}")
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
    @GetMapping("/organization-users/{organizationId}")
    public List<UserDTO> organizationUsers(@PathVariable("organizationId") Long organizationId) {
        return this.userService.organizationUsers(organizationId);
    }

    /**
     * 通过部门和岗位ID 查询用户ID列表
     *
     * @param orgIds  组织机构ID
     * @param postIds 岗位ID
     *
     * @return 用户ID列表
     */
    @GetMapping("/user-under-tenant/orgAndPost")
    public List<Long> listUserByOrgAndPost(@RequestParam("orgIds") List<Long> orgIds,
                                           @RequestParam("postIds") List<Long> postIds
                                          ) {
        List<UserOrganization> list = this.userOrganizationService.lambdaQuery()
                .in(!CollectionUtils.isEmpty(orgIds), UserOrganization::getOrganizationId, orgIds)
                .in(!CollectionUtils.isEmpty(postIds), UserOrganization::getPositionId, postIds).list();
        return Optional.ofNullable(list).orElse(Collections.emptyList())
                .stream().map(o -> o.getUserId()).distinct().collect(Collectors.toList());
    }

    @GetMapping("/user-under-tenant/ids/list")
    List<UserDTO> listUserByIds(@RequestParam(value = "ids") List<Long> ids) {
        List<User> users = this.userService.lambdaQuery().in(User::getId, ids).list();
        return ObjectUtils.copying(users, UserDTO.class);
    }

    @GetMapping("/user-under-tenant/username/{username}")
    public UserDTO getUserByUserName(@PathVariable("username") String username) {
        User one = this.userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        return ObjectUtils.copying(one, UserDTO.class);
    }

    @DeleteMapping("/user-under-tenant/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.userService.removeUserTenant(SessionContextHolder.getRequester().getTenant(), id);
        RocketMQContextHolder.send(DelTenantUserMessage.TOPIC, DelTenantUserMessage.builder().id(id)
                .tenant(SessionContextHolder.getRequester().getTenant()).build());
    }

    /**
     * 下载模板
     */
    @PostMapping("/user-under-tenant/download/template")
    public void downloadUserTemplate(HttpServletResponse response) {
        userService.downloadUserTemplate(response);
    }

    /**
     * 批量导入
     */
    @PostMapping("/user-under-tenant/import-user")
    public void importUser(@RequestParam("file") MultipartFile file) {
        userService.importUser(file);
    }

    /**
     * 初始化人员照片
     */
    @GetMapping("/public/batch-sync-photos")
    public String batchSyncPhotos(Integer tenantId) {
        return userService.batchSyncPhotos(tenantId);
    }

    /**
     * 用户中心，用户信息接口
     */
    @GetMapping("/user-under-tenant/register-user/list")
    public Pagination<UserVO> getRegisterUserListForUserCenter(UserCenterQueryParam param) {
        return userService.getRegisterUserListForUserCenter(param);
    }

    /**
     * 用户中心，用户信息详情
     */
    @GetMapping("/user-under-tenant/register-user/detail")
    public UserVO getRegisterUserDetailForUserCenter(@RequestParam("id") Long id) {
        return userService.getRegisterUserDetailForUserCenter(id);
    }

    /**
     * 人员信息导入（苏州）
     */
    @PostMapping("/user-under-tenant/import-user/sync")
    public void importUserSyncSuzhou(@RequestParam("file") MultipartFile file) {
        userService.importUserSyncSuzhou(file);
    }

    /**
     * 员工导出
     *
     * @param param 筛选条件
     */
    @GetMapping("/user-under-tenant/export")
    public void exportStaff(@SpringQueryMap @Valid UserQueryParam param) {
        enterpriseExportService.exportStaff(param);
    }

}
