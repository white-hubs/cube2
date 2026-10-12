package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.UserTenantMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.TenantUserWorkStatusDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserTenantDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.enumeration.TenantUserWorkStatusEnum;
import com.guhecloud.cube.biz.ouaa.server.enumeration.UserTenantBeloneEnum;
import com.guhecloud.cube.biz.ouaa.server.param.RenewWorkStatusBatchParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserTenantQueryParam;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.TenantUserWorkStatusMessage;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户租户业务操作实现
 *
 * @author Woody
 * @date 2021/7/13
 */
@Service
public class UserTenantServiceImpl extends ServiceImpl<UserTenantMapper, UserTenant> implements UserTenantService {
    @Override
    public void addUserTenant(@NonNull Integer tenant, @NonNull Long userId) {
        List<UserTenant> userTenants = this.lambdaQuery().eq(UserTenant::getTenant, tenant).eq(UserTenant::getUserId, userId).list();
        if (CollectionUtils.isEmpty(userTenants)) {
            UserTenant userTenant = UserTenant.builder().tenant(tenant).userId(userId).build();
            this.save(userTenant);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserTenantBatch(@NonNull Integer tenant, @NonNull List<Long> userIds) {
        var existingUserTenants = this.lambdaQuery()
                .eq(UserTenant::getTenant, tenant)
                .in(UserTenant::getUserId, userIds)
                .list()
                .stream().map(UserTenant::getUserId)
                .collect(Collectors.toSet());
        var addUsers = userIds.stream()
                .filter(userId -> !existingUserTenants.contains(userId))
                .map(userId -> UserTenant.builder().tenant(tenant).userId(userId).build())
                .collect(Collectors.toList());
        this.saveBatch((List<UserTenant>) addUsers);
    }

    @Override
    public void removeUserTenant(@NonNull Integer tenant, @NonNull Long userId) {
        UserTenant userTenant = this.lambdaQuery().eq(UserTenant::getTenant, tenant).eq(UserTenant::getUserId, userId).one();
        var userTenantBelong = userTenant.getBelongTenant();
        if (StringUtils.isNoneBlank(userTenantBelong)) {
            userTenant.setBelongTenant(UserTenantBeloneEnum.NOT_BELONG.name());
            updateById(userTenant);
            return;
        }
        this.removeById(userTenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserTenantByUserId(@NonNull Long userId, @NonNull Boolean enabled) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        UserTenant userTenant = UserTenant.builder().userId(userId).tenant(tenant).enabled(enabled)
                .belongTenant(UserTenantBeloneEnum.BELONG.name()).build();
        userTenant.setAdmin(false);
        this.baseMapper.insert(userTenant);
    }

    @Override
    public UserTenant getUserTenantAdmin(@NonNull Integer tenant) {
        var admins = this.lambdaQuery().eq(UserTenant::getTenant, tenant).eq(UserTenant::getAdmin, true).list();
        if (CollectionUtils.isEmpty(admins)) {
            return null;
        }
        return admins.getFirst();
    }

    @Override
    public List<UserTenant> listUserTenants(@NonNull Integer tenant) {
        return this.lambdaQuery().eq(UserTenant::getTenant, tenant).list();
    }

    @Override
    public Pagination<UserTenant> pageUserTenants(Integer tenant, UserTenantQueryParam param) {
        var result = this.lambdaQuery()
                .eq(UserTenant::getTenant, tenant)
                .and(Objects.nonNull(param.getEnabled()), (l) -> {
                    if (Optional.ofNullable(param.getEnabled()).orElse(true)) {
                        l.eq(UserTenant::getEnabled, Boolean.TRUE).or().isNull(UserTenant::getEnabled);
                    } else {
                        l.eq(UserTenant::getEnabled, Boolean.FALSE);
                    }
                })
                .eq(Objects.nonNull(param.getAdmin()), UserTenant::getAdmin, param.getAdmin())
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(result);
    }

    @Override
    public List<UserTenant> listUserTenantByUserIds(Collection<? extends Serializable> userIds, @NonNull Integer tenant) {
        return this.lambdaQuery().in(!CollectionUtils.isEmpty(userIds), UserTenant::getUserId, userIds)
                .eq(UserTenant::getTenant, tenant).list();
    }

    @Override
    public List<UserTenant> getByUserId(@NonNull Long userId) {
        return this.lambdaQuery().eq(UserTenant::getUserId, userId).eq(UserTenant::getAdmin, true).list();
    }

    @Override
    public UserTenant getUserTenantByUserIdAndTenant(@NonNull Long userId, @NonNull Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<UserTenant>lambdaQuery().eq(UserTenant::getUserId, userId)
                                                 .eq(UserTenant::getTenant, tenant));
    }

    @Override
    public List<UserTenant> listUserTenants(@NonNull Long userId) {
        return this.lambdaQuery().eq(UserTenant::getUserId, userId).list();
    }

    @Override
    public List<UserTenantDTO> getTenantUserWorkStatus(Integer tenant, Set<Long> userIds) {
        List<UserTenant> userTenantList = this.list(Wrappers.<UserTenant>lambdaQuery()
                                                            .eq(UserTenant::getTenant, tenant)
                                                            .in(UserTenant::getUserId, userIds)
                                                   );
        return ObjectUtils.copying(userTenantList, UserTenantDTO.class);
    }

    @Override
    public void batchUpdateTenantUserWorkStatus(RenewWorkStatusBatchParam param) {
        TenantUserWorkStatusMessage statusMessage = new TenantUserWorkStatusMessage();
        statusMessage.setTenant(SessionContextHolder.getRequester().getTenant());
        List<TenantUserWorkStatusDTO> workStatusDTOS = new ArrayList<>();
        if (param == null || param.getTenant() == null) {
            return;
        }
        if (param.getResetAllStatus() != null && Boolean.TRUE.equals(param.getResetAllStatus())) {
            this.update(null, Wrappers.<UserTenant>lambdaUpdate()
                                .eq(UserTenant::getTenant, param.getTenant())
                                .set(UserTenant::getWorkStatus, null)
                       );
        }
        if (param.getUserIdList() != null && !param.getUserIdList().isEmpty()) {
            this.update(null, Wrappers.<UserTenant>lambdaUpdate()
                                .eq(UserTenant::getTenant, param.getTenant())
                                .in(UserTenant::getUserId, param.getUserIdList())
                                .set(UserTenant::getWorkStatus, param.getWorkStatus())
                       );
            for (Long aLong : param.getUserIdList()) {
                TenantUserWorkStatusDTO tenantUserWorkStatusDTO = new TenantUserWorkStatusDTO();
                tenantUserWorkStatusDTO.setUserId(aLong);
                tenantUserWorkStatusDTO.setWorkStatus(param.getWorkStatus());
                workStatusDTOS.add(tenantUserWorkStatusDTO);
            }
            statusMessage.setUpdateWorkStatusList(workStatusDTOS);
            RocketMQContextHolder.send(TenantUserWorkStatusMessage.TOPIC, statusMessage);
            // 如果是确定更新就不进行下面的每一项更新了
            return;
        }

        // 更新请假
        if (param.getLeaveStaffList() != null && !param.getLeaveStaffList().isEmpty()) {
            // 查询不为请假状态的人(避免一直重复update
            List<UserTenant> notAskLeaveList = this.list(Wrappers.<UserTenant>lambdaQuery()
                                                                 .eq(UserTenant::getTenant, param.getTenant())
                                                                 .in(UserTenant::getUserId, param.getLeaveStaffList())
                                                                 .and(
                                                                         i -> i
                                                                                 .ne(UserTenant::getWorkStatus, TenantUserWorkStatusEnum.ASK_LEAVE.name())
                                                                                 .or()
                                                                                 .isNull(UserTenant::getWorkStatus)
                                                                     )

                                                        );
            List<Long> updList = null;
            if (notAskLeaveList != null && !notAskLeaveList.isEmpty()) {
                updList = notAskLeaveList.stream().map(UserTenant::getUserId).collect(Collectors.toList());
            }
            if (updList != null && !updList.isEmpty()) {
                // 批量更新人员工作状态
                this.update(null, Wrappers.<UserTenant>lambdaUpdate()
                                    .eq(UserTenant::getTenant, param.getTenant())
                                    .in(UserTenant::getUserId, param.getLeaveStaffList())
                                    .set(UserTenant::getWorkStatus, TenantUserWorkStatusEnum.ASK_LEAVE.name())
                           );
                for (Long aLong : updList) {
                    TenantUserWorkStatusDTO tenantUserWorkStatusDTO = new TenantUserWorkStatusDTO();
                    tenantUserWorkStatusDTO.setUserId(aLong);
                    tenantUserWorkStatusDTO.setWorkStatus(TenantUserWorkStatusEnum.ASK_LEAVE.name());
                    workStatusDTOS.add(tenantUserWorkStatusDTO);
                }
            }
        }
        // 更新出差
        if (param.getAwayStaffList() != null && !param.getAwayStaffList().isEmpty()) {
            // 查询不为出差状态的人(避免一直重复update
            List<UserTenant> notLeaveAwayList = this.list(Wrappers.<UserTenant>lambdaQuery()
                                                                  .eq(UserTenant::getTenant, param.getTenant())
                                                                  .in(UserTenant::getUserId, param.getAwayStaffList())
                                                                  .and(
                                                                          i -> i
                                                                                  .ne(UserTenant::getWorkStatus, TenantUserWorkStatusEnum.AWAY_BUSINESS.name())
                                                                                  .or()
                                                                                  .isNull(UserTenant::getWorkStatus)
                                                                      )
                                                         );
            List<Long> updList = null;
            if (notLeaveAwayList != null && !notLeaveAwayList.isEmpty()) {
                updList = notLeaveAwayList.stream().map(UserTenant::getUserId).collect(Collectors.toList());
            }
            if (updList != null && !updList.isEmpty()) {
                // 批量更新人员工作状态
                this.update(null, Wrappers.<UserTenant>lambdaUpdate()
                                    .eq(UserTenant::getTenant, param.getTenant())
                                    .in(UserTenant::getUserId, param.getAwayStaffList())
                                    .set(UserTenant::getWorkStatus, TenantUserWorkStatusEnum.AWAY_BUSINESS.name())
                           );
                for (Long aLong : updList) {
                    TenantUserWorkStatusDTO tenantUserWorkStatusDTO = new TenantUserWorkStatusDTO();
                    tenantUserWorkStatusDTO.setUserId(aLong);
                    tenantUserWorkStatusDTO.setWorkStatus(TenantUserWorkStatusEnum.AWAY_BUSINESS.name());
                    workStatusDTOS.add(tenantUserWorkStatusDTO);
                }
            }

        }
        // 更新离岗
        if (param.getPostStaffList() != null && !param.getPostStaffList().isEmpty()) {
            // 查询不为离岗状态的人(避免一直重复update
            List<UserTenant> notLeavePostList = this.list(Wrappers.<UserTenant>lambdaQuery()
                                                                  .eq(UserTenant::getTenant, param.getTenant())
                                                                  .in(UserTenant::getUserId, param.getPostStaffList())
                                                                  .and(
                                                                          i -> i
                                                                                  .ne(UserTenant::getWorkStatus, TenantUserWorkStatusEnum.LEAVE_POST.name())
                                                                                  .or()
                                                                                  .isNull(UserTenant::getWorkStatus)
                                                                      )
                                                         );
            List<Long> updList = null;
            if (notLeavePostList != null && !notLeavePostList.isEmpty()) {
                updList = notLeavePostList.stream().map(UserTenant::getUserId).collect(Collectors.toList());
            }
            if (updList != null && !updList.isEmpty()) {
                // 批量更新人员工作状态
                this.update(null, Wrappers.<UserTenant>lambdaUpdate()
                                    .eq(UserTenant::getTenant, param.getTenant())
                                    .in(UserTenant::getUserId, param.getPostStaffList())
                                    .set(UserTenant::getWorkStatus, TenantUserWorkStatusEnum.LEAVE_POST.name())
                           );
                for (Long aLong : updList) {
                    TenantUserWorkStatusDTO tenantUserWorkStatusDTO = new TenantUserWorkStatusDTO();
                    tenantUserWorkStatusDTO.setUserId(aLong);
                    tenantUserWorkStatusDTO.setWorkStatus(TenantUserWorkStatusEnum.LEAVE_POST.name());
                    workStatusDTOS.add(tenantUserWorkStatusDTO);
                }
            }

        }
        // 更新工作
        if (param.getWorkStaffList() != null && !param.getWorkStaffList().isEmpty()) {
            // 查询不为工作中的人(避免一直重复update
            List<UserTenant> notWorkingList = this.list(Wrappers.<UserTenant>lambdaQuery()
                                                                .eq(UserTenant::getTenant, param.getTenant())
                                                                .in(UserTenant::getUserId, param.getWorkStaffList())
                                                                .and(
                                                                        i -> i
                                                                                .ne(UserTenant::getWorkStatus, TenantUserWorkStatusEnum.WORKING.name())
                                                                                .or()
                                                                                .isNull(UserTenant::getWorkStatus)
                                                                    )
                                                       );
            List<Long> updList = null;
            if (notWorkingList != null && !notWorkingList.isEmpty()) {
                updList = notWorkingList.stream().map(UserTenant::getUserId).collect(Collectors.toList());
            }
            if (updList != null && !updList.isEmpty()) {
                // 批量更新人员工作状态
                this.update(null, Wrappers.<UserTenant>lambdaUpdate()
                                    .eq(UserTenant::getTenant, param.getTenant())
                                    .in(UserTenant::getUserId, param.getWorkStaffList())
                                    .set(UserTenant::getWorkStatus, TenantUserWorkStatusEnum.WORKING.name())
                           );
                for (Long aLong : updList) {
                    TenantUserWorkStatusDTO tenantUserWorkStatusDTO = new TenantUserWorkStatusDTO();
                    tenantUserWorkStatusDTO.setUserId(aLong);
                    tenantUserWorkStatusDTO.setWorkStatus(TenantUserWorkStatusEnum.WORKING.name());
                    workStatusDTOS.add(tenantUserWorkStatusDTO);
                }
            }

        }
        // 更新休息
        if (param.getRestStaffList() != null && !param.getRestStaffList().isEmpty()) {
            // 查询不为工作中的人(避免一直重复update
            List<UserTenant> notRestList = this.list(Wrappers.<UserTenant>lambdaQuery()
                                                             .eq(UserTenant::getTenant, param.getTenant())
                                                             .in(UserTenant::getUserId, param.getRestStaffList())
                                                             .and(
                                                                     i -> i
                                                                             .ne(UserTenant::getWorkStatus, TenantUserWorkStatusEnum.REST.name())
                                                                             .or()
                                                                             .isNull(UserTenant::getWorkStatus)
                                                                 )

                                                    );
            List<Long> updList = null;
            if (notRestList != null && !notRestList.isEmpty()) {
                updList = notRestList.stream().map(UserTenant::getUserId).collect(Collectors.toList());
            }
            if (updList != null && !updList.isEmpty()) {
                // 批量更新人员工作状态
                this.update(null, Wrappers.<UserTenant>lambdaUpdate()
                                    .eq(UserTenant::getTenant, param.getTenant())
                                    .in(UserTenant::getUserId, param.getRestStaffList())
                                    .set(UserTenant::getWorkStatus, TenantUserWorkStatusEnum.REST.name())
                           );
                for (Long aLong : updList) {
                    TenantUserWorkStatusDTO tenantUserWorkStatusDTO = new TenantUserWorkStatusDTO();
                    tenantUserWorkStatusDTO.setUserId(aLong);
                    tenantUserWorkStatusDTO.setWorkStatus(TenantUserWorkStatusEnum.REST.name());
                    workStatusDTOS.add(tenantUserWorkStatusDTO);
                }
            }

        }
        if (!CollectionUtils.isEmpty(workStatusDTOS)) {
            statusMessage.setUpdateWorkStatusList(workStatusDTOS);
            RocketMQContextHolder.send(TenantUserWorkStatusMessage.TOPIC, statusMessage);
        }
    }

    @Override
    public void updateUserTenantEnable(long userId, Boolean userTenantEnable) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        updateUserTenantEnable(tenant, userId, userTenantEnable);
    }

    @Override
    public void updateUserTenantEnable(Integer tenant, long userId, Boolean userTenantEnable) {
        lambdaUpdate()
                .set(UserTenant::getEnabled, userTenantEnable)
                .eq(UserTenant::getTenant, tenant)
                .eq(UserTenant::getUserId, userId)
                .update();
    }
}
