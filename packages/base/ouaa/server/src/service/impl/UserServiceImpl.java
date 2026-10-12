package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.util.SelectSheetWriteHandler;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.metadata.constant.UserInstitutionType;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.ConditionTypeEnum;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.InfoTypesEnum;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.OuaaFileSceneCodeEnum;
import com.guhecloud.cube.biz.ouaa.metadata.param.ConditionParam;
import com.guhecloud.cube.biz.ouaa.metadata.param.UserRuleConditionParam;
import com.guhecloud.cube.biz.ouaa.server.dao.UserMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserOrganizationDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Appointment;
import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserManagedOrganization;
import com.guhecloud.cube.biz.ouaa.server.entity.UserOrganization;
import com.guhecloud.cube.biz.ouaa.server.entity.UserRole;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.enumeration.TenantUserWorkStatusEnum;
import com.guhecloud.cube.biz.ouaa.server.enumeration.UserTenantBeloneEnum;
import com.guhecloud.cube.biz.ouaa.server.param.PasswordUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserBelongsInstitutionParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserCenterQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserPersonalUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.param.query.SysUserQuery;
import com.guhecloud.cube.biz.ouaa.server.param.query.UserMapQuery;
import com.guhecloud.cube.biz.ouaa.server.param.query.UserQuery;
import com.guhecloud.cube.biz.ouaa.server.param.validated.PasswordValidated;
import com.guhecloud.cube.biz.ouaa.server.permission.cache.UserPermissionCache;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.IncreaseTenantUserMessage;
import com.guhecloud.cube.biz.ouaa.server.service.AppointmentQueryService;
import com.guhecloud.cube.biz.ouaa.server.service.OrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.PositionService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleService;
import com.guhecloud.cube.biz.ouaa.server.service.UserManagedOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.UserOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.cube.biz.ouaa.server.service.UserWechatService;
import com.guhecloud.cube.biz.ouaa.server.utils.EasyExcelUtil;
import com.guhecloud.cube.biz.ouaa.server.utils.PasswordUtils;
import com.guhecloud.cube.biz.ouaa.server.verifier.UserTenantVerifier;
import com.guhecloud.cube.biz.ouaa.server.vo.UserExcelVO;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.AESUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户业务操作实现
 *
 * @author Woody
 * @date 2021/6/20
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    private final UserOrganizationService userOrganizationService;
    private final UserTenantService userTenantService;
    private final RoleService roleService;
    private final FileRefClient fileRefClient;
    private final UserMapper userMapper;
    private final UserWechatService userWechatService;
    private final UserManagedOrganizationService userManagedOrganizationService;
    private final PasswordUtils passwordUtils;
    private final FileClient fileClient;
    private final OrganizationService organizationService;
    private final AppointmentQueryService appointmentQueryService;
    private final PositionService positionService;
    private final PasswordValidated passwordValidated;
    private final UserTenantVerifier userTenantVerifier;
    private final UserPermissionCache userPermissionCache;

    private static void sendMessage(User user, UserModifyParam param) {
        if (param.getSendMessage()) {
            IncreaseTenantUserMessage increaseTenantUserMessage = ObjectUtils.copying(user, IncreaseTenantUserMessage.class);
            if (CollectionUtils.isNotEmpty(param.getOrganizationIds())) {
                increaseTenantUserMessage.setOrganizationId(param.getOrganizationIds().get(param.getOrganizationIds().size() - 1));
                increaseTenantUserMessage.setPositionIds(param.getPositionIds());
                increaseTenantUserMessage.setAppointmentIds(param.getAppointmentIds());
            }
            if (param.getPhotographIdList() != null && !param.getPhotographIdList().isEmpty()) {
                increaseTenantUserMessage.setPhotographKey(user.getPhotographKey());
            }
            if (param.getTenant() != null && param.getTenant() > 0) {
                increaseTenantUserMessage.setTenant(param.getTenant());
            } else if (SessionContextHolder.getRequester() != null) {
                increaseTenantUserMessage.setTenant(SessionContextHolder.getRequester().getTenant());
            }
            RocketMQContextHolder.send(IncreaseTenantUserMessage.TOPIC, increaseTenantUserMessage);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveByTenant(@NonNull User user, @NonNull UserModifyParam param) {
        // 判断用户是否存在，若存在用户判断是否存在于该租户下，如果存在则添加用户租户关系，不存在则创建用户再添加租户关系
        Long photographKey = IdWorker.getId();
        //添加的用户处理
        userHandle(user, param, photographKey);
        //处理用户租户关系
        userTenantHandle(user, param.getEnabled());
        // 保存用户照片
        saveUserPhotos(param, photographKey);
        //用户所属部门处理
        saveUserOrganizations(user, param);
        // 保存用户管理部门
        saveUserManageOrganizations(user, param);
        // 保存用户角色关联信息
        saveUserRoles(user, param);
        // 是否发送消息由客户端确定
        sendMessage(user, param);
    }

    private void saveUserOrganizations(User user, UserModifyParam param) {
        List<UserOrganization> userOrganizations = param.getUserOrg(user.getId());
        if (ObjectUtils.isNotEmpty(userOrganizations)) {
            this.userOrganizationService.saveBatch(userOrganizations);
        }
    }

    private void userHandle(User user, UserModifyParam param, Long photographKey) {
        User userByAccount = getUserByAccount(user.getUsername());
        User userByMobile = getUserByMobile(param.getMobile());
        if (Objects.isNull(userByAccount) && Objects.isNull(userByMobile)) {
            //不存在这个用户处理
            //校验身份证是否存在
            userTenantVerifier.idCardExistVerify(param.getIdCard());
            //校验手机号是否存在
            userTenantVerifier.mobileExistVerify(param.getMobile());
            if (param.getPhotographIdList() != null && !param.getPhotographIdList().isEmpty()) {
                user.setPhotographKey(photographKey);
            }
            this.save(user);
        } else {
            if (Objects.nonNull(userByAccount)) {
                ObjectUtils.copying(userByAccount, user);
            }
            if (Objects.nonNull(userByMobile)) {
                ObjectUtils.copying(userByMobile, user);
            }
        }
    }

    private void userTenantHandle(User user, Boolean enabled) {
        //用户租户关系处理
        Long userId = user.getId();
        //判断这个用户是否已经归属该企业
        UserTenant userTenant = userTenantService.getUserTenantByUserIdAndTenant(userId, SessionContextHolder.getRequester().getTenant());
        boolean belongToThisTenant = false;
        if (Objects.nonNull(userTenant)) {
            if (StringUtils.isBlank(userTenant.getBelongTenant())) {
                belongToThisTenant = true;
            } else {
                String[] split = userTenant.getBelongTenant().split(",");
                for (String belong : split) {
                    if (belong.equals(UserTenantBeloneEnum.BELONG.name())) {
                        belongToThisTenant = true;
                        break;
                    }
                }
            }
        }
        Assert.isConflict(belongToThisTenant, "用户存在该企业中");
        if (Objects.isNull(userTenant)) {
            userTenantService.addUserTenantByUserId(userId, enabled);
        } else {
            userTenant.setEnabled(enabled);
            var userTenantBelong = userTenant.getBelongTenant();
            if (userTenantBelong == null) {
                userTenant.setBelongTenant(UserTenantBeloneEnum.BELONG.name());
            } else {
                userTenant.setBelongTenant(userTenantBelong + "," + UserTenantBeloneEnum.BELONG.name());
            }
            userTenantService.updateById(userTenant);
        }
    }

    private void saveUserRoles(User user, UserModifyParam param) {
        if (!CollectionUtils.isEmpty(param.getRoleIds())) {
            List<UserRole> userRoles = param.getRoleIds().stream().map(
                    id -> UserRole.builder().userId(user.getId()).roleId(id).build()
                                                                      ).collect(Collectors.toList());
            this.userRoleService.saveBatch(userRoles);
        }
    }

    private void saveUserManageOrganizations(User user, UserModifyParam param) {
        if (CollectionUtils.isNotEmpty(param.getManagedOrganizationIds())) {
            List<UserManagedOrganization> userManagedOrganizations = new ArrayList<>();
            param.getManagedOrganizationIds().forEach(managedOrganizationId -> {
                UserManagedOrganization userManagedOrganization = new UserManagedOrganization();
                userManagedOrganization.setUserId(user.getId());
                userManagedOrganization.setOrganizationId(managedOrganizationId);
                userManagedOrganizations.add(userManagedOrganization);
            });
            userManagedOrganizationService.saveBatch(userManagedOrganizations);
        }
    }

    private void saveUserPhotos(UserModifyParam param, Long photographKey) {
        if (param.getPhotographIdList() != null && !param.getPhotographIdList().isEmpty()) {
            FileRefCreateParam refCreateParam = new FileRefCreateParam();
            refCreateParam.setBusinessKey(photographKey);
            refCreateParam.setFileIdList(param.getPhotographIdList());
            refCreateParam.setSceneCode(OuaaFileSceneCodeEnum.OUAA_USER.getCode());
            refCreateParam.setTag("photograph");
            fileRefClient.refInduce(refCreateParam);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User saveByAdmin(@NonNull UserModifyParam param) {
        // 判断是否存在该手机号
        User userByMobile = this.lambdaQuery().eq(User::getMobile, param.getMobile()).one();
        Assert.isConflict(Objects.nonNull(userByMobile), "已添加相同用户信息!");
        User user = ObjectUtils.copying(param, User.class);
        user.setId(IdWorker.getId());
        user.setAdmin(false);
        user.setName(StringUtils.isNotEmpty(param.getName()) ? param.getName() : param.getMobile());
        user.setUsername(param.getMobile());
        // 密码加初始值
        if (StringUtils.isNotBlank(param.getPassword())) {
            this.passwordValidated.validate(param.getPassword());
            user.setPassword(this.passwordEncoder.encode(param.getPassword()));
        } else {
            //默认密码
            user.setPassword(passwordUtils.getInitializePassword());
        }
        this.save(user);

        // 保存租户用户/租户关联关系
        if (Objects.nonNull(param.getTenant())) {
            UserTenant userTenant = UserTenant.builder()
                    .userId(user.getId())
                    .tenant(SessionContextHolder.getRequester().getTenant())
                    .admin(false).build();
            this.userTenantService.save(userTenant);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByAdmin(@NonNull UserUpdateParam param) {
        // 更新用户信息
        User user = this.getById(param.getId());
        Assert.isNotFound(user, "用户不存在");
        ObjectUtils.copying(param, user);
        this.updateById(user);

        if (param.getFileId() != null) {
            this.fileRefClient.removeByBusinessKeyAndScene(user.getId(), OuaaFileSceneCodeEnum.OUAA_USER.getCode());
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setBusinessKey(user.getId());
            fileRefCreateParam.setFileId(param.getFileId());
            this.fileRefClient.refInduceSingle(OuaaFileSceneCodeEnum.OUAA_USER.getCode(),
                                               user.getId(), param.getFileId(),
                                               null);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateNormalByTenant(@NonNull Long id, @NonNull UserModifyParam param) {
        // 更新用户信息
        User user = this.getById(id);
        String password = user.getPassword();
        if (ObjectUtils.isEmpty(user.getPassword())) {
            //初始密码
            password = passwordUtils.getInitializePassword();
        }
        Assert.isNotFound(user, "用户不存在");
        ObjectUtils.copying(param, user);
        user.setPassword(password);
        this.updateById(user);
        // 更新用户岗位/组织机构关联信息
        Assert.isParameterError(CollectionUtils.isEmpty(param.getOrganizationIds()), "部门不能为空!");
        List<Long> organizationIds = this.userOrganizationService.listUserOrganizations(Collections.singleton(user.getId())).stream()
                .map(Organization::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(organizationIds)) {
            //删除用户与租户的组织结构信息
            this.userOrganizationService.remove(Wrappers.<UserOrganization>lambdaQuery()
                                                        .eq(UserOrganization::getUserId, user.getId())
                                                        .in(UserOrganization::getOrganizationId, organizationIds));
        }
        //新增用户配置的部门
        List<UserOrganization> userSaveOrganizations = param.getUserOrg(user.getId());
        if (ObjectUtils.isNotEmpty(userSaveOrganizations)) {
            this.userOrganizationService.saveBatch(userSaveOrganizations);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateByTenant(@NonNull Long id, @NonNull UserModifyParam param) {
        long userId = id;
        //用户和租户的关系启用状态
        Boolean userTenantEnable = param.getEnabled();
        //更新用户和租户的启用状态关系
        userTenantService.updateUserTenantEnable(userId, userTenantEnable);
        // 更新用户信息
        User user = this.getById(id);
        Assert.isNotFound(user, "用户不存在");
        param.setPassword(user.getPassword());
        param.setEnabled(user.getEnabled());
        ObjectUtils.copying(param, user);
        Long oldPhotographId = user.getPhotographKey();
        Long newPhotographId = IdWorker.getId();
        if (param.getPhotographIdList() != null && !param.getPhotographIdList().isEmpty()) {
            user.setPhotographKey(newPhotographId);
        } else {
            user.setPhotographKey(null);
        }
        this.updateById(user);

        // 更新用户照片信息
        if (oldPhotographId != null && oldPhotographId > 0) {
            fileRefClient.removeByBusinessKeyAndScene(oldPhotographId, OuaaFileSceneCodeEnum.OUAA_USER.getCode());
        }
        if (param.getPhotographIdList() != null && !param.getPhotographIdList().isEmpty()) {
            FileRefCreateParam refCreateParam = new FileRefCreateParam();
            refCreateParam.setFileIdList(param.getPhotographIdList());
            refCreateParam.setSceneCode(OuaaFileSceneCodeEnum.OUAA_USER.getCode());
            refCreateParam.setBusinessKey(user.getPhotographKey());
            refCreateParam.setTag("photograph");
            fileRefClient.refInduce(refCreateParam);
        }

        // 更新用户岗位/组织机构关联信息
        Assert.isParameterError(CollectionUtils.isEmpty(param.getOrganizationIds()), "部门不能为空!");
        List<Long> organizationIds = this.userOrganizationService.listUserOrganizations(Collections.singleton(user.getId())).stream()
                .map(Organization::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(organizationIds)) {
            //删除用户与租户的组织结构信息
            this.userOrganizationService.remove(Wrappers.<UserOrganization>lambdaQuery()
                                                        .eq(UserOrganization::getUserId, user.getId())
                                                        .in(UserOrganization::getOrganizationId, organizationIds));
        }

        //新增用户配置的部门
        List<UserOrganization> userOrganizations = param.getUserOrg(user.getId());
        if (ObjectUtils.isNotEmpty(userOrganizations)) {
            this.userOrganizationService.saveBatch(userOrganizations);
        }
        // 更新用户角色关联信息
        List<UserRole> userRoles = this.userRoleService.lambdaQuery().eq(UserRole::getUserId, user.getId()).list();
        if (CollectionUtils.isEmpty(param.getRoleIds()) && CollectionUtils.isEmpty(userRoles)) {
            return user;
        }
        // 获取当前租户下用户的角色
        List<Long> myUserRoles = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> rolesUnderTenant = CollectionUtils.isEmpty(myUserRoles) ? new ArrayList<>()
                                                                           : this.roleService.lambdaQuery().in(Role::getId, myUserRoles)
                                              .list();
        List<Long> roleIdsUnderTenant = rolesUnderTenant.stream().map(Role::getId).collect(Collectors.toList());

        Set<Long> newRoles = ObjectUtils.ifNull(param.getRoleIds(), Sets::newHashSet, Collections::emptySet);
        Set<Long> originalRoles = userRoles.stream().filter(userRole -> roleIdsUnderTenant.contains(userRole.getRoleId()))
                .map(UserRole::getRoleId).collect(Collectors.toSet());
        Set<Long> removableUserRoles = userRoles.stream().filter(userRole -> !newRoles.contains(userRole.getRoleId()))
                .filter(userRole -> roleIdsUnderTenant.contains(userRole.getRoleId()))
                .map(UserRole::getId).collect(Collectors.toSet());
        List<UserRole> creatableUserRoles = newRoles.stream().filter(roleId -> !originalRoles.contains(roleId))
                .map(roleId -> UserRole.builder()
                        .userId(user.getId()).roleId(roleId).build()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(creatableUserRoles)) {
            this.userRoleService.saveBatch(creatableUserRoles);
        }
        if (!CollectionUtils.isEmpty(removableUserRoles)) {
            this.userRoleService.removeByIds(removableUserRoles);
        }

        // 保存管理部门
        // 先删除
        userManagedOrganizationService.remove(
                Wrappers.<UserManagedOrganization>lambdaQuery()
                        .eq(UserManagedOrganization::getUserId, user.getId())
                                             );
        List<UserManagedOrganization> userManagedOrganizations = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(param.getManagedOrganizationIds())) {
            param.getManagedOrganizationIds().forEach(managedOrganizationId -> {
                UserManagedOrganization userManagedOrganization = new UserManagedOrganization();
                userManagedOrganization.setUserId(user.getId());
                userManagedOrganization.setOrganizationId(managedOrganizationId);
                userManagedOrganizations.add(userManagedOrganization);
            });
        }
        if (CollectionUtils.isNotEmpty(userManagedOrganizations)) {
            userManagedOrganizationService.saveBatch(userManagedOrganizations);
        }

        return user;
    }

    @Override
    public void updateUserPersonal(@NonNull UserPersonalUpdateParam param) {
        // 更新用户信息
        User user = this.getById(SecurityContextHolder.getPrincipal().getId());
        Assert.isNotFound(user, "用户不存在");
        ObjectUtils.copying(param, user);
        this.updateById(user);
        if (param.getFileId() != null) {
            //            this.fileRefClient.removeByBusinessKey(user.getId());
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setFileId(param.getFileId());
            fileRefCreateParam.setSceneCode(OuaaFileSceneCodeEnum.OUAA_USER.getCode());
            fileRefCreateParam.setBusinessKey(user.getId());
            this.fileRefClient.refInduce(fileRefCreateParam);
        }
    }

    @Override
    public boolean registerOrUpdate(@NonNull User user) {
        return this.baseMapper.registerOrUpdate(user);
    }

    @Override
    public User getAdmin() {
        return this.baseMapper.getAdmin(SessionContextHolder.getRequester().getTenant());
    }

    @Override
    public User lookupUser(@NonNull String key) {
        // 获取用户信息并将信息存入当前会话上下文，避免多次查询
        return StringUtils.isEmpty(key) ? null :
               SessionContextHolder.getContextObject("user:" + key, k -> this.baseMapper.lookupUser(key));
    }

    @Override
    public User lookupUserNoContext(String key) {
        return this.baseMapper.lookupUser(key);
    }

    @Override
    public Page<User> getByPage(@NonNull UserQueryParam queryParam) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(User::getName, queryParam.getKeyword())
                        .or()
                        .like(User::getMobile, queryParam.getKeyword())
                    )
                .between(Objects.nonNull(queryParam.getCreateTime()), User::getCreateTime, Optional.ofNullable(queryParam.getCreateTime())
                        .orElse(Period.builder().build()).getBegin(), Optional.ofNullable(queryParam.getCreateTime())
                                 .orElse(Period.builder().build()).getEnd())
                .between(Objects.nonNull(queryParam.getEntryDate()), User::getEntryDate, Optional.ofNullable(queryParam.getEntryDate())
                        .orElse(Period.builder().build()).getBegin(), Optional.ofNullable(queryParam.getEntryDate())
                                 .orElse(Period.builder().build()).getEnd())
                .eq(!StringUtils.isEmpty(queryParam.getGender()), User::getGender, queryParam.getGender())
                .eq(StringUtils.isNotBlank(queryParam.getSource()), User::getSource, queryParam.getSource())
                .eq(Objects.nonNull(queryParam.getEnabled()), User::getEnabled, queryParam.getEnabled())
                .in(!CollectionUtils.isEmpty(queryParam.getIds()), User::getId, queryParam.getIds())
                .and(ListUtils.isNotEmpty(queryParam.getFilterUserIds()), (l) -> {
                    l.notIn(User::getId, queryParam.getFilterUserIds());
                })
                .orderByDesc(User::getLastLoginTime)
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    public Page<User> getSysUsers(@NonNull SysUserQuery query) {
        return baseMapper.getSysUsers(PagingUtils.convert(query), query);
    }

    @Override
    public List<UserDTO> positionUsers(@NonNull Long positionId) {
        return this.baseMapper.positionUsers(positionId);
    }

    @Override
    public List<UserDTO> organizationUsers(@NonNull Long organizationId) {
        return this.baseMapper.organizationUsers(organizationId);
    }

    @Override
    public List<UserDTO> organizationAndPostUsers(@NonNull Long organizationId, @NonNull Long positionId) {
        return this.baseMapper.organizationAndPostUsers(organizationId, positionId);
    }

    @Override
    public List<UserDTO> organizationAndPostEnabledUsers(@NonNull Long organizationId, @NonNull Long positionId) {
        return this.baseMapper.organizationAndPostEnabledUsers(organizationId, positionId);
    }

    @Override
    public List<UserDTO> organizationEnableUsers(Long organizationId) {

        return this.baseMapper.organizationUsers(organizationId);
    }

    @Override
    public void updatePassword(@NonNull Long id, @NonNull PasswordUpdateParam param) {
        String password = AESUtils.decrypt("comguheyuncloud1", param.getPassword());
        String newPassword = AESUtils.decrypt("comguheyuncloud1", param.getNewPassword());
        String secondNewPassword = AESUtils.decrypt("comguheyuncloud1", param.getSecondNewPassword());
        User user = this.getById(id);
        Assert.isNotFound(user, "用户不存在");
        //前置校验密码规则
        this.passwordValidated.validate(newPassword);
        Assert.isParameterError(!this.passwordEncoder.matches(password, user.getPassword()), "当前密码输入错误!");
        Assert.isParameterError(this.passwordEncoder.matches(newPassword, user.getPassword()), "新密码与当前密码一致!");
        Assert.isParameterError(!Objects.equals(newPassword, secondNewPassword), "新密码不相同!");
        user.setPassword(this.passwordEncoder.encode(newPassword));
        user.setValidity("PERMANENT");
        user.setLastUpdatePasswordTime(LocalDateTime.now());
        this.updateById(user);
    }

    @Override
    public void resetPassword(Long id, String newPassword) {
        String decryptNewPassword = AESUtils.decrypt("comguheyuncloud1", newPassword);
        //前置校验密码规则
        this.passwordValidated.validate(decryptNewPassword);
        User user = this.getById(id);
        user.setPassword(this.passwordEncoder.encode(decryptNewPassword));
        user.setValidity("PERMANENT");
        user.setLastUpdatePasswordTime(LocalDateTime.now());
        this.updateById(user);
    }

    @Override
    public Map<String, String> getUserOrganizationAndPostName(Long id) {
        HashMap<String, String> reMap = new HashMap<>();
        String userOrganizationName = userMapper.getUserOrganizationName(id);
        String userPostName = userMapper.getUserPostName(id);
        reMap.put("userOrganizationName", userOrganizationName);
        reMap.put("userPostName", userPostName);
        return reMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserTenant(@NonNull Integer tenant, @NonNull Long userId) {
        //删除用户与租户关系
        this.userTenantService.removeUserTenant(tenant, userId);
        List<Long> organizationIds = this.userOrganizationService.listUserOrganizations(Collections.singleton(userId)).stream()
                .map(userOrganization -> userOrganization.getId()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(organizationIds)) {
            //删除用户与租户的组织结构信息
            this.userOrganizationService.remove(Wrappers.<UserOrganization>lambdaQuery()
                                                        .eq(UserOrganization::getUserId, userId)
                                                        .in(UserOrganization::getOrganizationId, organizationIds));
        }
        List<Long> roleIds = this.userRoleService.listUserRoles(Collections.singleton(userId)).stream()
                .map(userRoleDTO -> userRoleDTO.getId()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(roleIds)) {
            //删除用户与租户的角色信息
            this.userRoleService.remove(Wrappers.<UserRole>lambdaQuery()
                                                .eq(UserRole::getUserId, userId)
                                                .in(UserRole::getRoleId, roleIds));
        }

    }

    @Override
    public List<UserDTO> getUserListByAppointmentIds(String appointmentIds) {
        if (ObjectUtils.isEmpty(appointmentIds)) {
            return new ArrayList<>();
        }
        return userMapper.getUserListByAppointmentIds(Arrays.asList(appointmentIds.split(",")).stream().map(id -> Long.parseLong(id))
                                                              .collect(Collectors.toList()));
    }

    @Override
    public void downloadUserTemplate(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        String fileNameEncode = null;
        try {
            fileNameEncode = URLEncoder.encode("用户导入模板", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ExcelTypeEnum.XLSX.getValue());
        Map<Integer, List<String>> dropdownData = new HashMap<>();
        List<Organization> organizations = organizationService.getAllOrganization();
        List<String> organizationNames = organizations.stream().map(Organization::getName).collect(Collectors.toList());
        List<Role> roles = roleService.list(new LambdaQueryWrapper<>());
        List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
        List<Appointment> appointments = appointmentQueryService.list();
        List<String> appointmentNames = appointments.stream().map(Appointment::getName).collect(Collectors.toList());
        List<Position> positions = positionService.list();
        List<String> positionNames = positions.stream().map(Position::getName).collect(Collectors.toList());
        dropdownData.put(2, Gender.getRemarks());
        dropdownData.put(3, organizationNames);
        dropdownData.put(4, organizationNames);
        dropdownData.put(5, appointmentNames);
        dropdownData.put(6, positionNames);
        dropdownData.put(7, roleNames);
        try {
            EasyExcelFactory.write(response.getOutputStream())
                    .registerWriteHandler(new SelectSheetWriteHandler(dropdownData))
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(EasyExcelUtil.getCustomHeaders(UserExcelVO.class))
                    .sheet("用户导入模板")
                    .doWrite(new ArrayList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void importUser(MultipartFile file) {
        // 创建Excel读取器并指定数据类型
        ExcelReaderBuilder reader = null;
        try {
            reader = EasyExcel.read(file.getInputStream());
        } catch (IOException e) {
            log.error("ouaa:导入用户读取excel失败{}", e);
            throw new ServiceException("文件读取失败");
        }
        // 读取第一个sheet的数据
        List<UserExcelVO> data = reader.sheet(0).head(UserExcelVO.class).doReadSync();
        Assert.isNotFound(CollectionUtils.isEmpty(data), "导入数据不能为空");
        List<Organization> organizations = organizationService.getAllOrganization();
        Map<String, Long> organizationNames = organizations.stream().collect(Collectors.toMap(Organization::getName, Organization::getId));
        List<Role> roles = roleService.list(new LambdaQueryWrapper<>());
        Map<String, Long> roleNames = roles.stream().collect(Collectors.toMap(Role::getName, Role::getId));
        List<Appointment> appointments = appointmentQueryService.list();
        Map<String, Long> appointmentNames = appointments.stream().collect(Collectors.toMap(Appointment::getName, Appointment::getId));
        List<Position> positions = positionService.list();
        Map<String, Long> positionNames = positions.stream().collect(Collectors.toMap(Position::getName, Position::getId));

        Set<Long> importedUserIds = new HashSet<>();
        data.forEach(ex -> {
            User user = ConvertUtils.convert(User.class, ex);
            Assert.isParameterError(ObjectUtils.isEmpty(user.getMobile()) || user.getMobile().length() != 11, "手机号必须为11位");
            Assert.isParameterError(ObjectUtils.isEmpty(user.getName()), "姓名不能为空");
            user.setUsername(user.getMobile());
            user.setGender(Gender.getByRemark(ex.getGender()));
            user.setAdmin(false);
            user.setEnabled(true);
            user.setPassword(passwordUtils.getInitializePassword());
            UserModifyParam param = new UserModifyParam();
            param.setEnabled(user.getEnabled());
            if (ObjectUtils.isNotEmpty(ex.getOrganizationName())) {
                param.setOrganizationIds(Arrays.asList(organizationNames.get(ex.getOrganizationName())));
            }
            if (ObjectUtils.isNotEmpty(ex.getRoleNames())) {
                param.setRoleIds(Arrays.asList(roleNames.get(ex.getRoleNames())));
            }
            if (ObjectUtils.isNotEmpty(ex.getPositionIds())) {
                param.setPositionIds(Arrays.asList(positionNames.get(ex.getPositionIds())));
            }
            if (ObjectUtils.isNotEmpty(ex.getAppointmentIds())) {
                param.setAppointmentIds(Arrays.asList(appointmentNames.get(ex.getAppointmentIds())));
            }
            if (ObjectUtils.isNotEmpty(ex.getManagedOrganizationIds())) {
                Set<Long> orgIds = new HashSet<>();
                orgIds.add(organizationNames.get(ex.getManagedOrganizationIds()));
                param.setManagedOrganizationIds(orgIds);
            }
            this.saveByTenant(user, param);
            importedUserIds.add(user.getId());
        });
        if (CollectionUtils.isNotEmpty(importedUserIds)) {
            userPermissionCache.cacheUserPermission(importedUserIds);
        }

    }

    @Override
    public String batchSyncPhotos(Integer tenantId) {
        SessionContextHolder.requestTenant(tenantId);
        List<User> userList = this.userMapper.selectList(new LambdaQueryWrapper<User>());
        userList.stream().forEach(user -> {
            try {
                if (ObjectUtils.isEmpty(user.getPhotographKey())) {
                    FileVO fileVO = fileClient.resourceBundle(user.getName() + ".png", "ouaa", "photo");
                    Long fileKey = IdWorker.getId();
                    FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
                    fileRefCreateParam.setFileId(fileVO.getId());
                    fileRefCreateParam.setBusinessKey(fileKey);
                    fileRefCreateParam.setSceneCode(OuaaFileSceneCodeEnum.OUAA_USER.getCode());
                    fileRefClient.refInduce(fileRefCreateParam);
                    user.setPhotographKey(fileKey);
                    userMapper.updateById(user);
                }
            } catch (Exception e) {
                log.error("ouaa:同步用户头像失败{}", e);
            }
        });
        return null;
    }

    @Override
    public Boolean userBelongsInstitution(UserBelongsInstitutionParam userBelongsInstitutionParam) {
        Set<Long> userIds = Collections.singleton(userBelongsInstitutionParam.getUserId());
        List<UserOrganizationDTO> userOrganizations = this.userOrganizationService.listUserOrganizations(userIds);
        if (CollectionUtils.isEmpty(userOrganizations) && !UserInstitutionType.ROLE.equals(userBelongsInstitutionParam.getType())) {
            return false;
        }
        //获取所以层级部门
        List<Long> ids = userOrganizations.stream().map(t -> {
            return t.getSequence().split("-");
        }).flatMap(Arrays::stream).map(Long::parseLong).collect(Collectors.toList());
        List<Organization> organizationList = this.organizationService.listOrganizations(ids);
        if (CollectionUtils.isEmpty(organizationList) && !UserInstitutionType.ROLE.equals(userBelongsInstitutionParam.getType())) {
            return false;
        }
        List<UserRoleDTO> roles = this.userRoleService.listUserRoles(userIds);
        if (CollectionUtils.isEmpty(roles) && UserInstitutionType.ROLE.equals(userBelongsInstitutionParam.getType())) {
            return false;
        }
        List<Long> institutionIds = userBelongsInstitutionParam.getInstitutionIds();
        boolean result = false;
        for (Long institutionId : institutionIds) {
            switch (userBelongsInstitutionParam.getType()) {
                case UserInstitutionType.DEPT:
                    List<Long> departmentIds = organizationList.stream().map(Organization::getId).collect(Collectors.toList());
                    result = departmentIds.contains(institutionId);
                    break;
                case UserInstitutionType.ROLE:
                    List<Long> roleIds = roles.stream().map(UserRoleDTO::getId).collect(Collectors.toList());
                    result = roleIds.contains(institutionId);
                    break;
                case UserInstitutionType.POST:
                    List<Long> positionIds = userOrganizations.stream().map(UserOrganizationDTO::getPositionId)
                            .collect(Collectors.toList());
                    result = positionIds.contains(institutionId);
                    break;
                case UserInstitutionType.APPOINTMENT:
                    List<Long> appointmentIds = userOrganizations.stream().map(UserOrganizationDTO::getAppointmentId)
                            .collect(Collectors.toList());
                    result = appointmentIds.contains(institutionId);
                    break;
            }
            if (result && UserInstitutionType.OR.equals(userBelongsInstitutionParam.getConditionType())) {
                break;
            } else if (!result && UserInstitutionType.AND.equals(userBelongsInstitutionParam.getType())) {
                break;
            }
        }
        return result;
    }

    @Override
    public Map<String, UserDTO> getTenantUserMap(UserMapQuery userQuery) {
        Map<String, UserDTO> userMap = new HashMap<>(10);
        List<User> userList = this.userMapper.selectList(new LambdaQueryWrapper<User>()
                                                                 .in(ObjectUtils.isNotEmpty(userQuery.getUserNames()), User::getUsername, userQuery.getUserNames())
                                                                 .in(ObjectUtils.isNotEmpty(userQuery.getUserIds()), User::getId, userQuery.getUserIds()));
        List<UserDTO> userDTOList = ConvertUtils.toList(UserDTO.class, userList);
        this.getUserExtend(userDTOList, userQuery.getInfoTypes());
        userDTOList.stream().forEach(ex -> {
            if (ObjectUtils.isNotEmpty(userQuery.getUserIds())) {
                userMap.put(ex.getId().toString(), ex);
            } else {
                userMap.put(ex.getUsername(), ex);
            }
        });
        return userMap;
    }

    @Override
    public Pagination<UserVO> getRegisterUserListForUserCenter(UserCenterQueryParam param) {
        Page<UserVO> userVOPage = userMapper.selectRegisterInfoPage(PagingUtils.convert(param), param);
        if (Objects.isNull(userVOPage) || CollectionUtils.isEmpty(userVOPage.getRecords())) {
            return new Pagination<>();
        }
        userVOPage.setRecords(userOrganizationService.encryptPhone(userVOPage.getRecords()));
        return PagingUtils.convert(userVOPage);
    }

    @Override
    public UserVO getRegisterUserDetailForUserCenter(Long id) {
        UserVO user = ObjectUtils.copying(this.getById(id), UserVO.class);
        if (Objects.isNull(user)) {
            return new UserVO();
        }
        // 获取用户头像
        List<FileVO> fileVO = this.fileClient.getByBusinessKey(user.getId());
        user.setFileVO(CollectionUtils.isEmpty(fileVO) ? null : fileVO.get(0));

        // 获取用户照片
        if (user.getPhotographKey() != null && user.getPhotographKey() > 0) {
            user.setPhotographList(fileClient.getByBusinessKey(user.getPhotographKey()));
        }
        return user;
    }

    @Override
    public void importUserSyncSuzhou(MultipartFile file) {
        // 创建Excel读取器并指定数据类型
        ExcelReaderBuilder reader = null;
        try {
            reader = EasyExcel.read(file.getInputStream());
        } catch (IOException e) {
            log.error("ouaa:导入用户读取excel失败{}", e);
            throw new ServiceException("文件读取失败");
        }
        // 读取第一个sheet的数据
        List<UserExcelVO> data = reader.sheet(0).head(UserExcelVO.class).doReadSync();
        Assert.isNotFound(CollectionUtils.isEmpty(data), "导入数据不能为空");
        List<Organization> organizations = organizationService.getAllOrganization();
        Map<String, Long> organizationNames = organizations.stream().collect(Collectors.toMap(Organization::getName, Organization::getId));
        Map<String, User> userMap = this.lambdaQuery().list().stream().collect(Collectors.toMap(User::getMobile, Function.identity()));
        for (UserExcelVO ex : data) {
            if (userMap.containsKey(ex.getMobile())) {
                if (StringUtils.isEmpty(ex.getIdCard())) {
                    continue;
                }
                this.lambdaUpdate()
                        .eq(User::getMobile, ex.getMobile())
                        .set(User::getIdCard, ex.getIdCard())
                        .update();
                continue;
            }
            User user = ConvertUtils.convert(User.class, ex);
            Assert.isParameterError(ObjectUtils.isEmpty(user.getMobile()) || user.getMobile().length() != 11, "手机号必须为11位");
            Assert.isParameterError(ObjectUtils.isEmpty(user.getName()), "姓名不能为空");
            user.setUsername(user.getMobile());
            user.setGender(Gender.getByRemark(ex.getGender()));
            user.setAdmin(false);
            user.setEnabled(true);
            user.setPassword(passwordUtils.getInitializePassword());
            UserModifyParam param = new UserModifyParam();
            if (ObjectUtils.isNotEmpty(ex.getOrganizationName())) {
                param.setOrganizationIds(Arrays.asList(organizationNames.get(ex.getOrganizationName())));
            }
            if (ObjectUtils.isNotEmpty(ex.getManagedOrganizationIds())) {
                Set<Long> orgIds = new HashSet<>();
                orgIds.add(organizationNames.get(ex.getManagedOrganizationIds()));
                param.setManagedOrganizationIds(orgIds);
            }
            this.saveByTenant(user, param);
        }

    }

    @Override
    public boolean isRegister(String username) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        User user = lambdaQuery().eq(User::getUsername, username).last("limit 1").one();
        return Objects.nonNull(user);
    }

    @Override
    public User getUserByMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return null;
        }
        return lambdaQuery().eq(User::getMobile, mobile).last("limit 1").one();

    }

    @Override
    public User getUserByAccount(String account) {

        return Optional
                .ofNullable(account)
                .map(accountStr -> lambdaQuery().eq(User::getUsername, account).last("limit 1").one())
                .orElse(null);
    }

    @Override
    public Map<Long, Set<Long>> findUserByRule(List<UserRuleConditionParam> ruleParam) {
        Map<Long, Set<Long>> result = new HashMap<>();
        if (CollectionUtils.isEmpty(ruleParam)) {
            return result;
        }
        Set<Long> userIds = new HashSet<>();
        Set<Long> departmentIds = new HashSet<>();
        Set<Long> roleIds = new HashSet<>();
        Set<Long> appointmentIds = new HashSet<>();
        for (UserRuleConditionParam conditionParam : ruleParam) {
            if (CollectionUtils.isNotEmpty(conditionParam.getAbnormalUserIdList())) {
                userIds.addAll(conditionParam.getAbnormalUserIdList());
            }
            for (var condition : conditionParam.getParticipateParams()) {
                if (ConditionTypeEnum.USER.equals(condition.getConditionTypeEnum())) {
                    userIds.add(condition.getTargetId());
                }
                if (ConditionTypeEnum.DEPT.equals(condition.getConditionTypeEnum())) {
                    departmentIds.add(condition.getTargetId());
                }
                if (ConditionTypeEnum.ROLE.equals(condition.getConditionTypeEnum())) {
                    roleIds.add(condition.getTargetId());
                }
                if (ConditionTypeEnum.POST.equals(condition.getConditionTypeEnum())) {
                    appointmentIds.add(condition.getTargetId());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(departmentIds)) {
            userIds.addAll(userOrganizationService.listEnableUserByOrgAndPost(new ArrayList<>(departmentIds), null));
        }
        if (CollectionUtils.isNotEmpty(roleIds)) {
            userIds.addAll(userRoleService.listUserIdsByRoleIds(roleIds));
        }
        if (CollectionUtils.isNotEmpty(appointmentIds)) {
            userIds.addAll(userOrganizationService.listEnableUserByOrgAndPost(null, new ArrayList<>(appointmentIds)));
        }
        userIds.removeIf(Objects::isNull);
        if (CollectionUtils.isEmpty(userIds)) {
            return result;
        }
        UserQuery userQuery = new UserQuery();
        userQuery.setUserIds(userIds);
        List<UserDTO> userDTOS = userMapper.selectUsers(userQuery, SessionContextHolder.getRequester().getTenant());
        for (var ruleConditionParam : ruleParam) {
            List<UserDTO> currentResult = new ArrayList<>();
            Map<ConditionTypeEnum, List<ConditionParam>> typeEnumListMap = ruleConditionParam.getParticipateParams().stream()
                    .collect(Collectors.groupingBy(ConditionParam::getConditionTypeEnum));
            for (var entry : typeEnumListMap.entrySet()) {
                Set<Long> ids = entry.getValue().stream().map(ConditionParam::getTargetId)
                        .collect(Collectors.toSet());

                if (ConditionTypeEnum.USER.equals(entry.getKey())) {

                    currentResult = userDTOS.stream().filter(t -> ids.contains(t.getId())).collect(Collectors.toList());

                }
                if (ConditionTypeEnum.DEPT.equals(entry.getKey())) {
                    currentResult = userDTOS.stream()
                            .filter(t -> Objects.nonNull(t.getOrganization()) && ids.contains(t.getOrganization().getId()))
                            .collect(Collectors.toList());
                }
                if (ConditionTypeEnum.ROLE.equals(entry.getKey())) {
                    currentResult = userDTOS.stream().filter(t -> Objects.nonNull(t.getRoles())
                                                                  && CollectionUtils.containsAny(ids, t.getRoles().stream()
                                    .map(UserDTO.Role::getId).collect(Collectors.toSet())))
                            .collect(Collectors.toList());

                }
                if (ConditionTypeEnum.POST.equals(entry.getKey())) {
                    currentResult = userDTOS.stream()
                            .filter(t -> Objects.nonNull(t.getRoles()) && CollectionUtils.containsAny(ids, t.getAppointments().stream()
                                    .map(UserDTO.Appointment::getId).collect(Collectors.toSet())))
                            .collect(Collectors.toList());
                }
            }
            // 过滤掉非在岗人员
            if (Boolean.TRUE.equals(ruleConditionParam.getOnDuty())) {

                currentResult = currentResult.stream().filter(t -> TenantUserWorkStatusEnum.WORKING.name().equals(t.getWorkStatus()))
                        .collect(Collectors.toList());
            }
            if (CollectionUtils.isEmpty(currentResult)) {
                currentResult = userDTOS.stream().filter(t -> CollectionUtils.isNotEmpty(ruleConditionParam.getAbnormalUserIdList())
                                                              && ruleConditionParam.getAbnormalUserIdList().contains(t.getId()))
                        .collect(Collectors.toList());
            }
            result.put(ruleConditionParam.getRuleId(), currentResult.stream().map(UserDTO::getId).collect(Collectors.toSet()));
        }
        return result;
    }

    private void getUserExtend(List<UserDTO> userList, Set<String> infoTypes) {
        if (ObjectUtils.isNotEmpty(infoTypes)) {
            // 扩展部门信息
            if (ObjectUtils.isNotEmpty(infoTypes.contains(InfoTypesEnum.ORGANIZATION.name()))) {
                Set<Long> userIds = userList.stream().map(UserDTO::getId).collect(Collectors.toSet());
                List<UserOrganizationDTO> userOrganizations = this.userOrganizationService.listUserOrganizations(userIds);
                Map<Long, List<UserOrganizationDTO>> userOrganizationGroups = userOrganizations.stream()
                        .collect(Collectors.groupingBy(UserOrganizationDTO::getUserId));
                Set<Long> organizationIds = new HashSet<>();
                userList.forEach(user -> {
                    List<UserOrganizationDTO> organizations =
                            ObjectUtils.ifNull(userOrganizationGroups.get(user.getId()), Collections.emptyList());
                    organizations.stream().findAny()
                            .ifPresent(organization ->
                                               organizationIds.addAll(Arrays.stream(organization.getSequence().split("-"))
                                                                              .map(Long::parseLong)
                                                                              .collect(Collectors.toList())));
                });
                List<Organization> organizationList;
                if (!CollectionUtils.isEmpty(organizationIds)) {
                    organizationList = Optional.ofNullable(this.organizationService.listOrganizations(new ArrayList<>(organizationIds)))
                            .orElse(new ArrayList<>());
                } else {
                    organizationList = new ArrayList<>();
                }

                userList.forEach(user -> {
                    // 设置用户组织机构
                    List<UserOrganizationDTO> organizations =
                            ObjectUtils.ifNull(userOrganizationGroups.get(user.getId()), Collections.emptyList());
                    UserOrganizationDTO organization = organizations.stream().findAny().orElse(null);
                    if (organization != null) {
                        List<Long> ids = Arrays.stream(organization.getSequence().split("-")).map(Long::parseLong)
                                .collect(Collectors.toList());
                        List<UserDTO.Organization> collect = organizationList.stream()
                                .filter(organization1 -> ids.contains(organization1.getId()))
                                .map(t -> ObjectUtils.copying(t, UserDTO.Organization.class))
                                .collect(Collectors.toList());
                        user.setOrganizations(collect);
                        user.setOrganization(ConvertUtils.convert(UserDTO.Organization.class, organization));
                    } else {
                        user.setOrganizations(Collections.emptyList());
                    }
                });
            }
        }
    }

}
