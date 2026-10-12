package com.guhecloud.cube.srv.gen.business.info.sync.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserParkingPermission;
import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.StatusEnum;
import com.guhecloud.cube.srv.gen.business.info.sync.server.mapper.SyncUserParkingPermissionMapper;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.VechicleApprovalUpdateVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.VechicleApprovalVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.VechicleDeleteVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserParkingPermissionService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pair;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 15561
 * @description 针对表【user_parking_permission(停车权限)】的数据库操作Service实现
 * @createDate 2024-12-17 16:14:05
 */
@Service
public class SyncUserParkingPermissionServiceImpl extends ServiceImpl<SyncUserParkingPermissionMapper, UserParkingPermission>
        implements SyncUserParkingPermissionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void vehicleInsert(VechicleApprovalVO vechicleApprovalVO) {
        /**
         * 当前停车场唯一
         */
        if (CollectionUtils.isEmpty(vechicleApprovalVO.getUserId()) || CollectionUtils.isEmpty(vechicleApprovalVO.getVehicleNos())) {
            return;
        }
        Long userId = vechicleApprovalVO.getUserId().get(0);
        List<String> vechicleNoList = vechicleApprovalVO.getVehicleNos().stream()
                .map(VechicleApprovalVO.VechicleNo::getVehicleNo).collect(Collectors.toList());
        List<UserParkingPermission> userParkingPermissions = this.lambdaQuery().eq(UserParkingPermission::getUserId, userId)
                .eq(UserParkingPermission::getDeleted, 0)
                .list();
        List<String> insterVechicleNoList = vechicleNoList.stream().filter(vechicleNo -> userParkingPermissions.stream()
                        .noneMatch(userParkingPermission -> StringUtils.equals(userParkingPermission.getVehicleNo(), vechicleNo)))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(insterVechicleNoList)) {
            List<UserParkingPermission> userParkingPermissionList = new ArrayList<>();

            for (UserParkingPermission userParkingPermission : userParkingPermissions) {
                if (CollectionUtils.isNotEmpty(insterVechicleNoList) && StringUtils.isBlank(userParkingPermission.getVehicleNo())) {
                    userParkingPermission.setVehicleNo(insterVechicleNoList.remove(0));
                    userParkingPermissionList.add(userParkingPermission);
                }
            }
            if (CollectionUtils.isNotEmpty(userParkingPermissionList)) {
                this.updateBatchById(userParkingPermissionList);
            }
        }
        if (CollectionUtils.isNotEmpty(insterVechicleNoList)) {
            List<UserParkingPermission> parkingPermissionList = insterVechicleNoList.stream().map(vechicleNo -> {
                return UserParkingPermission.builder()
                        .id(IdWorker.getId())
                        .userId(userId)
                        .vehicleNo(vechicleNo)
                        .parkinglotCode(
                                CollectionUtils.isNotEmpty(userParkingPermissions) ? userParkingPermissions.get(0).getParkinglotCode()
                                                                                   : null)
                        .status(StatusEnum.OPEN.name())
                        .tenant(SessionContextHolder.getRequester().getTenant())
                        .build();
            }).collect(Collectors.toList());
            this.saveBatch(parkingPermissionList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("CollectionIncompatibleType")
    public void vehicleUpdate(VechicleApprovalUpdateVO vechicleApprovalUpdateVO) {
        if (Objects.isNull(vechicleApprovalUpdateVO.getUserId()) ||
            CollectionUtils.isEmpty(vechicleApprovalUpdateVO.getVechicleChanges())) {
            return;
        }
        List<String> afterVehicleNos = vechicleApprovalUpdateVO.getVechicleChanges().stream()
                .map(VechicleApprovalUpdateVO.VechicleChangeDTO::getBeforeVehicleNo)
                .collect(Collectors.toList());
        List<UserParkingPermission> userParkingPermissions = this.lambdaQuery()
                .eq(UserParkingPermission::getDeleted, 0)
                .eq(UserParkingPermission::getUserId, vechicleApprovalUpdateVO.getUserId())
                .in(UserParkingPermission::getVehicleNo, afterVehicleNos)
                .list();
        if (CollectionUtils.isNotEmpty(userParkingPermissions)) {
            Map<Pair<Long, String>, UserParkingPermission> pairUserParkingPermissionMap = userParkingPermissions.stream()
                    .collect(Collectors.toMap(t ->
                                                      Pair.of(t.getUserId(), t.getVehicleNo()), Function.identity(), (k1, k2) -> k1));
            List<UserParkingPermission> updateUserParkingPermissionList = new ArrayList<>();
            vechicleApprovalUpdateVO.getVechicleChanges().forEach(vo -> {
                if (pairUserParkingPermissionMap.containsKey(Pair.of(vechicleApprovalUpdateVO.getUserId(), vo.getBeforeVehicleNo()))) {
                    UserParkingPermission userParkingPermission = pairUserParkingPermissionMap
                            .get(Pair.of(vechicleApprovalUpdateVO.getUserId(), vo.getBeforeVehicleNo()));
                    userParkingPermission.setVehicleNo(vo.getAfterVehicleNo());
                    updateUserParkingPermissionList.add(userParkingPermission);
                }
            });
            if (CollectionUtils.isNotEmpty(updateUserParkingPermissionList)) {
                this.updateBatchById(updateUserParkingPermissionList);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void vehicleDelete(VechicleDeleteVO vechicleDeleteVO) {
        if (Objects.isNull(vechicleDeleteVO.getUserId()) || CollectionUtils.isEmpty(vechicleDeleteVO.getVehicleNos())) {
            return;
        }
        List<String> deleteVechicleList = vechicleDeleteVO.getVehicleNos().stream().map(VechicleDeleteVO.VechicleNo::getVehicleNo)
                .collect(Collectors.toList());
        List<UserParkingPermission> userParkingPermissions = this.lambdaQuery()
                .eq(UserParkingPermission::getDeleted, 0)
                .eq(UserParkingPermission::getUserId, vechicleDeleteVO.getUserId())
                .in(UserParkingPermission::getVehicleNo, deleteVechicleList)
                .list();
        if (CollectionUtils.isEmpty(userParkingPermissions)) {
            log.error("用户没权限绑定记录");
            return;
        }
        userParkingPermissions.forEach(t -> {
            t.setVehicleNo(null);
        });
        this.updateBatchById(userParkingPermissions);
    }
}




