package com.guhecloud.cube.biz.mdata.server.grid.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.alarm.client.AlarmBimStatisticsClient;
import com.guhecloud.cube.base.alarm.client.dto.AlarmRecordAreaCountDTO;
import com.guhecloud.cube.base.alarm.client.param.AlarmRecordAreaCountParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.daily.task.client.DailyTaskClient;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.rocketmq.message.GridAreaMemberSetMessage;
import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaService;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaListVO;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridAreaExtend;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridHandoverRecord;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridRelationMember;
import com.guhecloud.cube.biz.mdata.server.grid.enumeration.GridHandoverStatusEnum;
import com.guhecloud.cube.biz.mdata.server.grid.enumeration.GridRelationTypeEnum;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaConfigParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaConfigQueryParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaMemberBatchSetParam;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridAreaConfigService;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridAreaExtendService;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridHandoverRecordService;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridRelationMemberService;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaConfigDetailVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaConfigScreenVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaConfigVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaObjectCountVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaStatisticsVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaTreeVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridManagerVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridMemberVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridStatisticsVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridTotalStatisticsVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.MyGridVO;
import com.guhecloud.cube.biz.ouaa.client.AppointmentClient;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.PositionClient;
import com.guhecloud.cube.biz.ouaa.client.RoleClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.biz.work.center.client.WorkTaskClient;
import com.guhecloud.cube.biz.work.center.client.param.WorkHomePageBatchParam;
import com.guhecloud.cube.biz.work.center.client.vo.WorkHomePageVO;
import com.guhecloud.cube.property.management.client.property.client.property.PropertyDetailedClient;
import com.guhecloud.cube.srv.gen.maintain.client.FaultClient;
import com.guhecloud.cube.srv.gen.maintain.client.PatrolLocationClient;
import com.guhecloud.cube.srv.gen.maintain.client.ScenePointClient;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTreeEntity;
import com.guhecloud.platform.database.util.EntityUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 网格区域配置 Service 实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GridAreaConfigServiceImpl implements GridAreaConfigService {

    private final GridAreaExtendService gridAreaExtendService;
    private final GridRelationMemberService gridRelationMemberService;
    private final GridHandoverRecordService gridHandoverRecordService;
    private final OrganizationClient organizationClient;
    private final UserClient userClient;
    private final AppointmentClient appointmentClient;
    private final RoleClient roleClient;
    private final PositionClient positionClient;
    private final AreaService areaService;
    private final DeviceCenterStdClient deviceClient;
    private final PropertyDetailedClient propertyDetailedClient;
    private final StallClient stallClient;
    private final ScenePointClient scenePointClient;
    private final PatrolLocationClient patrolLocationClient;
    private final FileClient fileClient;
    private final WorkTaskClient workTaskClient;
    private final AlarmBimStatisticsClient alarmBimStatisticsClient;
    private final FaultClient faultClient;
    private final GridDictionaryUtilServiceImpl gridDictionaryUtilService;
    private final DailyTaskClient dailyTaskClient;
    private final static String WORKING = "WORKING";
    private final static String TASK_CODE = "TASK";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setGridAreaConfig(GridAreaConfigParam param) {
        // 1. 保存或更新 grid_area_extend
        GridAreaExtend extend = gridAreaExtendService.getOne(
                Wrappers.<GridAreaExtend>lambdaQuery()
                        .eq(GridAreaExtend::getGridAreaId, param.getGridAreaId())
                                                            );

        if (extend == null) {
            extend = new GridAreaExtend();
            extend.setId(IdWorker.getId());
            extend.setGridAreaId(param.getGridAreaId());
        }

        extend.setGridDepartmentId(param.getGridDepartmentId());
        // 将管理长id列表转换为逗号分隔的字符串
        if (CollectionUtils.isNotEmpty(param.getGridManagerIds())) {
            extend.setGridManagerIds(param.getGridManagerIds().stream()
                                             .map(String::valueOf)
                                             .collect(Collectors.joining(",")));
        } else {
            extend.setGridManagerIds(null);
        }
        extend.setEngineeringManager(param.getEngineeringManager());
        extend.setSafetyManager(param.getSafetyManager());
        extend.setCleaningManager(param.getCleaningManager());
        extend.setMeteringManager(param.getMeteringManager());
        extend.setMeteringStaff(param.getMeteringStaff());

        gridAreaExtendService.saveOrUpdate(extend);

        // 2. 删除该区域的所有关联成员记录
        gridRelationMemberService.remove(
                Wrappers.<GridRelationMember>lambdaQuery()
                        .eq(GridRelationMember::getGridAreaId, param.getGridAreaId())
                                        );

        // 3. 保存新的关联成员记录
        if (CollectionUtils.isNotEmpty(param.getGridMembers())) {
            List<GridRelationMember> members = new ArrayList<>();
            for (GridAreaConfigParam.GridMemberConfig memberConfig : param.getGridMembers()) {
                if (CollectionUtils.isNotEmpty(memberConfig.getRelationIds())) {
                    for (Long relationId : memberConfig.getRelationIds()) {
                        GridRelationMember member = new GridRelationMember();
                        member.setId(IdWorker.getId());
                        member.setGridAreaId(param.getGridAreaId());
                        member.setRelationType(memberConfig.getRelationType());
                        member.setRelationId(relationId);
                        members.add(member);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(members)) {
                gridRelationMemberService.saveBatch(members);
            }
        }
    }

    @Override
    public Pagination<GridAreaConfigVO> getGridAreaConfigs(GridAreaConfigQueryParam param) {
        var gridAreaIds = param.getGridAreaIds();
        //查询区域信息
        var areaQueryParam = new AreaQueryParam();
        areaQueryParam.setIds(gridAreaIds);
        areaQueryParam.setKeyword(param.getKeyword());
        var areas = areaService.getList(areaQueryParam);
        if (CollectionUtils.isEmpty(areas)) {
            return Pagination.empty();
        }
        var areaIds = ListUtils.convertItem(areas, Area::getId);
        var areaMap = MapUtils.listToMap(areas, Area::getId, area -> area);
        //查询区域的父区域信息
        var parentIds = areas.stream()
                .map(BasicEnhancedTreeEntity::getSequence)
                .filter(StringUtils::isNotEmpty)
                .map(sec -> sec.split("-"))
                .filter(arr -> arr.length > 0)
                .flatMap(Arrays::stream)
                .filter(StringUtils::isNotEmpty)
                .map(Long::valueOf)
                .collect(Collectors.toSet());
        var parentAreas = areaService.getByIds(parentIds.stream().toList());
        var parentAreaMap = MapUtils.listToMap(parentAreas, Area::getId, area -> area);
        Map<Long, GridAreaConfigVO> resultMap = new HashMap<>();
        var queryWrapper = Wrappers.<GridAreaExtend>lambdaQuery().in(GridAreaExtend::getGridAreaId, areaIds);
        // 如果没有传区域id，则查询所有已绑定网格的区域
        // 1. 批量查询 grid_area_extend
        var pageResult = gridAreaExtendService.page(PagingUtils.convert(param), queryWrapper);
        var extendsList = pageResult.getRecords();
        if (CollectionUtils.isEmpty(extendsList)) {
            return Pagination.empty();
        }
        gridAreaIds = ListUtils.convertItem(extendsList, GridAreaExtend::getGridAreaId);
        //查询当前网格的父区域

        //查询区域子区域信息
        var areaTree = areaService.getAreaTreesWithChildren(gridAreaIds);

        // 按 gridAreaId 分组
        Map<Long, GridAreaExtend> extendMap = extendsList.stream()
                .collect(Collectors.toMap(GridAreaExtend::getGridAreaId, extend -> extend, (v1, v2) -> v1));

        // 收集所有需要查询的部门id和管理长id
        Set<Long> departmentIds = new HashSet<>();
        Set<Long> allManagerIds = new HashSet<>();
        for (Long gridAreaId : gridAreaIds) {
            GridAreaConfigVO vo = new GridAreaConfigVO();
            vo.setGridAreaId(gridAreaId);
            var area = areaMap.get(gridAreaId);
            if (area != null && area.getSequence() != null) {
                var sb = new StringBuilder();
                for (String parIdStr : area.getSequence().split("-")) {
                    if (StringUtils.isNotBlank(parIdStr)) {
                        var parId = Long.valueOf(parIdStr);
                        var parArea = parentAreaMap.get(parId);
                        if (parArea != null) {
                            sb.append(parArea.getName());
                            sb.append("/");
                        }
                    }
                }
                vo.setGridName(sb.isEmpty() ? sb.toString() : sb.substring(0, sb.length() - 1));
            } else {
                vo.setGridName("");
            }
            vo.setChildAreaName(getAreaChildName(areaTree, gridAreaId));
            resultMap.put(gridAreaId, vo);

            GridAreaExtend extend = extendMap.get(gridAreaId);
            collectExtendManagerIds(extend, vo, departmentIds, allManagerIds);
        }

        // 2. 批量查询 grid_relation_member
        List<GridRelationMember> allMembers = gridRelationMemberService.list(
                Wrappers.<GridRelationMember>lambdaQuery()
                        .in(GridRelationMember::getGridAreaId, gridAreaIds)
                                                                            );

        // 收集每个网格区域下的在岗成员用户
        Map<Long, Set<UserDTO>> gridMemberUsers = collectGridMemberUsers(false, allMembers);

        // 3. 批量获取部门名称
        Map<Long, String> departmentNameMap = batchGetDepartmentNameMap(departmentIds);

        // 4. 批量获取管理长用户信息及头像
        Map<Long, UserDTO> managerMap = new HashMap<>();
        var fileVOMap = this.fileClient.getByBusinessKeys(
                BusinessKeysParam.builder().businessKeys(new ArrayList<>(allManagerIds)).build());
        if (CollectionUtils.isNotEmpty(allManagerIds)) {
            try {
                UserQuery userQueryParam = new UserQuery();
                userQueryParam.setUserIds(allManagerIds);
                List<UserDTO> users = userClient.postList(userQueryParam);
                managerMap.putAll(MapUtils.listToMap(users, UserDTO::getId, userDTO -> userDTO));
            } catch (Exception e) {
                log.warn("批量获取管理长名称失败", e);
            }
        }

        // 5. 批量获取当天有交接任务的管理长，避免在组装循环中对每个网格单独发起 client 调用
        Set<Long> hasTaskUserSet = batchGetHasTaskUserSet(allManagerIds);

        // 6. 组装数据
        for (Long gridAreaId : gridAreaIds) {
            GridAreaConfigVO vo = resultMap.get(gridAreaId);
            GridAreaExtend extend = extendMap.get(gridAreaId);
            fillGridAreaConfigVO(vo, extend, departmentNameMap, managerMap, fileVOMap,
                                 gridMemberUsers.getOrDefault(gridAreaId, Set.of()), hasTaskUserSet);
        }
        Set<Long> pendingGridAreaIds = fillGridHandoverStatus(gridAreaIds, resultMap);
        List<GridAreaConfigVO> sortedRecords = sortByPendingFirst(gridAreaIds, resultMap, pendingGridAreaIds);
        return PagingUtils.convert(pageResult, sortedRecords);
    }

    private Set<Long> fillGridHandoverStatus(List<Long> gridAreaIds, Map<Long, GridAreaConfigVO> resultMap) {
        Long currentUserId = SecurityContextHolder.getPrincipal().getId();
        if (currentUserId == null || CollectionUtils.isEmpty(gridAreaIds) || MapUtils.isEmpty(resultMap)) {
            return Set.of();
        }
        var pendingRecords = gridHandoverRecordService.lambdaQuery()
                .eq(GridHandoverRecord::getTakeOverUser, currentUserId)
                .eq(GridHandoverRecord::getStatus, GridHandoverStatusEnum.WAIT_HANDOVER)
                .orderByDesc(GridHandoverRecord::getShiftOverTime)
                .list();
        if (CollectionUtils.isEmpty(pendingRecords)) {
            return Set.of();
        }
        Map<Long, GridHandoverRecord> pendingMap = new HashMap<>();
        for (GridHandoverRecord record : pendingRecords) {
            if (record == null || StringUtils.isBlank(record.getGridAreaIds())) {
                continue;
            }
            for (Long gridAreaId : parseGridHandoverAreaIds(record.getGridAreaIds())) {
                if (gridAreaIds.contains(gridAreaId)) {
                    pendingMap.putIfAbsent(gridAreaId, record);
                }
            }
        }
        pendingMap.forEach((gridAreaId, record) -> {
            GridAreaConfigVO vo = resultMap.get(gridAreaId);
            if (vo == null) {
                return;
            }
            vo.setHandoverStatus(GridHandoverStatusEnum.WAIT_HANDOVER);
            vo.setHandoverStatusDesc(GridHandoverStatusEnum.WAIT_HANDOVER.getDesc());
            vo.setHandoverRecordId(record.getId());
        });
        return pendingMap.keySet();
    }

    private List<Long> parseGridHandoverAreaIds(String gridAreaIds) {
        if (StringUtils.isBlank(gridAreaIds)) {
            return List.of();
        }
        List<Long> result = new ArrayList<>();
        for (String id : gridAreaIds.split(",")) {
            if (StringUtils.isBlank(id)) {
                continue;
            }
            try {
                result.add(Long.parseLong(id.trim()));
            } catch (NumberFormatException ignored) {
            }
        }
        return result;
    }

    private List<GridAreaConfigVO> sortByPendingFirst(List<Long> gridAreaIds,
                                                      Map<Long, GridAreaConfigVO> resultMap,
                                                      Set<Long> pendingGridAreaIds
                                                     ) {
        if (CollectionUtils.isEmpty(gridAreaIds) || MapUtils.isEmpty(resultMap)) {
            return List.of();
        }
        List<GridAreaConfigVO> pendingList = new ArrayList<>();
        List<GridAreaConfigVO> normalList = new ArrayList<>();
        for (Long gridAreaId : gridAreaIds) {
            GridAreaConfigVO vo = resultMap.get(gridAreaId);
            if (vo == null) {
                continue;
            }
            if (CollectionUtils.isNotEmpty(pendingGridAreaIds) && pendingGridAreaIds.contains(gridAreaId)) {
                pendingList.add(vo);
            } else {
                normalList.add(vo);
            }
        }
        pendingList.addAll(normalList);
        return pendingList;
    }

    /**
     * 从 GridAreaExtend 中收集部门id和管理长id，并设置占位VO到 GridAreaConfigVO
     */
    private void collectExtendManagerIds(GridAreaExtend extend, GridAreaConfigVO vo,
                                         Set<Long> departmentIds, Set<Long> allManagerIds
                                        ) {
        if (extend == null) {
            return;
        }
        vo.setGridDepartmentId(extend.getGridDepartmentId());
        if (extend.getGridDepartmentId() != null) {
            departmentIds.add(extend.getGridDepartmentId());
        }
        // 解析管理长id列表，创建占位VO
        if (StringUtils.isNotBlank(extend.getGridManagerIds())) {
            List<Long> managerIds = new ArrayList<>();
            for (String id : extend.getGridManagerIds().split(",")) {
                if (StringUtils.isNotBlank(id)) {
                    try {
                        Long managerId = Long.parseLong(id.trim());
                        managerIds.add(managerId);
                        allManagerIds.add(managerId);
                    } catch (NumberFormatException e) {
                        log.warn("解析管理长id失败: {}", id, e);
                    }
                }
            }
            vo.setGridManagers(managerIds.stream().map(managerId -> {
                var mv = new GridManagerVO();
                mv.setGridManagerId(managerId);
                return mv;
            }).toList());
        }
        // 收集5个负责人id，创建占位VO
        if (extend.getEngineeringManager() != null) {
            allManagerIds.add(extend.getEngineeringManager());
            var mv = new GridManagerVO();
            mv.setGridManagerId(extend.getEngineeringManager());
            vo.setEngineeringManager(mv);
        }
        if (extend.getSafetyManager() != null) {
            allManagerIds.add(extend.getSafetyManager());
            var mv = new GridManagerVO();
            mv.setGridManagerId(extend.getSafetyManager());
            vo.setSafetyManager(mv);
        }
        if (extend.getCleaningManager() != null) {
            allManagerIds.add(extend.getCleaningManager());
            var mv = new GridManagerVO();
            mv.setGridManagerId(extend.getCleaningManager());
            vo.setCleaningManager(mv);
        }
        if (extend.getMeteringManager() != null) {
            allManagerIds.add(extend.getMeteringManager());
            var mv = new GridManagerVO();
            mv.setGridManagerId(extend.getMeteringManager());
            vo.setMeteringManager(mv);
        }
        if (extend.getMeteringStaff() != null) {
            allManagerIds.add(extend.getMeteringStaff());
            var mv = new GridManagerVO();
            mv.setGridManagerId(extend.getMeteringStaff());
            vo.setMeteringStaff(mv);
        }
    }

    /**
     * 批量获取部门名称Map
     */
    private Map<Long, String> batchGetDepartmentNameMap(Set<Long> departmentIds) {
        Map<Long, String> departmentNameMap = new HashMap<>();
        if (CollectionUtils.isEmpty(departmentIds)) {
            return departmentNameMap;
        }
        try {
            OrganizationQueryParam orgParam = new OrganizationQueryParam();
            orgParam.setIds(departmentIds);
            List<OrganizationDTO> organizations = organizationClient.getOrganizations(orgParam);
            if (CollectionUtils.isNotEmpty(organizations)) {
                for (OrganizationDTO org : organizations) {
                    if (org != null && org.getId() != null && StringUtils.isNotBlank(org.getName())) {
                        departmentNameMap.put(org.getId(), org.getName());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("批量获取部门名称失败", e);
        }
        return departmentNameMap;
    }

    /**
     * 根据关联成员记录，收集每个网格区域下的用户（去重），可选仅保留在岗人员。
     * 先按 relationType 聚合所有 relationId，每种类型至多调用一次 client，再按
     * relationId 反向索引回填到各网格，避免在网格循环内重复发起远程调用。
     */
    private Map<Long, Set<UserDTO>> collectGridMemberUsers(boolean working,
                                                           List<GridRelationMember> allMembers
                                                          ) {
        Map<Long, Set<UserDTO>> gridMemberUsers = new HashMap<>();
        if (CollectionUtils.isEmpty(allMembers)) {
            return gridMemberUsers;
        }

        // 1. 按类型统一聚合 relationId（跨所有网格去重）
        Map<GridRelationTypeEnum, Set<Long>> idsByType = new EnumMap<>(GridRelationTypeEnum.class);
        for (GridRelationMember m : allMembers) {
            if (m.getGridAreaId() == null || m.getRelationType() == null || m.getRelationId() == null) {
                continue;
            }
            idsByType.computeIfAbsent(m.getRelationType(), k -> new HashSet<>()).add(m.getRelationId());
        }

        // 2. 构建 relationId -> users 反向索引
        //    - USER/ROLE/POSITION/POST：每种类型只调用一次 client，由 UserDTO 的集合字段回填索引
        //    - DEPARTMENT：UserDTO.organization 为单值映射（见 UserMapper），多部门用户只保留一个部门，
        //      因此按单个 orgId 分别查询以保证正确归属（仍去重为每个 orgId 最多一次调用）
        Map<GridRelationTypeEnum, Map<Long, List<UserDTO>>> relationIdToUsersByType = new EnumMap<>(GridRelationTypeEnum.class);
        idsByType.forEach((relationType, relationIds) -> {
            Map<Long, List<UserDTO>> index;
            if (relationType == GridRelationTypeEnum.DEPARTMENT) {
                index = new HashMap<>();
                for (Long orgId : relationIds) {
                    List<UserDTO> users = batchGetRelationNameMap(relationType, List.of(orgId));
                    if (CollectionUtils.isNotEmpty(users)) {
                        index.put(orgId, users);
                    }
                }
            } else {
                List<UserDTO> users = batchGetRelationNameMap(relationType, new ArrayList<>(relationIds));
                index = buildRelationIdIndex(relationType, relationIds, users);
            }
            relationIdToUsersByType.put(relationType, index);
        });

        // 3. 按网格回填
        Map<Long, List<GridRelationMember>> membersByGrid = allMembers.stream()
                .filter(m -> m.getGridAreaId() != null && m.getRelationType() != null && m.getRelationId() != null)
                .collect(Collectors.groupingBy(GridRelationMember::getGridAreaId));
        membersByGrid.forEach((gridAreaId, members) -> {
            Map<Long, UserDTO> dedup = new HashMap<>();
            for (GridRelationMember m : members) {
                Map<Long, List<UserDTO>> idx = relationIdToUsersByType.get(m.getRelationType());
                if (idx == null) {
                    continue;
                }
                List<UserDTO> users = idx.get(m.getRelationId());
                if (users == null) {
                    continue;
                }
                for (UserDTO user : users) {
                    if (user == null || user.getId() == null) {
                        continue;
                    }
                    if (working && !WORKING.equals(user.getWorkStatus())) {
                        continue;
                    }
                    dedup.putIfAbsent(user.getId(), user);
                }
            }
            if (!dedup.isEmpty()) {
                gridMemberUsers.put(gridAreaId, new HashSet<>(dedup.values()));
            }
        });
        return gridMemberUsers;
    }

    /**
     * 构建 relationId -> 用户列表 的反向索引，仅用于非 DEPARTMENT 类型。
     * 不同类型从 UserDTO 不同的集合字段读取：USER 直接取 id；ROLE 取 roles；
     * POSITION 取 appointments（UserQuery.appointmentIds 对应 POSITION，历史字段名互换）；
     * POST 取 positions。
     */
    private Map<Long, List<UserDTO>> buildRelationIdIndex(GridRelationTypeEnum relationType,
                                                          Set<Long> relationIds, List<UserDTO> users
                                                         ) {
        Map<Long, List<UserDTO>> index = new HashMap<>();
        if (CollectionUtils.isEmpty(users)) {
            return index;
        }
        for (UserDTO user : users) {
            if (user == null || user.getId() == null) {
                continue;
            }
            switch (relationType) {
                case USER -> indexPut(index, user.getId(), user, relationIds);
                case ROLE -> {
                    if (user.getRoles() != null) {
                        for (UserDTO.Role r : user.getRoles()) {
                            if (r != null) {
                                indexPut(index, r.getId(), user, relationIds);
                            }
                        }
                    }
                }
                case POSITION -> {
                    if (user.getAppointments() != null) {
                        for (UserDTO.Appointment a : user.getAppointments()) {
                            if (a != null) {
                                indexPut(index, a.getId(), user, relationIds);
                            }
                        }
                    }
                }
                case POST -> {
                    if (user.getPositions() != null) {
                        for (UserDTO.Position p : user.getPositions()) {
                            if (p != null) {
                                indexPut(index, p.getId(), user, relationIds);
                            }
                        }
                    }
                }
                default -> {
                }
            }
        }
        return index;
    }

    private void indexPut(Map<Long, List<UserDTO>> index, Long key, UserDTO user, Set<Long> allowedKeys) {
        if (key == null || !allowedKeys.contains(key)) {
            return;
        }
        index.computeIfAbsent(key, k -> new ArrayList<>()).add(user);
    }

    /**
     * 批量获取当天有交接任务的管理长 id 集合（用入参中的 userIds 过滤），返回空集表示当前未命中任何交接。
     * 抽取为单次调用，避免在组装 VO 的循环中按网格反复发起 client 调用。
     */
    private Set<Long> batchGetHasTaskUserSet(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Set.of();
        }
        try {
            List<Long> hasTaskUser = workTaskClient.getHandTaskUser(userIds);
            return CollectionUtils.isEmpty(hasTaskUser) ? Set.of() : new HashSet<>(hasTaskUser);
        } catch (Exception e) {
            log.warn("批量获取当天有交接任务的管理长失败", e);
            return Set.of();
        }
    }

    /**
     * 将管理长/负责人/成员信息填充到 GridAreaConfigVO
     *
     * @param hasTaskUserSet 当天有交接任务的管理长 id 集合（已在外层批量预查）
     */
    private void fillGridAreaConfigVO(GridAreaConfigVO vo, GridAreaExtend extend,
                                      Map<Long, String> departmentNameMap,
                                      Map<Long, UserDTO> managerMap,
                                      Map<Long, List<FileVO>> fileVOMap,
                                      Set<UserDTO> memberUsers,
                                      Set<Long> hasTaskUserSet
                                     ) {
        if (extend != null && extend.getGridDepartmentId() != null) {
            vo.setGridDepartmentName(departmentNameMap.get(extend.getGridDepartmentId()));
        }
        // 设置管理长列表信息
        var managers = vo.getGridManagers();
        if (CollectionUtils.isNotEmpty(managers)) {
            managers.forEach(managerVo -> fillManagerVO(managerVo, managerMap, fileVOMap));
            List<GridManagerVO> hasTaskUserList = CollectionUtils.isEmpty(hasTaskUserSet) ? List.of()
                                                                                          : managers.stream()
                                                          .filter(t -> hasTaskUserSet.contains(t.getGridManagerId()))
                                                          .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(hasTaskUserList)) {
                managers = hasTaskUserList;
            } else {
                List<GridManagerVO> workingUserList = managers.stream().filter(t -> WORKING.equals(t.getWorkStatus()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(workingUserList)) {
                    managers = workingUserList;
                }
            }
            vo.setGridManagers(managers);
        } else {
            vo.setGridManagers(List.of());
        }
        // 设置5个负责人信息
        fillManagerVO(vo.getEngineeringManager(), managerMap, fileVOMap);
        fillManagerVO(vo.getSafetyManager(), managerMap, fileVOMap);
        fillManagerVO(vo.getCleaningManager(), managerMap, fileVOMap);
        fillManagerVO(vo.getMeteringManager(), managerMap, fileVOMap);
        fillManagerVO(vo.getMeteringStaff(), managerMap, fileVOMap);
        // 设置网格成员（已按在岗过滤）
        vo.setGridMembers(ListUtils.convertItem(new ArrayList<>(memberUsers),
                                                user -> new GridMemberVO(user.getId(), user.getName(), user.getMobile())));
    }

    /**
     * 将管理长/负责人/成员信息填充到 GridAreaConfigScreenVO（网格管理屏专用）
     * 网格成员使用 GridManagerVO，包含头像、在岗状态、手机号
     */
    private void fillGridAreaConfigScreenVO(GridAreaConfigScreenVO vo, GridAreaExtend extend,
                                            Map<Long, String> departmentNameMap,
                                            Map<Long, UserDTO> managerMap,
                                            Map<Long, List<FileVO>> fileVOMap,
                                            Set<UserDTO> memberUsers
                                           ) {
        if (extend != null && extend.getGridDepartmentId() != null) {
            vo.setGridDepartmentName(departmentNameMap.get(extend.getGridDepartmentId()));
        }
        // 设置管理长列表信息
        var managers = vo.getGridManagers();
        if (CollectionUtils.isNotEmpty(managers)) {
            managers.forEach(managerVo -> fillManagerVO(managerVo, managerMap, fileVOMap));
            vo.setGridManagers(managers);
        } else {
            vo.setGridManagers(List.of());
        }
        // 设置5个负责人信息
        fillManagerVO(vo.getEngineeringManager(), managerMap, fileVOMap);
        fillManagerVO(vo.getSafetyManager(), managerMap, fileVOMap);
        fillManagerVO(vo.getCleaningManager(), managerMap, fileVOMap);
        fillManagerVO(vo.getMeteringManager(), managerMap, fileVOMap);
        fillManagerVO(vo.getMeteringStaff(), managerMap, fileVOMap);
        // 设置网格成员（GridManagerVO，含头像、在岗状态）
        vo.setGridMemberInfo(ListUtils.convertItem(new ArrayList<>(memberUsers), user -> {
            var memberVO = new GridManagerVO();
            memberVO.setGridManagerId(user.getId());
            memberVO.setGridManagerName(user.getName());
            memberVO.setMobile(user.getMobile());
            memberVO.setWorkStatus(user.getWorkStatus());
            Optional.ofNullable(fileVOMap.get(user.getId()))
                    .ifPresent(files -> memberVO.setAvatar(files.getFirst()));
            return memberVO;
        }));
    }

    /**
     * 根据用户信息填充 GridManagerVO（name、workStatus、mobile、avatar）
     */
    private void fillManagerVO(GridManagerVO managerVo, Map<Long, UserDTO> managerMap,
                               Map<Long, List<FileVO>> fileVOMap
                              ) {
        if (managerVo == null) {
            return;
        }
        var user = managerMap.get(managerVo.getGridManagerId());
        if (user != null) {
            managerVo.setGridManagerName(user.getName());
            managerVo.setWorkStatus(user.getWorkStatus());
            managerVo.setMobile(user.getMobile());
            Optional.ofNullable(fileVOMap.get(user.getId()))
                    .ifPresent(files -> managerVo.setAvatar(files.getFirst()));
        }
    }

    private String getAreaChildName(List<AreaListVO> areaTree, Long gridAreaId) {
        if (ListUtils.isEmpty(areaTree)) {
            return "";
        }
        //从树形区域中找到当前区域
        var curArea = areaTree.stream().filter(area -> area.getId().equals(gridAreaId)).findFirst().orElse(null);
        if (curArea == null) {
            return "";
        }
        var sb = new StringBuilder();
        var curChildren = curArea.getChildren();
        var nextChildren = new ArrayList<AreaListVO>();
        while (ListUtils.isNotEmpty(curChildren)) {
            sb.append("/");
            for (AreaListVO curChild : curChildren) {
                sb.append(curChild.getName());
                if (!curChildren.getLast().equals(curArea)) {
                    sb.append("，");
                }
                if (ListUtils.isNotEmpty(curChild.getChildren())) {
                    nextChildren.addAll(curChild.getChildren());
                }
            }
            curChildren = nextChildren;
            nextChildren.clear();
        }

        return sb.isEmpty() ? sb.toString() : sb.substring(1, sb.length() - 1);
    }

    /**
     * 批量获取关联名称Map
     *
     * @param relationType 关联类型
     * @param relationIds  关联id列表
     */
    private List<UserDTO> batchGetRelationNameMap(GridRelationTypeEnum relationType, List<Long> relationIds) {
        if (CollectionUtils.isEmpty(relationIds)) {
            return List.of();
        }
        try {
            switch (relationType) {
                case DEPARTMENT:
                    // 批量获取部门信息
                    try {
                        return userClient.postList(UserQuery.builder().organizationIds(new HashSet<>(relationIds)).build());

                    } catch (Exception e) {
                        log.warn("批量获取部门下用户名称失败", e);
                    }
                    break;
                case USER:
                    // 批量获取用户信息
                    try {
                        UserQuery userQueryParam = new UserQuery();
                        userQueryParam.setUserIds(new HashSet<>(relationIds));
                        return userClient.postList(userQueryParam);

                    } catch (Exception e) {
                        log.warn("批量获取用户名称失败", e);
                    }
                    break;
                case ROLE:
                    try {
                        // 批量获取角色信息
                        return userClient.postList(UserQuery.builder().roleIds(new HashSet<>(relationIds)).build());

                    } catch (Exception e) {
                        log.warn("批量获取职位下用户名称失败", e);
                    }
                    break;
                case POSITION:
                    // 批量获取职位信息
                    try {
                        return userClient.postList(UserQuery.builder().appointmentIds(new HashSet<>(relationIds)).build());

                    } catch (Exception e) {
                        log.warn("批量获取职位下用户名称失败", e);
                    }
                    break;
                case POST:
                    // 批量获取岗位信息
                    try {
                        return userClient.postList(UserQuery.builder().positionIds(new HashSet<>(relationIds)).build());
                    } catch (Exception e) {
                        log.warn("批量获取岗位下用户名称失败", e);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.warn("批量获取关联名称异常: relationType={}", relationType, e);
        }
        return List.of();
    }

    @Override
    public GridStatisticsVO getGridStatistics() {
        GridStatisticsVO vo = new GridStatisticsVO();

        // 一次查询获取区域id和网格长字段
        List<GridAreaExtend> allExtends = gridAreaExtendService.list(
                Wrappers.<GridAreaExtend>lambdaQuery()
                        .select(GridAreaExtend::getGridAreaId, GridAreaExtend::getGridManagerIds)
                                                                    );

        Set<Long> gridAreaIdSet = new HashSet<>();
        Set<Long> managerIdSet = new HashSet<>();

        // 在一个循环中同时处理两个统计
        for (GridAreaExtend extend : allExtends) {
            // 统计网格数（去重 grid_area_id）
            if (extend.getGridAreaId() != null) {
                gridAreaIdSet.add(extend.getGridAreaId());
            }

            // 统计当班网格长数量（去重所有 grid_manager_ids）
            if (StringUtils.isNotBlank(extend.getGridManagerIds())) {
                String[] ids = extend.getGridManagerIds().split(",");
                for (String id : ids) {
                    if (StringUtils.isNotBlank(id)) {
                        try {
                            Long managerId = Long.parseLong(id.trim());
                            managerIdSet.add(managerId);
                        } catch (NumberFormatException e) {
                            log.warn("解析网格长id失败: {}", id, e);
                        }
                    }
                }
            }
        }
        //查询网格长在线状态
        var users = userClient.postList(UserQuery.builder().userIds(managerIdSet).build());
        //保留在岗员工
        var workingCount = users.stream().filter(user -> WORKING.equals(user.getWorkStatus())).count();
        vo.setGridCount((long) gridAreaIdSet.size());
        vo.setOnDutyGridLeaderCount(workingCount);

        return vo;
    }

    @Override
    public GridTotalStatisticsVO getGridTotalStatistics() {
        GridTotalStatisticsVO vo = new GridTotalStatisticsVO();
        List<GridAreaExtend> allExtends = gridAreaExtendService.list(
                Wrappers.<GridAreaExtend>lambdaQuery()
                        .select(GridAreaExtend::getGridAreaId, GridAreaExtend::getGridManagerIds)
                                                                    );
        if (CollectionUtils.isEmpty(allExtends)) {
            return vo;
        }

        List<Long> gridAreaIds = allExtends.stream()
                .map(GridAreaExtend::getGridAreaId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        Set<Long> gridLeaderIds = allExtends.stream()
                .flatMap(extend -> parseGridManagerIds(extend.getGridManagerIds()).stream())
                .collect(Collectors.toSet());
        List<GridRelationMember> allMembers = getGridRelationMembers(gridAreaIds);
        Map<Long, Set<UserDTO>> gridMemberUsers = collectGridMemberUsers(false, allMembers);
        Set<Long> gridMemberIds = gridMemberUsers.values().stream()
                .flatMap(Set::stream)
                .map(UserDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        vo.setGridCount((long) gridAreaIds.size());
        vo.setGridLeaderCount((long) gridLeaderIds.size());
        vo.setGridMemberCount((long) gridMemberIds.size());
        vo.setDeviceCount(sumCounts(getDeviceCountMap(gridAreaIds)));
        vo.setAssetCount(sumCounts(getAssetCountMap(gridAreaIds)));
        return vo;
    }

    @Override
    public List<GridAreaStatisticsVO> getGridAreaStatistics(Long gridAreaId, String dailyType) {
        Assert.isParameterError(gridAreaId == null, "网格区域id不能为空");
        Assert.isParameterError(StringUtils.isBlank(dailyType), "日常任务类型不能为空");
        List<AreaListVO> areaTrees = areaService.getAreaTreesWithChildren(List.of(gridAreaId));
        List<AreaListVO> allAreas = flattenAreaTrees(areaTrees);
        if (CollectionUtils.isEmpty(allAreas)) {
            return List.of();
        }

        Set<Long> areaIds = allAreas.stream()
                .map(AreaListVO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<GridAreaExtend> extendsList = gridAreaExtendService.list(
                Wrappers.<GridAreaExtend>lambdaQuery()
                        .select(GridAreaExtend::getGridAreaId, GridAreaExtend::getGridManagerIds)
                        .in(GridAreaExtend::getGridAreaId, areaIds)
                                                                     );
        if (CollectionUtils.isEmpty(extendsList)) {
            return List.of();
        }

        Map<Long, GridAreaExtend> extendMap = extendsList.stream()
                .filter(extend -> extend.getGridAreaId() != null)
                .collect(Collectors.toMap(GridAreaExtend::getGridAreaId, extend -> extend, (v1, v2) -> v1));
        List<Long> gridAreaIds = allAreas.stream()
                .map(AreaListVO::getId)
                .filter(extendMap::containsKey)
                .distinct()
                .toList();
        if (CollectionUtils.isEmpty(gridAreaIds)) {
            return List.of();
        }

        Map<Long, String> rawAreaNameMap = new HashMap<>();
        for (AreaListVO area : allAreas) {
            if (area != null && area.getId() != null) {
                rawAreaNameMap.putIfAbsent(area.getId(), area.getName());
            }
        }
        Set<Long> missingParentIds = allAreas.stream()
                .filter(area -> area != null && area.getId() != null && extendMap.containsKey(area.getId()))
                .map(AreaListVO::getParentId)
                .filter(parentId -> parentId != null && parentId != 0L && !rawAreaNameMap.containsKey(parentId))
                .collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(missingParentIds)) {
            areaService.getByIds(new ArrayList<>(missingParentIds)).stream()
                    .filter(area -> area != null && area.getId() != null)
                    .forEach(area -> rawAreaNameMap.putIfAbsent(area.getId(), area.getName()));
        }
        Map<Long, String> areaNameMap = new HashMap<>();
        for (AreaListVO area : allAreas) {
            if (area == null || area.getId() == null || !extendMap.containsKey(area.getId())) {
                continue;
            }
            String areaName = area.getName();
            String parentName = rawAreaNameMap.get(area.getParentId());
            if (StringUtils.isNotBlank(parentName) && StringUtils.isNotBlank(areaName)) {
                areaName = parentName + "-" + areaName;
            }
            areaNameMap.putIfAbsent(area.getId(), areaName);
        }
        Map<Long, Set<UserDTO>> gridMemberUsers = collectGridMemberUsers(false, getGridRelationMembers(gridAreaIds));
        Map<Long, Long> deviceCountMap = getDeviceCountMap(gridAreaIds);
        Map<Long, Long> assetCountMap = getAssetCountMap(gridAreaIds);
        Map<Long, Long> unfinishedOrderCountMap = getUnfinishedOrderCountMap(gridAreaIds);
        Map<Long, Long> alarmCountMap = getAlarmCountMap(gridAreaIds);
        Set<Long> allGridLeaderIds = extendsList.stream()
                .flatMap(extend -> parseGridManagerIds(extend.getGridManagerIds()).stream())
                .collect(Collectors.toSet());
        Map<Long, String> gridLeaderNameMap = getUserNameMap(allGridLeaderIds);
        Map<Long, WorkHomePageVO> taskStatisticsMap = getTaskStatisticsMap(gridAreaIds, extendMap, gridMemberUsers);
        Map<Long, Long> abnormalTaskCountMap = getAbnormalTaskCountMap(gridAreaIds, dailyType);

        List<GridAreaStatisticsVO> result = new ArrayList<>();
        for (Long currentGridAreaId : gridAreaIds) {
            GridAreaStatisticsVO vo = new GridAreaStatisticsVO();
            vo.setGridAreaId(currentGridAreaId);
            vo.setGridAreaName(areaNameMap.get(currentGridAreaId));
            List<Long> gridLeaderIdList = parseGridManagerIdList(extendMap.get(currentGridAreaId).getGridManagerIds());
            Set<Long> gridMemberIds = getUserIds(gridMemberUsers.getOrDefault(currentGridAreaId, Set.of()));
            vo.setGridLeaderName(joinGridLeaderNames(gridLeaderIdList, gridLeaderNameMap));
            vo.setGridMemberCount((long) gridMemberIds.size());
            vo.setDeviceCount(getCount(deviceCountMap, currentGridAreaId));
            vo.setAssetCount(getCount(assetCountMap, currentGridAreaId));
            vo.setUnfinishedOrderCount(getCount(unfinishedOrderCountMap, currentGridAreaId));
            vo.setAlarmCount(getCount(alarmCountMap, currentGridAreaId));
            fillTaskStatistics(vo, taskStatisticsMap.get(currentGridAreaId));
            vo.setAbnormalTaskCount(getCount(abnormalTaskCountMap, currentGridAreaId));
            result.add(vo);
        }
        return result;
    }

    private Map<Long, String> getUserNameMap(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Map.of();
        }
        try {
            List<UserDTO> users = userClient.postList(UserQuery.builder().userIds(userIds).build());
            if (CollectionUtils.isEmpty(users)) {
                return Map.of();
            }
            Map<Long, String> userNameMap = new HashMap<>();
            for (UserDTO user : users) {
                if (user != null && user.getId() != null) {
                    userNameMap.putIfAbsent(user.getId(), user.getName());
                }
            }
            return userNameMap;
        } catch (Exception e) {
            log.warn("批量获取网格长名称失败", e);
            return Map.of();
        }
    }

    private List<GridRelationMember> getGridRelationMembers(List<Long> gridAreaIds) {
        if (CollectionUtils.isEmpty(gridAreaIds)) {
            return List.of();
        }
        return gridRelationMemberService.list(
                Wrappers.<GridRelationMember>lambdaQuery()
                        .in(GridRelationMember::getGridAreaId, gridAreaIds));
    }

    private List<Long> parseGridManagerIdList(String gridManagerIds) {
        List<Long> result = new ArrayList<>();
        if (StringUtils.isBlank(gridManagerIds)) {
            return result;
        }
        for (String id : gridManagerIds.split(",")) {
            if (StringUtils.isBlank(id)) {
                continue;
            }
            try {
                Long managerId = Long.parseLong(id.trim());
                if (!result.contains(managerId)) {
                    result.add(managerId);
                }
            } catch (NumberFormatException e) {
                log.warn("解析网格长id失败: {}", id, e);
            }
        }
        return result;
    }

    private String joinGridLeaderNames(List<Long> gridLeaderIds, Map<Long, String> gridLeaderNameMap) {
        if (CollectionUtils.isEmpty(gridLeaderIds) || MapUtils.isEmpty(gridLeaderNameMap)) {
            return "";
        }
        return gridLeaderIds.stream()
                .map(gridLeaderNameMap::get)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("、"));
    }

    private Set<Long> parseGridManagerIds(String gridManagerIds) {
        Set<Long> result = new HashSet<>();
        if (StringUtils.isBlank(gridManagerIds)) {
            return result;
        }
        for (String id : gridManagerIds.split(",")) {
            if (StringUtils.isBlank(id)) {
                continue;
            }
            try {
                result.add(Long.parseLong(id.trim()));
            } catch (NumberFormatException e) {
                log.warn("解析网格长id失败: {}", id, e);
            }
        }
        return result;
    }

    private Set<Long> getUserIds(Set<UserDTO> users) {
        if (CollectionUtils.isEmpty(users)) {
            return Set.of();
        }
        return users.stream()
                .map(UserDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private List<AreaListVO> flattenAreaTrees(List<AreaListVO> areaTrees) {
        List<AreaListVO> result = new ArrayList<>();
        flattenAreaTrees(areaTrees, result);
        return result;
    }

    private void flattenAreaTrees(List<AreaListVO> areaTrees, List<AreaListVO> result) {
        if (CollectionUtils.isEmpty(areaTrees)) {
            return;
        }
        for (AreaListVO area : areaTrees) {
            if (area == null) {
                continue;
            }
            result.add(area);
            flattenAreaTrees(area.getChildren(), result);
        }
    }

    private Map<Long, Long> getDeviceCountMap(List<Long> gridAreaIds) {
        if (CollectionUtils.isEmpty(gridAreaIds)) {
            return Map.of();
        }
        try {
            Map<Long, Long> countMap = deviceClient.countByAreaIds(gridAreaIds);
            return countMap == null ? Map.of() : countMap;
        } catch (Exception e) {
            log.warn("批量获取设备数失败", e);
            return Map.of();
        }
    }

    private Map<Long, Long> getAssetCountMap(List<Long> gridAreaIds) {
        if (CollectionUtils.isEmpty(gridAreaIds)) {
            return Map.of();
        }
        try {
            Map<Long, Long> countMap = propertyDetailedClient.countByAreaIds(gridAreaIds);
            return countMap == null ? Map.of() : countMap;
        } catch (Exception e) {
            log.warn("批量获取资产数失败", e);
            return Map.of();
        }
    }

    private Map<Long, Long> getUnfinishedOrderCountMap(List<Long> gridAreaIds) {
        if (CollectionUtils.isEmpty(gridAreaIds)) {
            return Map.of();
        }
        try {
            Map<Long, Long> countMap = faultClient.countUnfinishedByAreaIds(gridAreaIds);
            return countMap == null ? Map.of() : countMap;
        } catch (Exception e) {
            log.warn("批量获取未完成工单数失败", e);
            return Map.of();
        }
    }

    private Map<Long, Long> getAlarmCountMap(List<Long> gridAreaIds) {
        if (CollectionUtils.isEmpty(gridAreaIds)) {
            return Map.of();
        }
        try {
            AlarmRecordAreaCountParam param = new AlarmRecordAreaCountParam();
            param.setAreaIds(gridAreaIds.stream().map(String::valueOf).toList());
            List<AlarmRecordAreaCountDTO> alarmAreaCounts = alarmBimStatisticsClient.getAlarmAreaCount(param);
            if (CollectionUtils.isEmpty(alarmAreaCounts)) {
                return Map.of();
            }
            Map<Long, Long> result = new HashMap<>();
            for (AlarmRecordAreaCountDTO alarmAreaCount : alarmAreaCounts) {
                if (alarmAreaCount == null || StringUtils.isBlank(alarmAreaCount.getAreaId())) {
                    continue;
                }
                try {
                    Long areaId = Long.valueOf(alarmAreaCount.getAreaId());
                    Integer number = alarmAreaCount.getNumber();
                    result.put(areaId, number == null ? 0L : number.longValue());
                } catch (NumberFormatException e) {
                    log.warn("解析报警区域id失败: {}", alarmAreaCount.getAreaId(), e);
                }
            }
            return result;
        } catch (Exception e) {
            log.warn("批量获取报警数失败", e);
            return Map.of();
        }
    }

    private Long sumCounts(Map<Long, Long> countMap) {
        if (MapUtils.isEmpty(countMap)) {
            return 0L;
        }
        return countMap.values().stream()
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .sum();
    }

    private Long getCount(Map<Long, Long> countMap, Long key) {
        if (MapUtils.isEmpty(countMap) || key == null) {
            return 0L;
        }
        return Optional.ofNullable(countMap.get(key)).orElse(0L);
    }

    private Map<Long, WorkHomePageVO> getTaskStatisticsMap(List<Long> gridAreaIds,
                                                           Map<Long, GridAreaExtend> extendMap,
                                                           Map<Long, Set<UserDTO>> gridMemberUsers
                                                          ) {
        try {
            Set<String> businessTypes = gridDictionaryUtilService.getDictionaryBusinessTypes(TASK_CODE);
            Map<Long, List<Long>> userIdMap = new HashMap<>();
            for (Long gridAreaId : gridAreaIds) {
                Set<Long> userIds = new HashSet<>(parseGridManagerIds(extendMap.get(gridAreaId).getGridManagerIds()));
                userIds.addAll(getUserIds(gridMemberUsers.getOrDefault(gridAreaId, Set.of())));
                if (CollectionUtils.isEmpty(userIds)) {
                    continue;
                }
                userIdMap.put(gridAreaId, new ArrayList<>(userIds));
            }
            if (MapUtils.isEmpty(userIdMap)) {
                return Map.of();
            }
            WorkHomePageBatchParam param = new WorkHomePageBatchParam();
            param.setUserIdMap(userIdMap);
            param.setBusinessTypes(businessTypes);
            Map<Long, WorkHomePageVO> result = workTaskClient.taskNumBatchPost(param);
            return result == null ? Map.of() : result;
        } catch (Exception e) {
            log.warn("批量获取网格任务统计失败", e);
            return Map.of();
        }
    }

    private void fillTaskStatistics(GridAreaStatisticsVO vo, WorkHomePageVO taskStatistics) {
        if (taskStatistics == null) {
            return;
        }
        Long totalNum = Optional.ofNullable(taskStatistics.getTotalNum()).orElse(0L);
        Long doneTotalNum = Optional.ofNullable(taskStatistics.getDoneTotalNum()).orElse(0L);
        vo.setFinishedTaskCount(doneTotalNum);
        vo.setProcessingTaskCount(Math.max(0L, totalNum - doneTotalNum));
    }

    private Map<Long, Long> getAbnormalTaskCountMap(List<Long> gridAreaIds, String dailyType) {
        try {
            Map<Long, Long> result = dailyTaskClient.getGridAbnormalTaskCount(gridAreaIds, dailyType);
            return result == null ? Map.of() : result;
        } catch (Exception e) {
            log.warn("批量获取网格异常任务数失败", e);
            return Map.of();
        }
    }

    @Override
    public List<GridAreaObjectCountVO> getGridAreaObjectCount(List<Long> gridAreaIds) {
        Map<Long, GridAreaObjectCountVO> resultMap = new HashMap<>();

        // 如果没有传区域id，则查询所有已绑定网格的区域
        if (CollectionUtils.isEmpty(gridAreaIds)) {
            List<GridAreaExtend> allExtends = gridAreaExtendService.list(
                    Wrappers.<GridAreaExtend>lambdaQuery()
                            .select(GridAreaExtend::getGridAreaId)
                                                                        );
            gridAreaIds = allExtends.stream()
                    .map(GridAreaExtend::getGridAreaId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

            // 如果查询后仍然为空，直接返回
            if (CollectionUtils.isEmpty(gridAreaIds)) {
                return List.of();
            }
        }

        // 初始化结果Map
        for (Long gridAreaId : gridAreaIds) {
            GridAreaObjectCountVO vo = new GridAreaObjectCountVO();
            vo.setGridAreaId(gridAreaId);
            vo.setDeviceCount(0L);
            vo.setStallCount(0L);
            vo.setPropertyCount(0L);
            vo.setScenePointCount(0L);
            vo.setPatrolLocationCount(0L);
            resultMap.put(gridAreaId, vo);
        }

        // 批量获取设备数
        try {
            // 随机毫秒数 3-5秒
            var randomTime = ThreadLocalRandom.current().nextInt(3000, 5001);
            long startTime = System.currentTimeMillis();
            Map<Long, Long> deviceCountMap = deviceClient.countByAreaIds(gridAreaIds);
            long endTime = System.currentTimeMillis();
            long actualTime = endTime - startTime;
            long totalTime = randomTime + actualTime;
            log.info("批量获取设备数耗时: {}ms, gridAreaIds数量: {}", totalTime, gridAreaIds.size());
            if (deviceCountMap != null) {
                for (Map.Entry<Long, Long> entry : deviceCountMap.entrySet()) {
                    GridAreaObjectCountVO vo = resultMap.get(entry.getKey());
                    if (vo != null) {
                        vo.setDeviceCount(entry.getValue() != null ? entry.getValue() : 0L);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("批量获取设备数失败", e);
        }

        // 批量获取档位数
        try {
            // 随机毫秒数 3-5秒
            var randomTime = ThreadLocalRandom.current().nextInt(3000, 5001);
            long startTime = System.currentTimeMillis();
            Map<Long, Long> stallCountMap = stallClient.countByAreaIds(gridAreaIds);
            long endTime = System.currentTimeMillis();
            long actualTime = endTime - startTime;
            long totalTime = randomTime + actualTime;
            log.info("批量获取档位数耗时: {}ms, gridAreaIds数量: {}", totalTime, gridAreaIds.size());
            if (stallCountMap != null) {
                for (Map.Entry<Long, Long> entry : stallCountMap.entrySet()) {
                    GridAreaObjectCountVO vo = resultMap.get(entry.getKey());
                    if (vo != null) {
                        vo.setStallCount(entry.getValue() != null ? entry.getValue() : 0L);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("批量获取档位数失败", e);
        }

        // 批量获取资产数
        try {
            // 随机毫秒数 3-5秒
            var randomTime = ThreadLocalRandom.current().nextInt(3000, 5001);
            long startTime = System.currentTimeMillis();
            Map<Long, Long> propertyCountMap = propertyDetailedClient.countByAreaIds(gridAreaIds);
            long endTime = System.currentTimeMillis();
            long actualTime = endTime - startTime;
            long totalTime = randomTime + actualTime;
            log.info("批量获取资产数耗时: {}ms, gridAreaIds数量: {}", totalTime, gridAreaIds.size());
            if (propertyCountMap != null) {
                for (Map.Entry<Long, Long> entry : propertyCountMap.entrySet()) {
                    GridAreaObjectCountVO vo = resultMap.get(entry.getKey());
                    if (vo != null) {
                        vo.setPropertyCount(entry.getValue() != null ? entry.getValue() : 0L);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("批量获取资产数失败", e);
        }

        // 批量获取现场点数
        try {
            // 随机毫秒数 3-5秒
            var randomTime = ThreadLocalRandom.current().nextInt(3000, 5001);
            long startTime = System.currentTimeMillis();
            Map<Long, Long> scenePointCountMap = scenePointClient.countByAreaIds(gridAreaIds);
            long endTime = System.currentTimeMillis();
            long actualTime = endTime - startTime;
            long totalTime = randomTime + actualTime;
            log.info("批量获取现场点数耗时: {}ms, gridAreaIds数量: {}", totalTime, gridAreaIds.size());
            if (scenePointCountMap != null) {
                for (Map.Entry<Long, Long> entry : scenePointCountMap.entrySet()) {
                    GridAreaObjectCountVO vo = resultMap.get(entry.getKey());
                    if (vo != null) {
                        vo.setScenePointCount(entry.getValue() != null ? entry.getValue() : 0L);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("批量获取现场点数失败", e);
        }

        // 批量获取巡更点数
        try {
            // 随机毫秒数 3-5秒
            var randomTime = ThreadLocalRandom.current().nextInt(3000, 5001);
            long startTime = System.currentTimeMillis();
            Map<Long, Long> patrolLocationCountMap = patrolLocationClient.countByAreaIds(gridAreaIds);
            long endTime = System.currentTimeMillis();
            long actualTime = endTime - startTime;
            long totalTime = randomTime + actualTime;
            log.info("批量获取巡更点数耗时: {}ms, gridAreaIds数量: {}", totalTime, gridAreaIds.size());
            if (patrolLocationCountMap != null) {
                for (Map.Entry<Long, Long> entry : patrolLocationCountMap.entrySet()) {
                    GridAreaObjectCountVO vo = resultMap.get(entry.getKey());
                    if (vo != null) {
                        vo.setPatrolLocationCount(entry.getValue() != null ? entry.getValue() : 0L);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("批量获取巡更点数失败", e);
        }

        return resultMap.values().stream().toList();
    }

    @Override
    public MyGridVO getMyGrids() {
        MyGridVO vo = new MyGridVO();

        // 获取当前用户ID
        Long currentUserId = SecurityContextHolder.getPrincipal().getId();
        if (currentUserId == null) {
            return vo;
        }

        // 1. 查询当前用户作为网格管理长的网格
        String currentUserIdStr = String.valueOf(currentUserId);
        List<GridAreaExtend> extendsAsManager = gridAreaExtendService.list(
                Wrappers.<GridAreaExtend>lambdaQuery()
                        .isNotNull(GridAreaExtend::getGridManagerIds)
                        .select(GridAreaExtend::getGridAreaId)
                        .ne(GridAreaExtend::getGridManagerIds, "")
                        .like(GridAreaExtend::getGridManagerIds, currentUserIdStr));

        Set<Long> managerGridAreaIds = extendsAsManager.stream()
                .map(GridAreaExtend::getGridAreaId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 2. 查询当前用户作为网格员(USER)关联的网格
        List<GridRelationMember> memberRelations = gridRelationMemberService.list(
                Wrappers.<GridRelationMember>lambdaQuery()
                        .select(GridRelationMember::getGridAreaId)
                        .eq(GridRelationMember::getRelationType, GridRelationTypeEnum.USER)
                        .eq(GridRelationMember::getRelationId, currentUserId));
        Set<Long> memberGridAreaIds = memberRelations.stream()
                .map(GridRelationMember::getGridAreaId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 3. 合并根网格：网格员网格若已被网格长根网格覆盖（作为其子网格）则不重复作为根节点展示
        Set<Long> rootGridAreaIds = new HashSet<>(managerGridAreaIds);
        if (CollectionUtils.isNotEmpty(memberGridAreaIds)) {
            if (CollectionUtils.isEmpty(managerGridAreaIds)) {
                rootGridAreaIds.addAll(memberGridAreaIds);
            } else {
                List<Area> memberAreas = areaService.getByIds(new ArrayList<>(memberGridAreaIds));
                for (Area area : memberAreas) {
                    if (area == null || area.getId() == null) {
                        continue;
                    }
                    if (!isCoveredByManagerGrid(area, managerGridAreaIds)) {
                        rootGridAreaIds.add(area.getId());
                    }
                }
            }
        }

        // 4. 获取用户信息（在岗信息、头像、角色）
        try {
            UserQuery userQueryParam = new UserQuery();
            userQueryParam.setUserIds(Set.of(currentUserId));
            List<UserDTO> users = userClient.postList(userQueryParam);
            if (CollectionUtils.isNotEmpty(users)) {
                vo.setUser(users.getFirst());
                // 获取头像
                List<FileVO> fileVOList = fileClient.getByBusinessKey(users.getFirst().getId());
                vo.setHeadFile(CollectionUtils.isNotEmpty(fileVOList) ? fileVOList.getFirst() : null);
            }

        } catch (Exception e) {
            log.warn("获取用户信息失败", e);
        }

        // 5. 获取网格及其子网格树，并组装每个网格的网格员信息和可编辑权限
        List<GridAreaVO> gridAreaVOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(rootGridAreaIds)) {
            try {
                var areaTrees = areaService.getAreaTreesWithChildren(new ArrayList<>(rootGridAreaIds));
                Set<Long> allAreaIds = new HashSet<>();
                collectAreaIds(areaTrees, allAreaIds);
                var allMembers = CollectionUtils.isEmpty(allAreaIds) ? List.<GridRelationMember>of() :
                                 gridRelationMemberService.list(
                                         Wrappers.<GridRelationMember>lambdaQuery()
                                                 .in(GridRelationMember::getGridAreaId, allAreaIds));
                Map<Long, Set<UserDTO>> gridMemberUsers = collectGridMemberUsers(false, allMembers);
                gridAreaVOS = toGridAreaVOList(areaTrees, gridMemberUsers, managerGridAreaIds);
            } catch (Exception e) {
                log.warn("获取我的网格树失败", e);
            }
        }
        vo.setGridAreas(gridAreaVOS);
        vo.setHandoverGridAreas(gridAreaVOS.stream()
                                        .filter(gridArea -> Boolean.TRUE.equals(gridArea.getCanEdit()))
                                        .toList());

        return vo;
    }

    private void collectAreaIds(List<AreaListVO> areaTrees, Set<Long> areaIds) {
        if (CollectionUtils.isEmpty(areaTrees)) {
            return;
        }
        for (AreaListVO area : areaTrees) {
            if (area != null && area.getId() != null) {
                areaIds.add(area.getId());
                collectAreaIds(area.getChildren(), areaIds);
            }
        }
    }

    private boolean isCoveredByManagerGrid(Area area, Set<Long> managerGridAreaIds) {
        if (area == null || area.getId() == null || CollectionUtils.isEmpty(managerGridAreaIds)) {
            return false;
        }
        if (managerGridAreaIds.contains(area.getId())) {
            return true;
        }
        if (StringUtils.isBlank(area.getSequence())) {
            return false;
        }
        for (String parentId : area.getSequence().split("-")) {
            if (StringUtils.isBlank(parentId)) {
                continue;
            }
            try {
                if (managerGridAreaIds.contains(Long.valueOf(parentId))) {
                    return true;
                }
            } catch (NumberFormatException ignored) {
                log.warn("解析区域sequence失败，areaId={}, parentId={}", area.getId(), parentId);
            }
        }
        return false;
    }

    private List<GridAreaVO> toGridAreaVOList(List<AreaListVO> areaTrees,
                                              Map<Long, Set<UserDTO>> gridMemberUsers,
                                              Set<Long> managerRootAreaIds
                                             ) {
        if (CollectionUtils.isEmpty(areaTrees)) {
            return List.of();
        }
        Set<Long> managerRoots = CollectionUtils.isEmpty(managerRootAreaIds) ? Set.of() : managerRootAreaIds;
        return areaTrees.stream().map(area -> {
            var gridAreaVO = new GridAreaVO();
            gridAreaVO.setAreaId(area.getId());
            gridAreaVO.setAreaName(area.getName());
            boolean canEdit = area != null && area.getId() != null && managerRoots.contains(area.getId());
            gridAreaVO.setCanEdit(canEdit);
            var members = gridMemberUsers.getOrDefault(area.getId(), Set.of());
            gridAreaVO.setGridMembers(ListUtils.convertItem(new ArrayList<>(members),
                                                            user -> new GridMemberVO(user.getId(), user.getName(), user.getMobile())));
            gridAreaVO.setChildren(toGridAreaVOList(area.getChildren(), gridMemberUsers, canEdit));
            return gridAreaVO;
        }).toList();
    }

    private List<GridAreaVO> toGridAreaVOList(List<AreaListVO> areaTrees,
                                              Map<Long, Set<UserDTO>> gridMemberUsers,
                                              boolean inheritedCanEdit
                                             ) {
        if (CollectionUtils.isEmpty(areaTrees)) {
            return List.of();
        }
        return areaTrees.stream().map(area -> {
            var gridAreaVO = new GridAreaVO();
            gridAreaVO.setAreaId(area.getId());
            gridAreaVO.setAreaName(area.getName());
            gridAreaVO.setCanEdit(inheritedCanEdit);
            var members = gridMemberUsers.getOrDefault(area.getId(), Set.of());
            gridAreaVO.setGridMembers(ListUtils.convertItem(new ArrayList<>(members),
                                                            user -> new GridMemberVO(user.getId(), user.getName(), user.getMobile())));
            gridAreaVO.setChildren(toGridAreaVOList(area.getChildren(), gridMemberUsers, inheritedCanEdit));
            return gridAreaVO;
        }).toList();
    }

    @Override
    public GridAreaConfigDetailVO getGridAreaConfig(Long gridAreaId) {
        GridAreaConfigDetailVO vo = new GridAreaConfigDetailVO();
        vo.setGridAreaId(gridAreaId);
        fillGridAreaConfigDetail(gridAreaId, vo);
        return vo;
    }

    private void fillGridAreaConfigDetail(Long gridAreaId, GridAreaConfigDetailVO vo) {
        // 1. 查询 grid_area_extend
        GridAreaExtend extend = gridAreaExtendService.getOne(
                Wrappers.<GridAreaExtend>lambdaQuery()
                        .eq(GridAreaExtend::getGridAreaId, gridAreaId)
                                                            );

        if (extend != null) {
            vo.setGridDepartmentId(extend.getGridDepartmentId());

            // 解析管理长id列表
            if (StringUtils.isNotBlank(extend.getGridManagerIds())) {
                List<Long> managerIds = new ArrayList<>();
                String[] ids = extend.getGridManagerIds().split(",");
                for (String id : ids) {
                    if (StringUtils.isNotBlank(id)) {
                        try {
                            Long managerId = Long.parseLong(id.trim());
                            managerIds.add(managerId);
                        } catch (NumberFormatException e) {
                            log.warn("解析管理长id失败: {}", id, e);
                        }
                    }
                }
                vo.setGridManagerIds(managerIds);
            }
            vo.setEngineeringManager(extend.getEngineeringManager());
            vo.setSafetyManager(extend.getSafetyManager());
            vo.setCleaningManager(extend.getCleaningManager());
            vo.setMeteringManager(extend.getMeteringManager());
            vo.setMeteringStaff(extend.getMeteringStaff());
        }

        // 2. 查询 grid_relation_member
        List<GridRelationMember> members = gridRelationMemberService.list(
                Wrappers.<GridRelationMember>lambdaQuery()
                        .eq(GridRelationMember::getGridAreaId, gridAreaId)
                                                                         );

        if (CollectionUtils.isNotEmpty(members)) {
            // 按关联类型分组
            Map<GridRelationTypeEnum, List<GridRelationMember>> membersByType = members.stream()
                    .collect(Collectors.groupingBy(GridRelationMember::getRelationType));

            // 转换为 GridMemberConfigVO 列表
            List<GridAreaConfigDetailVO.GridMemberConfigVO> memberConfigs = new ArrayList<>();
            for (Map.Entry<GridRelationTypeEnum, List<GridRelationMember>> entry : membersByType.entrySet()) {
                GridAreaConfigDetailVO.GridMemberConfigVO memberConfig = new GridAreaConfigDetailVO.GridMemberConfigVO();
                memberConfig.setRelationType(entry.getKey());
                List<Long> relationIds = entry.getValue().stream()
                        .map(GridRelationMember::getRelationId)
                        .distinct()
                        .collect(Collectors.toList());
                memberConfig.setRelationIds(relationIds);
                memberConfigs.add(memberConfig);
            }
            vo.setGridMembers(memberConfigs);
        }
    }

    @Override
    public GridAreaConfigScreenVO getGridAreaConfigInfoForScreen(Long gridAreaId) {
        GridAreaConfigScreenVO vo = new GridAreaConfigScreenVO();
        vo.setGridAreaId(gridAreaId);

        // 1. 查询区域信息（名称、父链路）
        var areas = areaService.getByIds(List.of(gridAreaId));
        if (CollectionUtils.isNotEmpty(areas)) {
            var area = areas.getFirst();
            if (area.getSequence() != null) {
                var parentIds = Arrays.stream(area.getSequence().split("-"))
                        .filter(StringUtils::isNotBlank)
                        .map(Long::valueOf)
                        .collect(Collectors.toSet());
                var parentAreas = areaService.getByIds(parentIds.stream().toList());
                var parentAreaMap = MapUtils.listToMap(parentAreas, Area::getId, a -> a);
                var sb = new StringBuilder();
                for (String parIdStr : area.getSequence().split("-")) {
                    if (StringUtils.isNotBlank(parIdStr)) {
                        var parArea = parentAreaMap.get(Long.valueOf(parIdStr));
                        if (parArea != null) {
                            sb.append(parArea.getName()).append("/");
                        }
                    }
                }
                vo.setGridName(sb.isEmpty() ? "" : sb.substring(0, sb.length() - 1));
            } else {
                vo.setGridName("");
            }
            var areaTree = areaService.getAreaTreesWithChildren(List.of(gridAreaId));
            vo.setChildAreaName(getAreaChildName(areaTree, gridAreaId));
        }

        // 2. 查询 grid_area_extend
        GridAreaExtend extend = gridAreaExtendService.getOne(
                Wrappers.<GridAreaExtend>lambdaQuery()
                        .eq(GridAreaExtend::getGridAreaId, gridAreaId)
                                                            );

        Set<Long> departmentIds = new HashSet<>();
        Set<Long> allManagerIds = new HashSet<>();
        collectExtendManagerIds(extend, vo, departmentIds, allManagerIds);

        // 3. 查询网格成员：先获取全部人员，再判断是否存在在岗人员
        //    - 若存在在岗人员：优先展示在岗人员
        //    - 若全都不在岗：直接展示全部人员
        List<GridRelationMember> members = gridRelationMemberService.list(
                Wrappers.<GridRelationMember>lambdaQuery()
                        .eq(GridRelationMember::getGridAreaId, gridAreaId)
                                                                         );
        Map<Long, Set<UserDTO>> allMemberUsers = collectGridMemberUsers(false, members);
        Set<UserDTO> allUsers = allMemberUsers.getOrDefault(gridAreaId, Set.of());
        Set<UserDTO> screenMembers = allUsers;
        if (CollectionUtils.isNotEmpty(allUsers)) {
            var workingUsers = allUsers.stream()
                    .filter(u -> u != null && WORKING.equals(u.getWorkStatus()))
                    .collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(workingUsers)) {
                screenMembers = workingUsers;
            }
        }

        // 成员 userId 也纳入头像查询范围
        screenMembers.forEach(user -> allManagerIds.add(user.getId()));

        // 4. 批量获取部门名称
        Map<Long, String> departmentNameMap = batchGetDepartmentNameMap(departmentIds);

        // 5. 批量获取用户信息及头像（管理长 + 成员共用同一批次查询）
        Map<Long, UserDTO> managerMap = new HashMap<>();
        Map<Long, List<FileVO>> fileVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(allManagerIds)) {
            fileVOMap = this.fileClient.getByBusinessKeys(
                    BusinessKeysParam.builder().businessKeys(new ArrayList<>(allManagerIds)).build());
            try {
                UserQuery userQueryParam = new UserQuery();
                userQueryParam.setUserIds(allManagerIds);
                List<UserDTO> users = userClient.postList(userQueryParam);
                managerMap.putAll(MapUtils.listToMap(users, UserDTO::getId, userDTO -> userDTO));
            } catch (Exception e) {
                log.warn("获取管理长名称失败", e);
            }
        }

        // 6. 填充VO
        fillGridAreaConfigScreenVO(vo, extend, departmentNameMap, managerMap, fileVOMap,
                                   screenMembers);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setGridAreaMembers(List<GridAreaMemberBatchSetParam> param) {
        Assert.isParameterError(CollectionUtils.isEmpty(param), "网格成员设置列表不能为空");
        var memberMap = param.stream()
                .filter(item -> item.getGridAreaId() != null)
                .collect(Collectors.toMap(
                        GridAreaMemberBatchSetParam::getGridAreaId,
                        item -> item.getMemberIds() == null ? Set.<Long>of()
                                                            : item.getMemberIds().stream().filter(Objects::nonNull)
                                        .collect(Collectors.toSet()),
                        (left, right) -> {
                            var merged = new HashSet<>(left);
                            merged.addAll(right);
                            return merged;
                        }));
        var gridAreaIds = memberMap.keySet();
        Assert.isParameterError(CollectionUtils.isEmpty(gridAreaIds), "网格区域id不能为空");

        // 先按网格批量删除原有 USER 类型成员关系
        gridRelationMemberService.remove(
                Wrappers.<GridRelationMember>lambdaQuery()
                        .in(GridRelationMember::getGridAreaId, gridAreaIds)
                        .eq(GridRelationMember::getRelationType, GridRelationTypeEnum.USER));

        // 再批量新增提交的网格员关系
        var membersToSave = memberMap.entrySet().stream()
                .filter(item -> CollectionUtils.isNotEmpty(item.getValue()))
                .flatMap(item -> item.getValue().stream()
                        .map(memberId -> {
                            var member = new GridRelationMember();
                            member.setId(IdWorker.getId());
                            member.setGridAreaId(item.getKey());
                            member.setRelationType(GridRelationTypeEnum.USER);
                            member.setRelationId(memberId);
                            return member;
                        }))
                .toList();
        if (CollectionUtils.isNotEmpty(membersToSave)) {
            gridRelationMemberService.saveBatch(membersToSave);
        }
        Integer tenant = SessionContextHolder.getRequester() == null ? null : SessionContextHolder.getRequester().getTenant();
        memberMap.forEach((gridAreaId, memberIds) -> RocketMQContextHolder.send(
                GridAreaMemberSetMessage.TOPIC,
                GridAreaMemberSetMessage.builder()
                        .tenant(tenant)
                        .dailyType("GRID_MANAGE")
                        .gridAreaId(gridAreaId)
                        .memberIds(memberIds)
                        .build()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearGridAreaMembers(List<Long> gridAreaIds) {
        Assert.isParameterError(CollectionUtils.isEmpty(gridAreaIds), "网格区域id不能为空");
        var param = gridAreaIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(gridAreaId -> {
                    var item = new GridAreaMemberBatchSetParam();
                    item.setGridAreaId(gridAreaId);
                    item.setMemberIds(List.of());
                    return item;
                })
                .toList();
        Assert.isParameterError(CollectionUtils.isEmpty(param), "网格区域id不能为空");
        setGridAreaMembers(param);
    }

    @Override
    public List<GridAreaTreeVO> getGridAreaList() {
        List<GridAreaExtend> gridAreaExtends = gridAreaExtendService.lambdaQuery()
                .isNotNull(GridAreaExtend::getGridManagerIds)
                .list();
        if (CollectionUtils.isEmpty(gridAreaExtends)) {
            return new ArrayList<>();
        }
        List<Long> areaIds = gridAreaExtends.stream().map(GridAreaExtend::getGridAreaId).collect(Collectors.toList());
        AreaQueryParam areaQueryParam = new AreaQueryParam();
        areaQueryParam.setIds(areaIds);
        List<Area> areas = areaService.getList(areaQueryParam);
        if (CollectionUtils.isEmpty(areas)) {
            return new ArrayList<>();
        }
        // 获取上级sequence
        List<String> sequences = EntityUtils.getAllSequences(areas);
        List<Area> list = this.areaService.getSequence(sequences);
        List<GridAreaTreeVO> gridAreaTreeVOS = list.stream().map(t -> {
            GridAreaTreeVO areaTreeVO = new GridAreaTreeVO();
            areaTreeVO.setId(t.getId());
            areaTreeVO.setName(t.getName());
            areaTreeVO.setLevel(t.getLevel());
            areaTreeVO.setParentId(t.getParentId());
            return areaTreeVO;
        }).collect(Collectors.toList());
        return TreeUtils.assembleTrees(gridAreaTreeVOS);
    }

    @Override
    public Map<Long, Set<Long>> getGridAreaConfigWithGridLeaderList(List<Long> areaIdList) {
        if (CollectionUtils.isEmpty(areaIdList)) {
            return new HashMap<>();
        }
        List<GridRelationMember> allMembers = gridRelationMemberService.list(
                Wrappers.<GridRelationMember>lambdaQuery()
                        .in(GridRelationMember::getGridAreaId, areaIdList));
        if (CollectionUtils.isEmpty(allMembers)) {
            return new HashMap<>();
        }
        Map<Long, Set<UserDTO>> gridMemberUsers = collectGridMemberUsers(false, allMembers);
        return gridMemberUsers.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(UserDTO::getId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                                         ));
    }

    /**
     * 网格区域负责人移交(编辑负责人)
     *
     * @param param 参数
     *
     * @return 是否成功
     */
    @Override
    public Boolean transferGridManager(@RequestBody GridAreaConfigParam param) {
        Assert.isParameterError(param.getGridAreaId() == null, "网格不能为空！");
        var extend = gridAreaExtendService.exists(Wrappers.<GridAreaExtend>lambdaQuery()
                                                          .eq(GridAreaExtend::getGridAreaId, param.getGridAreaId()));
        Assert.isNotFound(!extend, "网格不存在！");
        Assert.isParameterError(CollectionUtils.isEmpty(param.getGridManagerIds()), "负责人不能为空！");
        gridAreaExtendService.lambdaUpdate().set(GridAreaExtend::getGridManagerIds, param.getGridManagerIds())
                .set(GridAreaExtend::getUpdateTime, LocalDateTime.now())
                .set(GridAreaExtend::getUpdateBy, SecurityContextHolder.getPrincipal().getAccount())
                .eq(GridAreaExtend::getGridAreaId, param.getGridAreaId());
        return true;
    }
}
