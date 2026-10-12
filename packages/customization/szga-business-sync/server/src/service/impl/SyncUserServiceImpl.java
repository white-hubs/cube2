package com.guhecloud.cube.srv.gen.business.info.sync.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.door.client.DoorDeviceClient;
import com.guhecloud.cube.biz.door.client.param.DoorDeviceQueryParam;
import com.guhecloud.cube.biz.door.client.vo.DoorDeviceVo;
import com.guhecloud.cube.biz.subsystem.parking.client.ParkingInfoClient;
import com.guhecloud.cube.biz.subsystem.parking.client.param.ParkingInfoQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.ParkingInfoVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.param.UserInfoParam;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.UserInfoVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.UserPermissionVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.User;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserDoorPermission;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserOneCard;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserParkingPermission;
import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.StatusEnum;
import com.guhecloud.cube.srv.gen.business.info.sync.server.mapper.SyncUserMapper;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserDoorPermissionService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserOneCardService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserParkingPermissionService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 15561
 * @description 针对表【user(系统用户表)】的数据库操作Service实现
 * @createDate 2024-12-17 16:11:56
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncUserServiceImpl extends ServiceImpl<SyncUserMapper, User>
        implements SyncUserService {
    private final SyncUserMapper syncUserMapper;
    private final SyncUserDoorPermissionService syncUserDoorPermissionService;
    private final SyncUserParkingPermissionService syncUserParkingPermissionService;
    private final SyncUserOneCardService syncUserOneCardService;
    private final DoorDeviceClient doorDeviceClient;
    private final ParkingInfoClient parkingInfoClient;

    @Override
    public List<UserInfoVO> getUserInfoListAll(String keyword, Period period) {
        return syncUserMapper.selectUserInfoAll(keyword, period);
    }

    @Override
    public Pagination<UserInfoVO> getUserInfoList(UserInfoParam userInfoParam) {
        Page<UserInfoVO> userInfoVOPage = this.baseMapper.selectUserInfoAll(PagingUtils.convert(userInfoParam), null, userInfoParam.getPeriod());
        return PagingUtils.convert(userInfoVOPage);
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {

        return syncUserMapper.selectUserInfoById(userId);
    }

    @Override
    public UserPermissionVO getUserPermissionById(String mobile) {
        UserPermissionVO userPermissionVO = new UserPermissionVO();
        User user = this.lambdaQuery()
                .eq(User::getMobile, mobile)
                .last("limit 1")
                .one();
        if (Objects.isNull(user)) {
            return userPermissionVO;
        }
        List<UserDoorPermission> userDoorPermissions = syncUserDoorPermissionService.lambdaQuery()
                .eq(UserDoorPermission::getUserId, user.getId())
                .eq(UserDoorPermission::getStatus, StatusEnum.OPEN.name())
                .list();
        if (CollectionUtils.isNotEmpty(userDoorPermissions)) {
            List<String> resouceCodeList = userDoorPermissions.stream().filter(t -> StringUtils.isNotBlank(t.getResourceCode()))
                    .map(UserDoorPermission::getResourceCode)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(resouceCodeList)) {
                DoorDeviceQueryParam doorDeviceQueryParam = new DoorDeviceQueryParam();
                doorDeviceQueryParam.setOutBusinessIdList(resouceCodeList);
                List<DoorDeviceVo> doorDeviceList = doorDeviceClient.getDoorDeviceList(doorDeviceQueryParam);
                userPermissionVO.setDoorDeviceList(doorDeviceList);
            }

        }
        List<UserParkingPermission> parkingPermissions = syncUserParkingPermissionService.lambdaQuery()
                .eq(UserParkingPermission::getUserId, user.getId())
                .eq(UserParkingPermission::getStatus, StatusEnum.OPEN.name())
                .list();
        if (CollectionUtils.isNotEmpty(parkingPermissions)) {
            Set<String> parkingLotCodeList = parkingPermissions.stream().filter(t -> StringUtils.isNotBlank(t.getParkinglotCode()))
                    .map(UserParkingPermission::getParkinglotCode)
                    .collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(parkingLotCodeList)) {
                ParkingInfoQueryParam param = new ParkingInfoQueryParam();
                LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>(parkingLotCodeList);
                param.setParkingInfoCodes(linkedHashSet);
                List<ParkingInfoVO> parkingInfos = parkingInfoClient.getParkingInfos(param);
                userPermissionVO.setParkingInfos(parkingInfos);
            }
        }
        UserOneCard userOneCard = syncUserOneCardService.lambdaQuery()
                .eq(UserOneCard::getUserId, user.getId())
                .last("limit 1")
                .one();
        userPermissionVO.setConsumePm(Objects.nonNull(userOneCard) && StatusEnum.OPEN.name().equals(userOneCard.getStatus()));
        return userPermissionVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean userPermissionModify(UserPermissionVO userPermissionVO) {
        User user = this.lambdaQuery()
                .eq(User::getMobile, userPermissionVO.getMobile())
                .last("limit 1")
                .one();
        if (Objects.isNull(user)) {
            return false;
        }
        var now = LocalDateTime.now();
        //一卡通权限更新
        List<UserOneCard> userOneCards = syncUserOneCardService.lambdaQuery()
                .eq(UserOneCard::getUserId, user.getId())
                .list();
        String oneCardStatus = userPermissionVO.getConsumePm() ? StatusEnum.OPEN.name() : StatusEnum.CANCEL.name();
        if (CollectionUtils.isNotEmpty(userOneCards)) {
            userOneCards.forEach(userOneCard -> {
                userOneCard.setStatus(oneCardStatus);
            });
            syncUserOneCardService.updateBatchById(userOneCards);
        } else {
            UserOneCard userOneCard = UserOneCard.builder()
                    .id(IdWorker.getId())
                    .userId(user.getId())
                    .status(oneCardStatus)
                    .tenant(SessionContextHolder.getRequester().getTenant())
                    .build();
            syncUserOneCardService.save(userOneCard);
        }
        if (CollectionUtils.isEmpty(userPermissionVO.getDoorDeviceList())) {
            syncUserDoorPermissionService.lambdaUpdate()
                    .eq(UserDoorPermission::getUserId, user.getId())
                    .set(UserDoorPermission::getStatus, StatusEnum.CANCEL.name())
                    .set(UserDoorPermission::getUpdateTime, now)
                    .update();
        } else {
            syncUserDoorPermissionService.remove(new LambdaQueryWrapper<UserDoorPermission>()
                                                         .eq(UserDoorPermission::getUserId, user.getId()));
            List<UserDoorPermission> userDoorPermissions = userPermissionVO.getDoorDeviceList()
                    .stream().map(t -> {
                        Assert.isNotAcceptable(StringUtils.isBlank(t.getOutBusinessId()), "门禁第三方设备编码为空，修改权限失败");
                        return UserDoorPermission.builder()
                                .userId(user.getId())
                                .resourceCode(t.getOutBusinessId())
                                .status(StatusEnum.OPEN.name())
                                .tenant(SessionContextHolder.getRequester().getTenant())
                                .build();
                    }).collect(Collectors.toList());
            syncUserDoorPermissionService.saveBatch(userDoorPermissions);
        }
        if (CollectionUtils.isEmpty(userPermissionVO.getParkingInfos())) {
            syncUserParkingPermissionService.lambdaUpdate()
                    .eq(UserParkingPermission::getUserId, user.getId())
                    .set(UserParkingPermission::getStatus, StatusEnum.CANCEL.name())
                    .set(UserParkingPermission::getUpdateTime, now)
                    .update();
        } else {
            syncUserParkingPermissionService.remove(new LambdaQueryWrapper<UserParkingPermission>()
                                                            .eq(UserParkingPermission::getUserId, user.getId()));
            List<UserParkingPermission> userParkingPermissions = userPermissionVO.getParkingInfos()
                    .stream().map(t -> {
                        return UserParkingPermission.builder()
                                .userId(user.getId())
                                .parkinglotCode(t.getCode())
                                .status(StatusEnum.OPEN.name())
                                .tenant(SessionContextHolder.getRequester().getTenant())
                                .build();
                    }).collect(Collectors.toList());
            syncUserParkingPermissionService.saveBatch(userParkingPermissions);
        }
        return true;
    }
}




