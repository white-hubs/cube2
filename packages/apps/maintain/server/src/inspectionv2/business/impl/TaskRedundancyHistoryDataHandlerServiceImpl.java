package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectTaskTargetItemCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectItemTemplateMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectResultMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.PlanRelationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.TaskTargetRelationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultOptionCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectItemTemplatePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectResultPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskTargetItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.TaskTargetRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectResultVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionItemVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskRedundancyHistoryDataHandlerServiceImpl {
    private final PlanRelationMapper planRelationMapper;
    private final InspectTaskMapper inspectTaskMapper;
    private final TaskTargetRelationMapper taskTargetRelationMapper;
    private final InspectStandardMapper inspectStandardMapper;
    private final InspectStandardItemMapper inspectStandardItemMapper;
    private final InspectItemTemplateMapper inspectItemTemplateMapper;
    private final InspectResultMapper inspectResultMapper;
    private final InspectTaskTargetItemCommandService inspectTaskTargetItemCommandService;

    public void taskDataHandler() {
        List<Integer> tenantList = inspectTaskMapper.getAllTaskTenant();
        if (tenantList == null) {
            return;
        }
        tenantList = new ArrayList<>();
        tenantList.add(58197260);
        for (Integer tenant : tenantList) {
            SessionContextHolder.requestTenant(tenant);
            Map<Long, InspectStandardPO> standardPOMap = this.getInspectStandardMap(null);
            Map<Long, InspectionItemVO> itemVOMap = this.getInspectItemVOMap(null);
            Map<Long, List<InspectResultPO>> resultMap = this.getRsultMap();
            this.handleTaskData(standardPOMap, itemVOMap, resultMap);
        }
    }

    private void handleTaskData(Map<Long, InspectStandardPO> standardPOMap, Map<Long, InspectionItemVO> itemVOMap,
                                Map<Long, List<InspectResultPO>> resultMap
                               ) {

        // 获取需要处理的数据(这里每次获取1000条未处理数据)
        List<TaskTargetRelationPO> taskTargetRelationPOList = taskTargetRelationMapper.selectHandleList();
        if (CollectionUtils.isEmpty(taskTargetRelationPOList)) {
            return;
        }
        Set<Long> planIdSet = new HashSet<>();
        taskTargetRelationPOList.forEach(taskTargetRelationPO -> planIdSet.add(taskTargetRelationPO.getPlanId()));
        // 获取的巡检信息
        List<PlanRelationPO> planRelationPOList = planRelationMapper.selectList(Wrappers.<PlanRelationPO>lambdaQuery()
                                                                                        .in(PlanRelationPO::getPlanId, planIdSet)
                                                                               );
        if (CollectionUtils.isEmpty(planRelationPOList)) {
            return;
        }
        Map<Long, List<PlanRelationPO>> planRelationMap = planRelationPOList.stream()
                .collect(Collectors.groupingBy(PlanRelationPO::getPlanId));

        List<InspectTaskTargetItemPO> insertList = new ArrayList<>();
        // 按任务处理
        taskTargetRelationPOList.stream().collect(Collectors.groupingBy(TaskTargetRelationPO::getTaskId)).forEach((taskId, list) -> {
            // 获取任务在计划中对应的对象
            List<PlanRelationPO> planRelationList = planRelationMap.get(list.get(0).getPlanId());
            // 处理任务的巡检内容
            this.handlerTaskTargetItem(taskId, planRelationList, standardPOMap, itemVOMap, resultMap, insertList);
        });

        inspectTaskTargetItemCommandService.saveBatch(insertList);

    }

    private Map<Long, InspectStandardPO> getInspectStandardMap(Set<Long> idSet) {
        Map<Long, InspectStandardPO> standardPOMap = new HashMap<>();
        List<InspectStandardPO> standardPOList = inspectStandardMapper.selectList(Wrappers.<InspectStandardPO>lambdaQuery()
                                                                                          .in(CollectionUtils.isNotEmpty(idSet), InspectStandardPO::getId, idSet));
        if (CollectionUtils.isNotEmpty(standardPOList)) {
            standardPOList.forEach(po -> standardPOMap.put(po.getId(), po));
        }
        return standardPOMap;
    }

    private Map<Long, InspectionItemVO> getInspectItemVOMap(Set<Long> idSet) {
        Map<Long, InspectionItemVO> itemVOMap = new HashMap<>();
        // 模板中的巡检项
        List<InspectItemTemplatePO> templateItemPOList = inspectItemTemplateMapper.selectList(Wrappers.<InspectItemTemplatePO>lambdaQuery()
                                                                                                      .in(CollectionUtils.isNotEmpty(idSet), InspectItemTemplatePO::getId, idSet));
        if (CollectionUtils.isNotEmpty(templateItemPOList)) {
            templateItemPOList.forEach(item -> itemVOMap.put(item.getId(), InspectionItemVO.builder().id(item.getId()).name(item.getName())
                    .alias(item.getAlias()).resultItemValue(item.getResultItemValue()).imageResult(item.getImageResult())
                    .remarkResult(item.getRemarkResult()).itemTag(item.getItemTag()).resultId(item.getResultId()).build()));
        }
        // 巡检标准中的巡检项
        List<InspectStandardItemPO> standardItemPOList = inspectStandardItemMapper.selectList(Wrappers.<InspectStandardItemPO>lambdaQuery()
                                                                                                      .in(CollectionUtils.isNotEmpty(idSet), InspectStandardItemPO::getId, idSet));
        if (CollectionUtils.isNotEmpty(standardItemPOList)) {
            standardItemPOList.forEach(item -> itemVOMap.put(item.getId(), InspectionItemVO.builder().id(item.getId()).name(item.getName())
                    .alias(item.getAlias()).resultItemValue(item.getResultItemValue()).imageResult(item.getImageResult())
                    .remarkResult(item.getRemarkResult()).itemTag(item.getItemTag()).resultId(item.getResultId()).build()));
        }

        return itemVOMap;
    }

    private Map<Long, List<InspectResultPO>> getRsultMap() {
        Map<Long, List<InspectResultPO>> resultMap = new HashMap<>();
        List<InspectResultPO> resultPOList = inspectResultMapper.selectList(Wrappers.emptyWrapper());
        if (CollectionUtils.isNotEmpty(resultPOList)) {
            return resultPOList.stream().collect(Collectors.groupingBy(InspectResultPO::getId));
        }
        return resultMap;
    }

    /**
     * 组装任务对象的巡检内容
     */
    private void handlerTaskTargetItem(Long taskId, List<PlanRelationPO> planRelationList, Map<Long, InspectStandardPO> standardPOMap,
                                       Map<Long, InspectionItemVO> itemVOMap, Map<Long, List<InspectResultPO>> resultMap,
                                       List<InspectTaskTargetItemPO> resultList
                                      ) {
        if (CollectionUtils.isEmpty(planRelationList)) {
            return;
        }
        planRelationList.forEach(relation -> {
            /* 组装巡检内容 */
            InspectTaskTargetItemPO po = InspectTaskTargetItemPO.builder()
                    .taskId(taskId)
                    .targetId(relation.getTargetId())
                    .standardId(relation.getStandardId())
                    .itemId(relation.getItemId())
                    .build();
            InspectStandardPO standardPO = standardPOMap.get(relation.getStandardId());
            if (standardPO != null) {
                po.setStandardCode(standardPO.getCode());
                po.setStandardName(standardPO.getName());
            }
            InspectionItemVO itemVO = itemVOMap.get(relation.getItemId());
            if (itemVO != null) {
                po.setItemName(itemVO.getName());
                po.setItemAlias(itemVO.getAlias());
                po.setImageResult(itemVO.getImageResult());
                po.setRemarkResult(itemVO.getRemarkResult());
                po.setResultItemValue(itemVO.getResultItemValue());
                po.setItemTag(itemVO.getItemTag());
                po.setResultId(itemVO.getResultId());
                List<InspectResultPO> resultPOList = resultMap.get(po.getResultId());
                if (CollectionUtils.isNotEmpty(resultPOList)) {
                    InspectResultPO result = resultPOList.get(0);
                    InspectResultVO vo = new InspectResultVO();
                    vo.setId(result.getId());
                    vo.setName(result.getName());
                    vo.setCode(result.getCode());
                    vo.setItemNames(result.getItemName());
                    vo.setEnable(result.getEnable());
                    vo.setDeleted(result.getDeleted());
                    vo.setTenant(result.getTenant());
                    vo.setCreateTime(result.getCreateTime());
                    vo.setUpdateTime(result.getUpdateTime());
                    vo.setCreateBy(result.getCreateBy());
                    vo.setUpdateBy(result.getUpdateBy());
                    List<InspectResultOptionCommandModel> items = ObjectUtils.copying(resultPOList, InspectResultOptionCommandModel.class);
                    vo.setOptions(items);
                    po.setResultText(JSON.toJSONString(vo));
                }
            }
            resultList.add(po);
        });
    }

}
