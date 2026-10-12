package com.guhecloud.cube.biz.work.center.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.work.center.server.dao.HandoverRecordMapper;
import com.guhecloud.cube.biz.work.center.server.dao.WorkTaskMapper;
import com.guhecloud.cube.biz.work.center.server.entity.HandoverRecord;
import com.guhecloud.cube.biz.work.center.server.entity.WorkBusinessUrl;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTagReferBusinessType;
import com.guhecloud.cube.biz.work.center.server.enumeration.WorkTaskStatusEnum;
import com.guhecloud.cube.biz.work.center.server.param.WorkHomePageBatchParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkHomePageParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTaskQueryParam;
import com.guhecloud.cube.biz.work.center.server.service.WorkBusinessUrlService;
import com.guhecloud.cube.biz.work.center.server.service.WorkCenterService;
import com.guhecloud.cube.biz.work.center.server.service.WorkTagService;
import com.guhecloud.cube.biz.work.center.server.vo.WorkHomePageVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskBatchGroupTagVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskGroupTagVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskPageVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/4 11:59
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkCenterServiceImpl implements WorkCenterService {
    private final WorkTaskMapper workTaskMapper;
    private final HandoverRecordMapper handoverRecordMapper;
    private final WorkBusinessUrlService workBusinessUrlService;
    private final WorkTagService workTagService;

    @Override
    public WorkHomePageVO getHomePageNum(WorkHomePageParam param) {
        var totalTaskGroupByTag = workTaskMapper.selectWorkTaskNumTotal(null, param);
        if (CollectionUtils.isEmpty(totalTaskGroupByTag)) {
            return new WorkHomePageVO();
        }
        var doneTaskGroupTagVOS = workTaskMapper.selectWorkTaskNumTotal(WorkTaskStatusEnum.DONE.name(), param);
        Map<String, Long> doneTaskGroupTagMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(doneTaskGroupTagVOS)) {
            doneTaskGroupTagMap = doneTaskGroupTagVOS.stream()
                    .collect(Collectors.toMap(WorkTaskGroupTagVO::getType, WorkTaskGroupTagVO::getNumber));
        }

        var totalTaskGroupByTagMap = totalTaskGroupByTag.stream()
                .collect(Collectors.toMap(WorkTaskGroupTagVO::getType, WorkTaskGroupTagVO::getNumber));
        var totalNum = 0L;
        var doneNum = 0L;
        var normalNum = 0L;
        var doneNormalNum = 0L;
        var alarmNum = 0L;
        var doneAlarmNum = 0L;
        var approveNum = 0L;
        var doneApproveNum = 0L;
        for (var entry : totalTaskGroupByTagMap.entrySet()) {
            //总数
            var total = entry.getValue();
            //已完成数量
            var doneNumByType = doneTaskGroupTagMap.getOrDefault(entry.getKey(), 0L);
            totalNum += total;
            doneNum += doneNumByType;
            if ("ALARM_TASK".equals(entry.getKey())) {
                alarmNum += total;
                doneAlarmNum += doneNumByType;

            } else if ("APPROVE_TASK".equals(entry.getKey())) {
                approveNum += total;
                doneApproveNum += doneNumByType;
            } else {
                normalNum += total;
                doneNormalNum += doneNumByType;
            }

        }
        var result = new WorkHomePageVO();
        result.setTotalNum(totalNum);
        result.setDoneTotalNum(doneNum);
        result.setNormalNum(normalNum);
        result.setDoneNormalNum(doneNormalNum);
        result.setAlarmNum(alarmNum);
        result.setDoneAlarmNum(doneAlarmNum);
        result.setApproveNum(approveNum);
        result.setDoneApproveNum(doneApproveNum);
        return result;

    }

    @Override
    public Map<Long, WorkHomePageVO> getHomePageNumBatch(WorkHomePageBatchParam param) {
        if (param == null || MapUtils.isEmpty(param.getUserIdMap())) {
            return Map.of();
        }
        Map<Long, WorkHomePageVO> result = new HashMap<>();
        Map<Long, List<Long>> validUserIdMap = new HashMap<>();
        param.getUserIdMap().forEach((groupId, userIds) -> {
            if (groupId != null) {
                result.put(groupId, new WorkHomePageVO());
                if (CollectionUtils.isNotEmpty(userIds)) {
                    List<Long> validUserIds = userIds.stream().filter(Objects::nonNull).distinct().toList();
                    if (CollectionUtils.isNotEmpty(validUserIds)) {
                        validUserIdMap.put(groupId, validUserIds);
                    }
                }
            }
        });
        if (MapUtils.isEmpty(validUserIdMap)) {
            return result;
        }
        param.setUserIdMap(validUserIdMap);
        List<WorkTaskBatchGroupTagVO> statistics = workTaskMapper.selectWorkTaskNumBatch(param);
        if (CollectionUtils.isEmpty(statistics)) {
            return result;
        }
        for (WorkTaskBatchGroupTagVO statistic : statistics) {
            if (statistic == null || statistic.getGroupId() == null) {
                continue;
            }
            WorkHomePageVO vo = result.computeIfAbsent(statistic.getGroupId(), key -> new WorkHomePageVO());
            long total = Optional.ofNullable(statistic.getNumber()).orElse(0L);
            long done = Optional.ofNullable(statistic.getDoneNumber()).orElse(0L);
            vo.setTotalNum(vo.getTotalNum() + total);
            vo.setDoneTotalNum(vo.getDoneTotalNum() + done);
            if ("ALARM_TASK".equals(statistic.getType())) {
                vo.setAlarmNum(vo.getAlarmNum() + total);
                vo.setDoneAlarmNum(vo.getDoneAlarmNum() + done);
            } else if ("APPROVE_TASK".equals(statistic.getType())) {
                vo.setApproveNum(vo.getApproveNum() + total);
                vo.setDoneApproveNum(vo.getDoneApproveNum() + done);
            } else {
                vo.setNormalNum(vo.getNormalNum() + total);
                vo.setDoneNormalNum(vo.getDoneNormalNum() + done);
            }
        }
        return result;
    }

    @Override
    public Pagination<WorkTaskPageVO> taskQuery(WorkTaskQueryParam param) {
        if (StringUtils.isNotEmpty(param.getWorkTagCodes())) {
            Set<String> tagCodes = Arrays.stream(param.getWorkTagCodes().split(",")).collect(Collectors.toSet());
            List<WorkTagReferBusinessType> businessTypes = workTagService.listWorkTagReferBusinessTypesByCodes(tagCodes);
            if (CollectionUtils.isEmpty(businessTypes)) {
                return new Pagination<>();
            }
            param.setBusinessTypes(businessTypes.stream().map(WorkTagReferBusinessType::getBusinessType).collect(Collectors.toSet()));
        }
        var workTaskPageVOPage = workTaskMapper.selectTask(PagingUtils.convert(param), param);
        this.extend(workTaskPageVOPage.getRecords());
        return PagingUtils.convert(workTaskPageVOPage);
    }

    @Override
    public List<Long> getHandTaskUser(Set<Long> userIds) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginOfDay = DateUtils.beginOfDay(now);
        List<HandoverRecord> takeUserList = handoverRecordMapper.selectList(Wrappers.<HandoverRecord>lambdaQuery()
                                                                                    .select(HandoverRecord::getTakeOverUser)
                                                                                    .between(HandoverRecord::getShiftOverTime, beginOfDay, now)
                                                                                    .in(HandoverRecord::getTakeOverUser, userIds));
        return Optional.ofNullable(takeUserList).orElse(new ArrayList<>())
                .stream().map(HandoverRecord::getTakeOverUser).collect(Collectors.toList());
    }

    private void extend(List<WorkTaskPageVO> pageVOS) {
        var businessTypes = pageVOS.stream().map(WorkTaskPageVO::getBusinessType).collect(Collectors.toSet());
        var workBusinessUrlMap = this.workBusinessUrlService
                .listByBusinessTypes(businessTypes, SessionContextHolder.getRequester().getTerminal()).stream()
                .collect(Collectors.toMap(WorkBusinessUrl::getBusinessType, workBusinessUrl -> workBusinessUrl, (o, n) -> n));
        pageVOS.forEach(workTaskPageVO -> {
            var workBusinessUrl = workBusinessUrlMap.get(workTaskPageVO.getBusinessType());
            if (Objects.nonNull(workBusinessUrl)) {
                workTaskPageVO.setUrl(workBusinessUrl.getUrl());
            }
        });
    }
}
