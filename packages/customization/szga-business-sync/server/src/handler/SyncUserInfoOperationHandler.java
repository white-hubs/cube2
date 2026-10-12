package com.guhecloud.cube.srv.gen.business.info.sync.server.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.business.info.sync.server.aspect.InfoUpdateWebHookAspect;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.User;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserDoorPermission;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserOneCard;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserOrganization;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserParkingPermission;
import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.Gender;
import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.PaperType;
import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.StatusEnum;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user.DeleteUserMessage;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user.IncreaseUserMessage;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user.UserUpdateMessage;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.DoorPermissionVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.InductionPO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.ParkingPermissionVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserDoorPermissionService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserOneCardService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserOrganizationService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserParkingPermissionService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 17:06
 * @Description: 用户信息操作消息处理器
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncUserInfoOperationHandler {
    private final SyncUserService userService;
    private final SyncUserOrganizationService userOrganizationService;
    private final SyncUserDoorPermissionService userDoorPermissionService;
    private final SyncUserParkingPermissionService userParkingPermissionService;
    private final SyncUserOneCardService userOneCardService;

    @Transactional(rollbackFor = Exception.class)
    public Long increaseUser(IncreaseUserMessage message) {
        User user = userService.lambdaQuery()
                .eq(User::getDeleted, 0)
                .eq(User::getMobile, message.getMobile())
                .one();
        Long userId;
        if (user != null) {
            this.updateUser(UserUpdateMessage.builder()
                                    .id(message.getId())
                                    .name(message.getName())
                                    .gender(message.getGender())
                                    .mobile(message.getMobile())
                                    .idCard(message.getIdCard())
                                    .organizationId(message.getOrganizationId())
                                    .status(message.getStatus())
                                    .cornet(message.getCornet())
                                    .enabled(true)
                                    .build());
            userId = user.getId();
        } else {
            userId = this.insertUserInfo(message);
        }
        return userId;
    }

    /**
     * 新增用户信息 (double check)
     * 入职审批新增不带用户id，业务服务监听完成后会带id，不一样以传入的为准
     *
     * @param message
     *
     * @return
     */
    private synchronized Long insertUserInfo(IncreaseUserMessage message) {
        User user = userService.lambdaQuery()
                .eq(User::getDeleted, 0)
                .eq(User::getMobile, message.getMobile())
                .one();
        Long userId;
        if (user != null && Objects.equals(user.getId(), message.getId())) {
            this.refreshUserId(message.getId(), user);
            userId = message.getId();
        } else {
            var now = LocalDateTime.now();
            //新增用户
            User insterUser = User.builder()
                    .id(Objects.nonNull(message.getId()) ? message.getId() : IdWorker.getId())
                    .name(message.getName())
                    .gender(Objects.nonNull(message.getGender()) ? message.getGender().name() : Gender.NONE.name())
                    .mobile(message.getMobile())
                    .paperType(PaperType.ID_CARD.name())
                    .paperNumber(message.getIdCard())
                    .enabled(1)
                    .status(StringUtils.isNotBlank(message.getStatus()) ? message.getStatus() : "FORMAL")
                    .cornet(message.getCornet())
                    .tenant(SessionContextHolder.getRequester().getTenant())
                    .createTime(now)
                    .updateTime(now)
                    .build();
            InfoUpdateWebHookAspect.CREATE_USER.set(Objects.isNull(message.getId()));
            userService.save(insterUser);
            insertUserOrganization(message.getOrganizationId(), insterUser, now);
            userId = insterUser.getId();
        }

        return userId;
    }

    /**
     * 刷新id
     *
     * @param ouaaId
     * @param user
     */
    private void refreshUserId(Long ouaaId, User user) {
        userService.lambdaUpdate()
                .eq(User::getDeleted, 0)
                .eq(User::getId, user.getId())
                .set(User::getId, ouaaId)
                .update();
        userOrganizationService.lambdaUpdate()
                .eq(UserOrganization::getUserId, user.getId())
                .eq(UserOrganization::getDeleted, 0)
                .set(UserOrganization::getUserId, ouaaId)
                .update();
        userDoorPermissionService.lambdaUpdate()
                .eq(UserDoorPermission::getUserId, user.getId())
                .eq(UserDoorPermission::getDeleted, 0)
                .set(UserDoorPermission::getUserId, ouaaId)
                .update();
        userParkingPermissionService.lambdaUpdate()
                .eq(UserParkingPermission::getUserId, user.getId())
                .eq(UserParkingPermission::getDeleted, 0)
                .set(UserParkingPermission::getUserId, ouaaId)
                .update();
        userOneCardService.lambdaUpdate()
                .eq(UserOneCard::getUserId, user.getId())
                .eq(UserOneCard::getDeleted, 0)
                .set(UserOneCard::getUserId, ouaaId)
                .update();
    }

    private void insertUserOrganization(Long orgId, User insterUser, LocalDateTime now) {
        //新增用户组织关系
        UserOrganization userOrganization = new UserOrganization();
        userOrganization.setUserId(insterUser.getId());
        userOrganization.setOrganizationId(orgId);
        userOrganization.setCreateTime(now);
        userOrganization.setUpdateTime(now);
        userOrganization.setTenant(SessionContextHolder.getRequester().getTenant());
        userOrganizationService.save(userOrganization);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long updateUser(UserUpdateMessage message) {
        User user = userService.lambdaQuery()
                .eq(User::getDeleted, 0)
                .eq(User::getMobile, message.getMobile()).one();
        if (user == null) {
            user = userService.getById(message.getId());
            if (user == null) {
                log.error("用户信息更新失败，用户不存在");
                return null;
            }
        }
        if (Objects.nonNull(message.getId()) && !Objects.equals(user.getId(), message.getId())) {
            this.refreshUserId(message.getId(), user);
            user.setId(message.getId());
        }
        userService.lambdaUpdate()
                .eq(User::getId, user.getId())
                .eq(User::getDeleted, 0)
                .set(StringUtils.isNotBlank(message.getName()), User::getName, message.getName())
                .set(StringUtils.isNotBlank(message.getMobile()), User::getMobile, message.getMobile())
                .set(Objects.nonNull(message.getGender()), User::getGender,
                     Objects.nonNull(message.getGender()) ? message.getGender().name() : null)
                .set(StringUtils.isNotBlank(message.getIdCard()), User::getPaperNumber, message.getIdCard())
                .set(Objects.nonNull(message.getEnabled()), User::getEnabled, message.getEnabled())
                .set(StringUtils.isNotBlank(message.getStatus()), User::getStatus, message.getStatus())
                .set(StringUtils.isNotBlank(message.getCornet()), User::getCornet, message.getCornet())
                .update();

        UserOrganization userOrganization = userOrganizationService.lambdaQuery()
                .eq(UserOrganization::getDeleted, 0)
                .eq(UserOrganization::getUserId, user.getId())
                .last(" limit 1")
                .one();
        if (Objects.isNull(userOrganization)) {
            insertUserOrganization(message.getOrganizationId(), user, LocalDateTime.now());
        } else {
            userOrganization.setOrganizationId(message.getOrganizationId());
            userOrganization.setUpdateTime(LocalDateTime.now());
            userOrganizationService.updateById(userOrganization);
        }

        //离职删除权限
        if ("LEAVE_OFFICE".equals(message.getStatus())) {
            canalPermission(user.getId(), false);
        }
        return user.getId();
    }

    /**
     * 移除权限
     *
     * @param id
     * @param delete
     */
    private void canalPermission(Long id, boolean delete) {
        long timeMillis = System.currentTimeMillis();
        userDoorPermissionService.lambdaUpdate()
                .eq(UserDoorPermission::getUserId, id)
                .set(UserDoorPermission::getStatus, StatusEnum.CANCEL.name())
                .set(delete, UserDoorPermission::getDeleted, timeMillis)
                .update();
        userParkingPermissionService.lambdaUpdate()
                .eq(UserParkingPermission::getUserId, id)
                .set(UserParkingPermission::getStatus, StatusEnum.CANCEL.name())
                .set(delete, UserParkingPermission::getDeleted, timeMillis)
                .update();
        userOneCardService.lambdaUpdate()
                .eq(UserOneCard::getUserId, id)
                .set(UserOneCard::getStatus, StatusEnum.CANCEL.name())
                .set(delete, UserOneCard::getDeleted, timeMillis)
                .update();

        //todo 是否删除待确定
    }

    @Transactional(rollbackFor = Exception.class)
    public Long deleteUserMessage(DeleteUserMessage message) {
        User user = userService.lambdaQuery()
                .eq(User::getDeleted, 0)
                .eq(User::getMobile, message.getMobile()).one();
        if (user == null) {
            user = userService.lambdaQuery()
                    .eq(User::getDeleted, 0)
                    .eq(User::getId, message.getId()).one();
        }
        if (user == null) {
            log.error("用户信息删除失败，用户不存在");
            return null;
        }
        long timeMillis = System.currentTimeMillis();
        SessionContextHolder.requestTenant(user.getTenant());
        userService.lambdaUpdate()
                .eq(User::getId, user.getId())
                .set(User::getEnabled, false)
                .set(User::getDeleted, timeMillis)
                .update();
        List<UserOrganization> userOrganizationList = userOrganizationService
                .lambdaQuery()
                .eq(UserOrganization::getDeleted, 0)
                .eq(UserOrganization::getUserId, user.getId()).list();
        if (CollectionUtils.isEmpty(userOrganizationList)) {
            log.error("用户信息删除失败，用户组织关系不存在");
        } else {
            userOrganizationService.removeByIds(userOrganizationList.stream().map(UserOrganization::getId).collect(Collectors.toList()));
        }
        //删除权限
        canalPermission(user.getId(), true);
        return user.getId();
    }

    /**
     * 入职审批流程通过
     *
     * @param inductionPO
     */

    @Transactional(rollbackFor = Exception.class)
    public Long inductionComplete(InductionPO inductionPO) {
        User user = userService.lambdaQuery().eq(User::getName, inductionPO.getStaffName())
                .eq(User::getDeleted, 0)
                .eq(User::getMobile, inductionPO.getMobile())
                .one();
        Long userId;
        if (user != null) {
            this.updateUser(UserUpdateMessage.builder()
                                    .name(inductionPO.getStaffName())
                                    .mobile(inductionPO.getMobile())
                                    .idCard(inductionPO.getIdCard())
                                    .organizationId(CollectionUtils.isNotEmpty(inductionPO.getOrganizationIdList())
                                                    ? inductionPO.getOrganizationIdList().get(0) : null)
                                    .gender(Gender.getGender(inductionPO.getGender()))
                                    .build());
            userId = user.getId();
        } else {
            userId = this.increaseUser(IncreaseUserMessage.builder()
                                               .name(inductionPO.getStaffName())
                                               .mobile(inductionPO.getMobile())
                                               .idCard(inductionPO.getIdCard())
                                               .organizationId(CollectionUtils.isNotEmpty(inductionPO.getOrganizationIdList())
                                                               ? inductionPO.getOrganizationIdList().get(0) : null)
                                               .gender(Gender.getGender(inductionPO.getGender()))
                                               .build());
        }
        if (CollectionUtils.isEmpty(inductionPO.getUserPermissionList())) {
            return userId;
        }
        List<InductionPO.UserPermission> userPermissionList = inductionPO.getUserPermissionList();
        List<UserDoorPermission> userDoorPermissions = new ArrayList<>();

        List<UserOneCard> userOneCards = new ArrayList<>();

        List<UserParkingPermission> userParkingPermissions = userParkingPermissionService.lambdaQuery()
                .eq(UserParkingPermission::getUserId, userId)
                .list();
        boolean consumePm = false;
        for (InductionPO.UserPermission userPermission : userPermissionList) {
            //门禁权限
            buildDoorPermission(userPermission.getDoorPm(), userId, userDoorPermissions);
            //车辆权限
            buildParkingPermission(userPermission.getParkingPm(), userId, userParkingPermissions);
            if (Objects.nonNull(userPermission.getConsumePm()) && userPermission.getConsumePm()) {
                consumePm = true;
            }
        }
        //消费权限
        if (consumePm) {
            buildOneCard(inductionPO, userId, userOneCards);
        }
        if (CollectionUtils.isNotEmpty(userDoorPermissions)) {
            userDoorPermissionService.remove(new QueryWrapper<UserDoorPermission>().lambda().eq(UserDoorPermission::getUserId, userId));
            userDoorPermissionService.saveBatch(userDoorPermissions);
        }
        if (CollectionUtils.isNotEmpty(userParkingPermissions)) {
            //更新
            List<UserParkingPermission> updateParkingPermissionList = userParkingPermissions.stream()
                    .filter(t -> Objects.nonNull(t.getId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(updateParkingPermissionList)) {
                userParkingPermissionService.updateBatchById(updateParkingPermissionList);
            }
            //新增
            List<UserParkingPermission> parkingPermissionList = userParkingPermissions.stream().filter(t -> Objects.isNull(t.getId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(parkingPermissionList)) {
                parkingPermissionList.forEach(t -> {
                    t.setId(IdWorker.getId());
                });
                userParkingPermissionService.saveBatch(parkingPermissionList);
            }

        }
        if (CollectionUtils.isNotEmpty(userOneCards)) {
            userOneCardService.remove(new QueryWrapper<UserOneCard>().lambda().eq(UserOneCard::getUserId, userId));
            userOneCardService.saveBatch(userOneCards);
        }
        return userId;
    }

    /**
     * 一卡通权限
     *
     * @param inductionPO
     * @param userOneCards
     *
     * @return
     */
    private void buildOneCard(InductionPO inductionPO, Long userId, List<UserOneCard> userOneCards) {
        //暂定物理卡号由大华写入
        userOneCards.add(UserOneCard.builder()
                                 .id(IdWorker.getId())
                                 .userId(userId)
                                 .validityDate(inductionPO.getServicePeriod())
                                 .status(StatusEnum.OPEN.name())
                                 .tenant(SessionContextHolder.getRequester().getTenant())
                                 .build());
    }

    /**
     * 停车权限
     *
     * @param parkingPm
     * @param userParkingPermissions
     *
     * @return
     */
    private void buildParkingPermission(ParkingPermissionVO parkingPm, Long userId, List<UserParkingPermission> userParkingPermissions) {

        if (Objects.isNull(parkingPm) ||
            Objects.isNull(parkingPm.getParkingControl()) || !parkingPm.getParkingControl() ||
            CollectionUtils.isEmpty(parkingPm.getParkingControlRows())) {
            return;
        }
        //根据第三方id去重
        Set<ParkingPermissionVO.ParkingControlRow> controlRows = parkingPm.getParkingControlRows().stream().collect(collectingAndThen(
                toCollection(() -> new TreeSet<>(Comparator.comparing(ParkingPermissionVO.ParkingControlRow::getCode))),
                HashSet::new));

        List<ParkingPermissionVO.ParkingControlRow> controlRowList = new ArrayList<>(controlRows);

        //车辆已经录入,直接绑定停车场
        if (CollectionUtils.isNotEmpty(userParkingPermissions)) {
            for (int i = 0; i < userParkingPermissions.size(); i++) {
                userParkingPermissions.get(i).setParkinglotCode(controlRowList.remove(i).getCode());
            }
        }
        if (CollectionUtils.isNotEmpty(controlRowList)) {
            controlRowList.forEach(t -> {
                userParkingPermissions.add(UserParkingPermission.builder()
                                                   .userId(userId)
                                                   .parkinglotCode(t.getCode())
                                                   .tenant(SessionContextHolder.getRequester().getTenant())
                                                   .build());
            });
        }
    }

    /**
     * 门禁权限
     *
     * @param doorPm
     * @param userDoorPermissions
     *
     * @return
     */
    private void buildDoorPermission(DoorPermissionVO doorPm, Long userId, List<UserDoorPermission> userDoorPermissions) {
        if (Objects.isNull(doorPm.getAccessControl()) || !doorPm.getAccessControl() ||
            CollectionUtils.isEmpty(doorPm.getAccessControlRows())) {
            log.error("门禁权限为空");
            return;
        }
        //使用第三方设备id写入
        for (DoorPermissionVO.AccessControlRow accessControlRow : doorPm.getAccessControlRows()) {
            userDoorPermissions.add(UserDoorPermission.builder()
                                            .userId(userId)
                                            .resourceCode(CollectionUtils.isNotEmpty(doorPm.getAccessControlRows())
                                                          ? accessControlRow.getOutBusinessId() : null)
                                            .status(StatusEnum.OPEN.name())
                                            .tenant(SessionContextHolder.getRequester().getTenant())
                                            .build());
        }
    }
}
