package com.guhecloud.cube.biz.mdata.server.grid.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.mdata.server.grid.dao.GridAreaExtendMapper;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridAreaExtend;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridRelationMember;
import com.guhecloud.cube.biz.mdata.server.grid.enumeration.GridRelationTypeEnum;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaTaskPageParam;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridAreaTaskService;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridRelationMemberService;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridCodeNameStringStringVO;
import com.guhecloud.cube.biz.work.center.client.WorkTaskClient;
import com.guhecloud.cube.biz.work.center.client.param.WorkHomePageParam;
import com.guhecloud.cube.biz.work.center.client.param.WorkTaskQueryParam;
import com.guhecloud.cube.biz.work.center.client.vo.WorkHomePageVO;
import com.guhecloud.cube.biz.work.center.client.vo.WorkTaskPageVO;
import com.guhecloud.cube.srv.gen.maintain.client.FaultClient;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GridAreaTaskServiceImpl implements GridAreaTaskService {
    private final GridAreaExtendMapper gridAreaExtendMapper;
    private final GridRelationMemberService gridRelationMemberService;
    private final WorkTaskClient workTaskClient;
    private final GridDictionaryUtilServiceImpl gridDictionaryUtilService;
    private final FaultClient faultClient;
    private static final String TASK_CODE = "TASK";

    /**
     * 获取网格区域业务类型下拉列表
     *
     * @return 网格区域业务类型下拉列表
     */
    @Override
    public List<GridCodeNameStringStringVO> getGridAreaBusinessSelect() {
        return gridDictionaryUtilService.getDictionaryPairs(TASK_CODE);
    }

    /**
     * 获取网格区域任务列表
     *
     * @param param 网格区域id
     *
     * @return 网格区域任务分页列表
     */
    @Override
    public Pagination<WorkTaskPageVO> getGridAreaTaskPage(GridAreaTaskPageParam param) {
        var taskUserIds = this.getGridTaskUserIds(param.getGridAreaId(), param.getUserId(), param.getUserIds());
        var workTaskQueryParam = new WorkTaskQueryParam();
        workTaskQueryParam.setPageNumber(param.getPageNumber());
        workTaskQueryParam.setPageSize(param.getPageSize());
        workTaskQueryParam.setUserIdList(taskUserIds);
        workTaskQueryParam.setKeyword(param.getKeyword());
        workTaskQueryParam.setTaskStatus(param.getTaskStatus());
        workTaskQueryParam.setStatus(param.getStatus());
        workTaskQueryParam.setBusinessTypes(this.getBusinessTypes(param.getBusinessTypes()));
        workTaskQueryParam.setPeriod(param.getPeriod());
        return workTaskClient.taskQueryPost(workTaskQueryParam);
    }

    private Set<String> getBusinessTypes(String businessTypes) {
        if (StringUtils.isNotBlank(businessTypes)) {
            return new HashSet<>(StringUtils.split(businessTypes));
        }
        return gridDictionaryUtilService.getDictionaryBusinessTypes(TASK_CODE);
    }

    /**
     * 获取网格区域任务数量统计
     *
     * @param param 网格区域id
     *
     * @return 网格区域任务数量统计
     */
    @Override
    public WorkHomePageVO getGridAreaTaskNum(GridAreaTaskPageParam param) {
        var taskUserIds = this.getGridTaskUserIds(param.getGridAreaId(), param.getUserId(), param.getUserIds());
        var workHomePageParam = new WorkHomePageParam();
        workHomePageParam.setUserIdList(taskUserIds);
        workHomePageParam.setPeriod(param.getPeriod());
        workHomePageParam.setBusinessTypes(gridDictionaryUtilService.getDictionaryBusinessTypes(TASK_CODE));
        WorkHomePageVO vo = workTaskClient.taskNumPost(workHomePageParam);
        if (vo == null) {
            vo = new WorkHomePageVO();
        }
        fillFaultOrderNum(vo, param.getGridAreaId());
        return vo;
    }

    private void fillFaultOrderNum(WorkHomePageVO vo, Long gridAreaId) {
        if (vo == null || gridAreaId == null) {
            return;
        }
        try {
            var countMap = faultClient.countByAreaIds(List.of(gridAreaId));
            if (MapUtils.isNotEmpty(countMap)) {
                vo.setFaultOrderNum(countMap.getOrDefault(gridAreaId, 0L));
            }
        } catch (Exception e) {
            log.warn("获取网格故障工单数失败: gridAreaId={}", gridAreaId, e);
        }
    }

    /**
     * 获取网格区域任务用户id
     * 如果网格和人员都为空，则返回当前登陆用户id
     * 如果有用户id 则返回用户id
     * 否则返回网格的管理长和网格员用户id
     *
     * @param gridAreaId 网格
     * @param userId     用户id
     * @param userIds
     *
     * @return 人员id
     */
    private List<Long> getGridTaskUserIds(Long gridAreaId, Long userId, List<Long> userIds) {
        var resultSet = new HashSet<Long>();
        if (gridAreaId == null && userId == null && CollectionUtils.isEmpty(userIds)) {
            //            resultSet.add(SecurityContextHolder.getPrincipal().getId());
            return new ArrayList<>(resultSet);
        }
        if (userId != null) {
            resultSet.add(userId);
            return new ArrayList<>(resultSet);
        }
        if (CollectionUtils.isNotEmpty(userIds)) {
            resultSet.addAll(userIds);
            return new ArrayList<>(resultSet);
        }
        var gridAreaExtendList = gridAreaExtendMapper.selectList(Wrappers.<GridAreaExtend>lambdaQuery()
                                                                         .select(GridAreaExtend::getGridManagerIds)
                                                                         .eq(GridAreaExtend::getGridAreaId, gridAreaId));
        Optional.ofNullable(gridAreaExtendList)
                .orElse(CollectionUtils.emptyList())
                .forEach(gridAreaExtend -> resultSet.addAll(parseUserIds(gridAreaExtend.getGridManagerIds())));

        var gridMembers = gridRelationMemberService.list(Wrappers.<GridRelationMember>lambdaQuery()
                                                                 .select(GridRelationMember::getRelationId)
                                                                 .eq(GridRelationMember::getGridAreaId, gridAreaId)
                                                                 .eq(GridRelationMember::getRelationType,
                                                                     GridRelationTypeEnum.USER));
        Optional.ofNullable(gridMembers)
                .orElse(CollectionUtils.emptyList())
                .stream()
                .map(GridRelationMember::getRelationId)
                .filter(memberId -> memberId != null)
                .forEach(resultSet::add);
        return new ArrayList<>(resultSet);
    }

    private Set<Long> parseUserIds(String userIds) {
        var resultSet = new HashSet<Long>();
        if (StringUtils.isBlank(userIds)) {
            return resultSet;
        }
        for (String userId : userIds.split(",")) {
            if (StringUtils.isNotBlank(userId)) {
                resultSet.add(Long.valueOf(userId.trim()));
            }
        }
        return resultSet;
    }
}
