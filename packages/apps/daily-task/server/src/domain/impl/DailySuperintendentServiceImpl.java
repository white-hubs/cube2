package com.guhecloud.cube.biz.daily.task.domain.impl;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailySuperintendentDTO;
import com.guhecloud.cube.biz.daily.task.domain.DailySuperintendentService;
import com.guhecloud.cube.biz.daily.task.domain.adapter.client.DailyAttendanceClient;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.ArrangeWorkParam;
import com.guhecloud.cube.biz.daily.task.domain.po.base.DailyBaseSuperintendent;
import com.guhecloud.cube.biz.daily.task.enums.DailySuperintendentTypeEnum;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.cube.biz.ouaa.client.AppointmentClient;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.RoleClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.AppointmentDetailDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.RoleDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.AppointmentQueryParam;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserMapQuery;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.ConditionTypeEnum;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 负责人员处理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailySuperintendentServiceImpl implements DailySuperintendentService {

    private final UserClient userClient;
    private final OrganizationClient organizationClient;
    private final AppointmentClient appointmentClient;
    private final RoleClient roleClient;
    private final AreaClient areaClient;
    private final DailyAttendanceClient dailyAttendanceClient;

    /**
     * 处负责人集合获取其名称(人员,角色,部门,岗位名)
     *
     * @param poList 人员关系集合
     *
     * @return 处理后负责人集合
     */
    @Override
    public <T extends DailyBaseSuperintendent> List<DailySuperintendentDTO> handlerSuperintendent(List<T> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return Collections.emptyList();
        }
        /* 批量获取负责对象信息 */
        Map<String, UserDTO> userMap = new HashMap<>();
        Map<Long, OrganizationDTO> detpMap = new HashMap<>();
        Map<Long, AppointmentDetailDTO> appointmentMap = new HashMap<>();
        Map<Long, RoleDTO> roleMap = new HashMap<>();
        Map<Long, String> areaMap = new HashMap<>();
        var typeMap = poList.stream().collect(Collectors.groupingBy(T::getSuperintendentType));
        for (Map.Entry<String, List<T>> stringListEntry : typeMap.entrySet()) {
            var type = stringListEntry.getKey();
            var targetIdSet = stringListEntry.getValue().stream().map(T::getTargetId).collect(Collectors.toSet());
            if (DailySuperintendentTypeEnum.USER.name().equals(type)) {
                userMap = userClient.getTenantUserMap(UserMapQuery.builder().userIds(targetIdSet).build());
            } else if (DailySuperintendentTypeEnum.DEPT.name().equals(type)) {
                detpMap = organizationClient.postOrganizationsMapByIds(OrganizationQueryParam.builder().ids(targetIdSet).build());
            } else if (DailySuperintendentTypeEnum.POST.name().equals(type)) {
                var param = new AppointmentQueryParam();
                param.setEnable(Boolean.TRUE);
                param.setIds(targetIdSet);
                appointmentMap = appointmentClient.postAppointmentMap(param);
            } else if (DailySuperintendentTypeEnum.ROLE.name().equals(type)) {
                roleMap = roleClient.postRoleMap();
            } else if (ConditionTypeEnum.GRID_AREA.name().equals(type)) {
                AreaQueryParam queryParam = new AreaQueryParam();
                queryParam.setIds(targetIdSet);
                var araList = areaClient.listInnerAreaPost(queryParam);
                areaMap = Optional.ofNullable(araList).orElse(CollectionUtils.emptyList()).stream()
                        .collect(Collectors.toMap(AreaDTO::getId, AreaDTO::getName));
            }
        }
        Map<String, UserDTO> finalUserMap = userMap;
        Map<Long, OrganizationDTO> finalDetpMap = detpMap;
        Map<Long, AppointmentDetailDTO> finalAppointmentMap = appointmentMap;
        Map<Long, RoleDTO> finalRoleMap = roleMap;
        Map<Long, String> finalAreaMap = areaMap;
        return poList.stream().map(po -> {
            String targetName = this.getTargetNameByType(po.getTargetId(), po.getSuperintendentType(), finalUserMap, finalDetpMap, finalAppointmentMap, finalRoleMap, finalAreaMap);
            return DailySuperintendentDTO.builder().id(po.getId()).masterId(po.getMasterId())
                    .participateType(po.getSuperintendentType())
                    .targetId(po.getTargetId()).targetName(targetName).build();
        }).collect(Collectors.toList());
    }

    /**
     * 根据账号获取用户名称
     *
     * @param accountList 账号集合
     *
     * @return 用户名称集合
     */
    @Override
    public Map<String, UserDTO> getUserNameByAccounts(Set<String> accountList) {
        if (CollectionUtils.isEmpty(accountList)) {
            return Collections.emptyMap();
        }
        var userMap = userClient.getTenantUserMap(UserMapQuery.builder().userNames(accountList).build());
        return userMap == null ? new HashMap<>() : userMap;
    }

    /**
     * 过滤出在岗人员列表
     *
     * @param userDTOS 待过滤的用户列表
     *
     * @return 在岗人员列表
     */
    @Override
    public List<UserDTO> filterAttendanceUser(List<UserDTO> userDTOS) {
        /* 过滤不在岗位人员 */
        var userIdSet = userDTOS.stream().map(UserDTO::getId).collect(Collectors.toSet());
        var attendanceParam = new ArrangeWorkParam();
        attendanceParam.setUserIds(userIdSet);
        attendanceParam.setStartDate(LocalDateTime.now());
        List<Long> attUserIdList;
        try {
            attUserIdList = dailyAttendanceClient.postOnDutyPersonnel(attendanceParam);
        } catch (Exception e) {
            log.warn("获取在岗人员失败:{}", e.getMessage(), e);
            return userDTOS;
        }
        if (CollectionUtils.isEmpty(attUserIdList)) {
            return userDTOS;
        }
        // 根据考勤接口返回的在岗人员ID列表，过滤出实际在岗的用户
        var attUsers = userDTOS.stream()
                .filter(user -> attUserIdList.contains(user.getId()))
                .toList();
        // 如果没有在岗用户，则抛出异常
        if (CollectionUtils.isEmpty(attUsers)) {
            return userDTOS;
        }
        return attUsers;
    }

    /**
     * 根据id和类型获取对象名称
     *
     * @param id             对象id
     * @param type           类型
     * @param userMap        用户map
     * @param deptMap        部门map
     * @param appointmentMap 岗位map
     * @param roleMap        角色map
     * @param finalAreaMap
     *
     * @return 名称
     */
    private String getTargetNameByType(Long id, String type, Map<String, UserDTO> userMap, Map<Long, OrganizationDTO> deptMap,
                                       Map<Long, AppointmentDetailDTO> appointmentMap, Map<Long, RoleDTO> roleMap,
                                       Map<Long, String> finalAreaMap
                                      ) {
        if (DailySuperintendentTypeEnum.USER.name().equals(type)) {
            return userMap.containsKey(String.valueOf(id)) ? userMap.get(String.valueOf(id)).getName() : "";
        } else if (DailySuperintendentTypeEnum.DEPT.name().equals(type)) {
            return deptMap.containsKey(id) ? deptMap.get(id).getName() : "";
        } else if (DailySuperintendentTypeEnum.POST.name().equals(type)) {
            return appointmentMap.containsKey(id) ? appointmentMap.get(id).getName() : "";
        } else if (DailySuperintendentTypeEnum.ROLE.name().equals(type)) {
            return roleMap.containsKey(id) ? roleMap.get(id).getName() : "";
        } else if (ConditionTypeEnum.GRID_AREA.name().equals(type)) {
            return finalAreaMap.getOrDefault(id, "");
        } else {
            return "";
        }
    }
}
