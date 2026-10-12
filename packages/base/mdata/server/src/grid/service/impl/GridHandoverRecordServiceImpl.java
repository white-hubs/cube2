package com.guhecloud.cube.biz.mdata.server.grid.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaService;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaVO;
import com.guhecloud.cube.biz.mdata.server.grid.dao.GridAreaExtendMapper;
import com.guhecloud.cube.biz.mdata.server.grid.dao.GridHandoverRecordMapper;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridAreaExtend;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridHandoverRecord;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridHandoverRecordExtend;
import com.guhecloud.cube.biz.mdata.server.grid.enumeration.GridHandoverStatusEnum;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridHandoverAddParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridHandoverRecordExtendParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridHandoverRecordQueryParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridHandoverTakeOverParam;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridHandoverRecordExtendService;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridHandoverRecordService;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridHandoverRecordDetailVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridHandoverRecordExtendVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridHandoverRecordVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_FORMAT;
import static com.guhecloud.platform.base.util.DateUtils.PURE_DATE_FORMAT;

/**
 * 网格交接记录 Service 实现
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GridHandoverRecordServiceImpl extends ServiceImpl<GridHandoverRecordMapper, GridHandoverRecord>
        implements GridHandoverRecordService {
    private static final String GRID_HANDOVER_SCENE = "GRID::HANDOVER";
    private static final String SHIFT_OVER_MESSAGE = "SHIFT_OVER_MESSAGE";
    private static final String TAKE_OVER_MESSAGE = "TAKE_OVER_MESSAGE";
    private final GridAreaExtendMapper gridAreaExtendMapper;
    private final GridHandoverRecordExtendService gridHandoverRecordExtendService;
    private final FileClient fileClient;
    private final FileOperateClient fileOperateClient;
    private final FileRefClient fileRefClient;
    private final UserClient userClient;
    private final AreaService areaService;
    private final MessageSendClient messageSendClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> addHandoverRecord(GridHandoverAddParam param) {
        Long currentUserId = SecurityContextHolder.getPrincipal().getId();
        Assert.isParameterError(Objects.equals(currentUserId, param.getTakeOverUser()), "不能交接给自己");
        Assert.isParameterError(CollectionUtils.isEmpty(param.getGridAreaIds()), "网格区域id列表不能为空");
        List<Long> gridAreaIds = param.getGridAreaIds().stream().filter(Objects::nonNull).distinct().toList();
        Assert.isParameterError(CollectionUtils.isEmpty(gridAreaIds), "网格区域id列表不能为空");

        Map<Long, String> areaNameMap = getAreaNameMap(gridAreaIds);
        var extendsList = gridAreaExtendMapper.selectList(
                Wrappers.<GridAreaExtend>lambdaQuery().in(GridAreaExtend::getGridAreaId, gridAreaIds));
        Assert.isConflict(CollectionUtils.isEmpty(extendsList) || extendsList.size() != gridAreaIds.size(), "存在无效网格");
        Map<Long, GridAreaExtend> extendMap = extendsList.stream()
                .collect(Collectors.toMap(GridAreaExtend::getGridAreaId, item -> item));

        for (Long gridAreaId : gridAreaIds) {
            GridAreaExtend extend = extendMap.get(gridAreaId);
            Assert.isNotFound(extend == null, "网格不存在");
            List<Long> managerIds = parseManagerIds(extend.getGridManagerIds());
            Assert.isConflict(!managerIds.contains(currentUserId),
                              "当前用户不是网格[" + getGridAreaName(areaNameMap, gridAreaId) + "]负责人");
        }

        LocalDateTime now = LocalDateTime.now();
        var handoverTime = DateUtils.format(now, PURE_DATE_FORMAT);
        var record = new GridHandoverRecord();
        record.setId(IdWorker.getId());
        record.setRecordName(handoverTime + "网格交接");
        record.setGridAreaIds(joinGridAreaIds(gridAreaIds));
        record.setShiftOverUser(currentUserId);
        record.setShiftOverTime(now);
        record.setTakeOverUser(param.getTakeOverUser());
        record.setStatus(GridHandoverStatusEnum.WAIT_HANDOVER);
        record.setRemarks(param.getRemarks());
        record.setShiftSignFileId(param.getShiftSignFileId());
        if (Objects.nonNull(param.getShiftSignFileId())) {
            fileOperateClient.fileInduceSingle(GRID_HANDOVER_SCENE, null, param.getShiftSignFileId());
        }
        this.save(record);
        saveHandoverExtend(record.getId(), param.getExtend());
        sendShiftOverMessage(record, areaNameMap);
        return List.of(record.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void takeOver(GridHandoverTakeOverParam param) {
        Long currentUserId = SecurityContextHolder.getPrincipal().getId();
        GridHandoverRecord record = this.getById(param.getId());
        Assert.isNotFound(record == null, "交接记录不存在");
        Assert.isConflict(!GridHandoverStatusEnum.WAIT_HANDOVER.equals(record.getStatus()), "当前记录已接班或已撤销");
        Assert.isConflict(!Objects.equals(record.getTakeOverUser(), currentUserId), "您没有接班权限");

        List<Long> gridAreaIds = parseGridAreaIds(record.getGridAreaIds());
        Assert.isNotFound(CollectionUtils.isEmpty(gridAreaIds), "网格不存在");
        Map<Long, String> areaNameMap = getAreaNameMap(gridAreaIds);
        List<GridAreaExtend> extendsList = gridAreaExtendMapper.selectList(
                Wrappers.<GridAreaExtend>lambdaQuery().in(GridAreaExtend::getGridAreaId, gridAreaIds));
        Assert.isNotFound(CollectionUtils.isEmpty(extendsList) || extendsList.size() != gridAreaIds.size(), "网格不存在");
        Map<Long, GridAreaExtend> extendMap = extendsList.stream()
                .collect(Collectors.toMap(GridAreaExtend::getGridAreaId, item -> item));

        LocalDateTime takeOverTime = LocalDateTime.now();
        for (Long gridAreaId : gridAreaIds) {
            GridAreaExtend extend = extendMap.get(gridAreaId);
            Assert.isNotFound(extend == null, "网格不存在");
            var managerIds = parseManagerIds(extend.getGridManagerIds());
            var replacedManagerIds = replaceManagerId(managerIds, record.getShiftOverUser(), record.getTakeOverUser());
            extend.setGridManagerIds(joinManagerIds(replacedManagerIds));
            gridAreaExtendMapper.updateById(extend);
        }
        record.setStatus(GridHandoverStatusEnum.HAND_OVERED);
        record.setTakeOverTime(takeOverTime);
        record.setTakeSignFileId(param.getTakeSignFileId());
        if (Objects.nonNull(param.getTakeSignFileId())) {
            fileOperateClient.fileInduceSingle(GRID_HANDOVER_SCENE, null, param.getTakeSignFileId());
        }
        this.updateById(record);
        sendTakeOverMessage(record, areaNameMap, takeOverTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(Long id) {
        Long currentUserId = SecurityContextHolder.getPrincipal().getId();
        GridHandoverRecord record = this.getById(id);
        Assert.isNotFound(record == null, "交接记录不存在");
        Assert.isConflict(!Objects.equals(record.getShiftOverUser(), currentUserId), "您没有撤销权限");
        Assert.isConflict(!GridHandoverStatusEnum.WAIT_HANDOVER.equals(record.getStatus()), "当前记录不为待接班无法撤销");
        record.setStatus(GridHandoverStatusEnum.CANCEL);
        return this.updateById(record);
    }

    @Override
    public Pagination<GridHandoverRecordVO> getHandoverRecord(GridHandoverRecordQueryParam param) {
        Period period = param.getPeriod();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (Objects.nonNull(period)) {
            startTime = period.getBegin();
            endTime = period.getEnd();
        }
        Set<Long> userIds = new HashSet<>();
        if (StringUtils.isNotBlank(param.getKeyword())) {
            var users = userClient.listUsers(param.getKeyword());
            if (CollectionUtils.isNotEmpty(users)) {
                userIds.addAll(users.stream().map(UserDTO::getId).filter(Objects::nonNull).collect(Collectors.toSet()));
            }
        }
        if (Boolean.TRUE.equals(param.getPermission())) {
            userIds.add(SecurityContextHolder.getPrincipal().getId());
        }

        boolean isTakeOver = false; //自己已接班数据
        boolean isShiftOver = false; //自己已交班数据
        GridHandoverStatusEnum queryStatus = param.getStatus();
        if (Objects.nonNull(queryStatus)) {
            switch (queryStatus) {
                case HAND_OVERED:
                    isShiftOver = true;
                    break;
                case TAKE_OVERED:
                    queryStatus = GridHandoverStatusEnum.HAND_OVERED;
                    isTakeOver = true;
                    break;
                case HAND_TAKE:
                    queryStatus = GridHandoverStatusEnum.HAND_OVERED;
                    break;
                case WAIT_HANDOVER:
                case CANCEL:
                    break;
            }
        }

        Set<Long> finalUserIds = userIds;
        var queryWrapper = Wrappers.<GridHandoverRecord>lambdaQuery()
                .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), GridHandoverRecord::getShiftOverTime,
                         startTime, endTime)
                .eq(Objects.nonNull(queryStatus), GridHandoverRecord::getStatus, queryStatus)
                .like(StringUtils.isNotBlank(param.getKeyword()), GridHandoverRecord::getRecordName, param.getKeyword())
                .eq(param.getShiftOverUser() != null, GridHandoverRecord::getShiftOverUser, param.getShiftOverUser())
                .eq(param.getTakeOverUser() != null, GridHandoverRecord::getTakeOverUser, param.getTakeOverUser());
        queryWrapper.and(CollectionUtils.isNotEmpty(finalUserIds), wrapper ->
                                 wrapper.or().in(CollectionUtils.isNotEmpty(finalUserIds), GridHandoverRecord::getShiftOverUser, finalUserIds)
                                         .or().in(CollectionUtils.isNotEmpty(finalUserIds), GridHandoverRecord::getTakeOverUser, finalUserIds)
                        )
                .eq(isTakeOver, GridHandoverRecord::getTakeOverUser, SecurityContextHolder.getPrincipal().getId())
                .eq(isShiftOver, GridHandoverRecord::getShiftOverUser, SecurityContextHolder.getPrincipal().getId())
                .orderByDesc(GridHandoverRecord::getCreateTime)
                .orderByDesc(GridHandoverStatusEnum.HAND_OVERED.equals(queryStatus), GridHandoverRecord::getTakeOverTime)
                .orderByDesc(GridHandoverStatusEnum.WAIT_HANDOVER.equals(queryStatus), GridHandoverRecord::getShiftOverTime);
        var pageResult = this.page(PagingUtils.convert(param), queryWrapper);
        if (CollectionUtils.isEmpty(pageResult.getRecords())) {
            return Pagination.empty();
        }
        var queryUserIds = pageResult.getRecords().stream()
                .flatMap(record -> List.of(record.getShiftOverUser(), record.getTakeOverUser()).stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> userNameMap;
        if (CollectionUtils.isNotEmpty(queryUserIds)) {
            var query = new UserQuery();
            query.setUserIds(queryUserIds);
            List<UserDTO> users = userClient.postList(query);
            userNameMap = MapUtils.listToMap(users, UserDTO::getId, UserDTO::getName);
        } else {
            userNameMap = new HashMap<>();
        }

        Map<Long, String> areaNameMap = MapUtils.listToMap(areaService.getAllAreaFullName(), AreaVO::getId, AreaVO::getName);
        List<GridHandoverRecordVO> records = pageResult.getRecords().stream()
                .map(record -> buildHandoverRecordVO(record, areaNameMap, userNameMap))
                .toList();
        return PagingUtils.convert(pageResult, records);
    }

    @Override
    public GridHandoverRecordDetailVO getHandoverRecordDetail(Long id) {
        GridHandoverRecord record = this.getById(id);
        Assert.isNotFound(record == null, "交接记录不存在");
        Long currentUserId = SecurityContextHolder.getPrincipal().getId();
        Assert.isConflict(!Objects.equals(record.getShiftOverUser(), currentUserId)
                          && !Objects.equals(record.getTakeOverUser(), currentUserId),
                          "您没有查看权限");

        Map<Long, String> areaNameMap = MapUtils.listToMap(areaService.getAllAreaFullName(), AreaVO::getId, AreaVO::getName);
        var userQuery = new UserQuery();
        userQuery.setUserIds(Set.of(record.getShiftOverUser(), record.getTakeOverUser()));
        var users = userClient.postList(userQuery);
        Map<Long, String> userNameMap = MapUtils.listToMap(users, UserDTO::getId, UserDTO::getName);

        GridHandoverRecordDetailVO vo = new GridHandoverRecordDetailVO();
        vo.setId(record.getId());
        vo.setRecordName(record.getRecordName());
        List<Long> gridAreaIds = parseGridAreaIds(record.getGridAreaIds());
        vo.setGridAreaId(CollectionUtils.isEmpty(gridAreaIds) ? null : gridAreaIds.get(0));
        vo.setGridAreaIds(gridAreaIds);
        vo.setGridAreaName(joinGridAreaNames(gridAreaIds, areaNameMap));
        vo.setShiftOverUserId(record.getShiftOverUser());
        vo.setShiftOverUserName(userNameMap.get(record.getShiftOverUser()));
        vo.setShiftOverTime(record.getShiftOverTime());
        vo.setTakeOverUserId(record.getTakeOverUser());
        vo.setTakeOverUserName(userNameMap.get(record.getTakeOverUser()));
        vo.setTakeOverTime(record.getTakeOverTime());
        vo.setStatus(resolveDisplayStatus(record));
        vo.setRemarks(record.getRemarks());
        vo.setShiftSignFileId(record.getShiftSignFileId());
        vo.setTakeSignFileId(record.getTakeSignFileId());
        vo.setExtend(buildHandoverExtendVO(id));
        return vo;
    }

    private GridHandoverRecordVO buildHandoverRecordVO(GridHandoverRecord record,
                                                       Map<Long, String> areaNameMap,
                                                       Map<Long, String> userNameMap
                                                      ) {
        var vo = new GridHandoverRecordVO();
        vo.setId(record.getId());
        vo.setRecordName(record.getRecordName());
        List<Long> gridAreaIds = parseGridAreaIds(record.getGridAreaIds());
        vo.setGridAreaId(record.getGridAreaIds());
        vo.setGridAreaIds(gridAreaIds);
        vo.setGridAreaName(joinGridAreaNames(gridAreaIds, areaNameMap));
        vo.setShiftOverUserId(record.getShiftOverUser());
        vo.setShiftOverUserName(userNameMap.get(record.getShiftOverUser()));
        vo.setShiftOverTime(record.getShiftOverTime());
        vo.setTakeOverUserId(record.getTakeOverUser());
        vo.setTakeOverUserName(userNameMap.get(record.getTakeOverUser()));
        vo.setTakeOverTime(record.getTakeOverTime());
        vo.setStatus(resolveDisplayStatus(record));
        vo.setRemarks(record.getRemarks());
        vo.setShiftSignFileId(record.getShiftSignFileId());
        vo.setTakeSignFileId(record.getTakeSignFileId());
        vo.setRevoke(Objects.equals(record.getShiftOverUser(), SecurityContextHolder.getPrincipal().getId()));
        return vo;
    }

    private GridHandoverStatusEnum resolveDisplayStatus(GridHandoverRecord record) {
        if (record == null || !GridHandoverStatusEnum.HAND_OVERED.equals(record.getStatus())) {
            return record == null ? null : record.getStatus();
        }
        Long currentUserId = SecurityContextHolder.getPrincipal().getId();
        if (Objects.equals(currentUserId, record.getTakeOverUser())) {
            return GridHandoverStatusEnum.TAKE_OVERED;
        }
        return GridHandoverStatusEnum.HAND_OVERED;
    }

    private String joinGridAreaNames(List<Long> gridAreaIds, Map<Long, String> areaNameMap) {
        if (CollectionUtils.isEmpty(gridAreaIds)) {
            return "";
        }
        return gridAreaIds.stream()
                .distinct()
                .map(areaId -> getGridAreaName(areaNameMap, areaId))
                .collect(Collectors.joining(","));
    }

    private String joinGridAreaIds(List<Long> gridAreaIds) {
        return gridAreaIds.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private List<Long> parseGridAreaIds(String gridAreaIds) {
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

    private void sendShiftOverMessage(GridHandoverRecord record, Map<Long, String> areaNameMap) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", SecurityContextHolder.getPrincipal().getName());
        paramMap.put("taskName", getRecordName(record, areaNameMap));
        List<MessageNotifyTarget> notifyTargets = new ArrayList<>();
        MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
        notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
        notifyTarget.setIds(Collections.singleton(record.getTakeOverUser()));
        notifyTargets.add(notifyTarget);
        MessageSceneSendRequest message = MessageSceneSendRequest.builder()
                .tenant(SessionContextHolder.getRequester().getTenant())
                .msgSceneCode(SHIFT_OVER_MESSAGE)
                .businessId(String.valueOf(record.getId()))
                .tmpParams(paramMap)
                .messageNotifyTargets(notifyTargets)
                .build();
        try {
            MessageSendResponse messageSendResponse = messageSendClient.sceneSend(message);
            log.info("网格交接消息发送结果,{}", JsonUtils.serialize(messageSendResponse));
        } catch (Exception e) {
            log.error("网格交接消息发送失败", e);
        }
    }

    private void sendTakeOverMessage(GridHandoverRecord record, Map<Long, String> areaNameMap, LocalDateTime takeOverTime) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", SecurityContextHolder.getPrincipal().getName());
        paramMap.put("takeTime", DateUtils.format(takeOverTime, COMPACT_DATETIME_FORMAT));
        paramMap.put("taskName", getRecordName(record, areaNameMap));
        List<MessageNotifyTarget> notifyTargets = new ArrayList<>();
        MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
        notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
        notifyTarget.setIds(Collections.singleton(record.getShiftOverUser()));
        notifyTargets.add(notifyTarget);
        MessageSceneSendRequest message = MessageSceneSendRequest.builder()
                .tenant(SessionContextHolder.getRequester().getTenant())
                .msgSceneCode(TAKE_OVER_MESSAGE)
                .businessId(String.valueOf(record.getId()))
                .tmpParams(paramMap)
                .messageNotifyTargets(notifyTargets)
                .build();
        try {
            MessageSendResponse messageSendResponse = messageSendClient.sceneSend(message);
            log.info("网格接班消息发送结果,{}", JsonUtils.serialize(messageSendResponse));
        } catch (Exception e) {
            log.error("网格接班消息发送失败", e);
        }
    }

    private String getRecordName(GridHandoverRecord record, Map<Long, String> areaNameMap) {
        String handoverTime = DateUtils.format(record.getShiftOverTime(), PURE_DATE_FORMAT);
        String gridAreaNames = joinGridAreaNames(parseGridAreaIds(record.getGridAreaIds()), areaNameMap);
        if (StringUtils.isBlank(gridAreaNames)) {
            return handoverTime + "网格交接";
        }
        return handoverTime + gridAreaNames + "网格交接";
    }

    private List<Long> parseManagerIds(String gridManagerIds) {
        if (StringUtils.isBlank(gridManagerIds)) {
            return List.of();
        }
        List<Long> result = new ArrayList<>();
        for (String id : gridManagerIds.split(",")) {
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

    private String joinManagerIds(Set<Long> managerIds) {
        if (CollectionUtils.isEmpty(managerIds)) {
            return null;
        }
        return managerIds.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private Set<Long> replaceManagerId(List<Long> managerIds, Long oldUserId, Long newUserId) {
        Set<Long> replaced = new LinkedHashSet<>();
        for (Long managerId : managerIds) {
            if (Objects.equals(managerId, oldUserId)) {
                replaced.add(newUserId);
            } else {
                replaced.add(managerId);
            }
        }
        return replaced;
    }

    private void saveHandoverExtend(Long handoverId, GridHandoverRecordExtendParam extendParam) {
        if (handoverId == null || Objects.isNull(extendParam)) {
            return;
        }
        List<FileRefCreateParam> fileRefParams = new ArrayList<>();
        GridHandoverRecordExtend extend = new GridHandoverRecordExtend();
        extend.setId(IdWorker.getId());
        extend.setHandoverId(handoverId);
        extend.setAttInfo1(extendParam.getAttInfo1());
        extend.setAttInfo2(extendParam.getAttInfo2());
        extend.setAttInfo3(extendParam.getAttInfo3());
        extend.setAttInfo4(extendParam.getAttInfo4());
        extend.setFile1(bindExtendFileKey(extendParam.getFile1(), fileRefParams));
        extend.setFile2(bindExtendFileKey(extendParam.getFile2(), fileRefParams));
        extend.setFile3(bindExtendFileKey(extendParam.getFile3(), fileRefParams));
        extend.setFile4(bindExtendFileKey(extendParam.getFile4(), fileRefParams));
        gridHandoverRecordExtendService.save(extend);
        if (CollectionUtils.isNotEmpty(fileRefParams)) {
            fileRefClient.refInduceBatch(fileRefParams);
        }
    }

    private Map<Long, String> getAreaNameMap(List<Long> gridAreaIds) {
        if (CollectionUtils.isEmpty(gridAreaIds)) {
            return new HashMap<>();
        }
        Map<Long, String> areaNameMap = MapUtils.listToMap(areaService.getAllAreaFullName(), AreaVO::getId, AreaVO::getName);
        return gridAreaIds.stream()
                .distinct()
                .collect(Collectors.toMap(areaId -> areaId, areaId -> getGridAreaName(areaNameMap, areaId)));
    }

    private String getGridAreaName(Map<Long, String> areaNameMap, Long gridAreaId) {
        if (MapUtils.isEmpty(areaNameMap)) {
            return String.valueOf(gridAreaId);
        }
        return areaNameMap.getOrDefault(gridAreaId, String.valueOf(gridAreaId));
    }

    private Long bindExtendFileKey(List<Long> fileIds, List<FileRefCreateParam> fileRefParams) {
        if (CollectionUtils.isEmpty(fileIds)) {
            return null;
        }
        Long businessKey = IdWorker.getId();
        FileRefCreateParam fileParam = new FileRefCreateParam();
        fileParam.setFileIdList(fileIds.stream().distinct().collect(Collectors.toList()));
        fileParam.setBusinessKey(businessKey);
        fileParam.setSceneCode(GRID_HANDOVER_SCENE);
        fileRefParams.add(fileParam);
        return businessKey;
    }

    private GridHandoverRecordExtendVO buildHandoverExtendVO(Long handoverId) {
        GridHandoverRecordExtend extend = gridHandoverRecordExtendService.lambdaQuery()
                .eq(GridHandoverRecordExtend::getHandoverId, handoverId)
                .one();
        if (extend == null) {
            return null;
        }
        List<Long> businessKeys = new ArrayList<>();
        addBusinessKey(businessKeys, extend.getFile1());
        addBusinessKey(businessKeys, extend.getFile2());
        addBusinessKey(businessKeys, extend.getFile3());
        addBusinessKey(businessKeys, extend.getFile4());
        Map<Long, List<FileVO>> fileMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(businessKeys)) {
            fileMap = fileClient.getByBusinessKeys(BusinessKeysParam.builder().businessKeys(businessKeys).build());
        }
        GridHandoverRecordExtendVO vo = new GridHandoverRecordExtendVO();
        vo.setAttInfo1(extend.getAttInfo1());
        vo.setAttInfo2(extend.getAttInfo2());
        vo.setAttInfo3(extend.getAttInfo3());
        vo.setAttInfo4(extend.getAttInfo4());
        vo.setFile1(toFileVOList(extend.getFile1(), fileMap));
        vo.setFile2(toFileVOList(extend.getFile2(), fileMap));
        vo.setFile3(toFileVOList(extend.getFile3(), fileMap));
        vo.setFile4(toFileVOList(extend.getFile4(), fileMap));
        return vo;
    }

    private void addBusinessKey(List<Long> businessKeys, Long businessKey) {
        if (businessKey != null) {
            businessKeys.add(businessKey);
        }
    }

    private List<FileVO> toFileVOList(Long businessKey, Map<Long, List<FileVO>> fileMap) {
        if (businessKey == null || MapUtils.isEmpty(fileMap)) {
            return new ArrayList<>();
        }
        return fileMap.getOrDefault(businessKey, new ArrayList<>());
    }
}
