package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskTargetRelationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectTaskTargetItemQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectTargetInfoQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.TaskTargetRelationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.TaskTargetRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectGeneratedTaskTargetVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 任务对象关联关系
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskTargetRelationServiceImpl extends ServiceImpl<TaskTargetRelationMapper, TaskTargetRelationPO> implements TaskTargetRelationService {

    private final TaskTargetRelationMapper taskTargetRelationMapper;

    private final InspectTargetInfoQuery targetInfoQuery;

    private final InspectTaskTargetItemQueryService inspectTaskTargetItemQueryService;

    /**
     * 获取任务的对象信息
     *
     * @param taskId
     *
     * @return
     */
    @Override
    public List<List<InspectTargetInfoDTO>> getTargetInfoByTaskId(Long taskId) {
        List<List<InspectTargetInfoDTO>> resultList = new ArrayList<>();
        List<TaskTargetRelationPO> planRelationPOList = taskTargetRelationMapper.selectList(Wrappers.<TaskTargetRelationPO>lambdaQuery()
                                                                                                    .eq(TaskTargetRelationPO::getTaskId, taskId)
                                                                                           );

        Map<String, List<TaskTargetRelationPO>> map = Optional.ofNullable(planRelationPOList)
                .orElse(Collections.emptyList())
                .stream().collect(Collectors.groupingBy(p -> p.getTargetCategory().name()));

        map.forEach((type, typeList) -> {
            Map<Long, Long> targetNatureMap = new HashMap<>();
            Set<Long> targetIdSet = new HashSet<>();
            typeList.forEach(r -> {
                targetIdSet.add(r.getTargetId());
                targetNatureMap.put(r.getTargetId(), r.getTargetParentId());
            });
            List<InspectTargetInfoDTO> targetInfoDTOList = targetInfoQuery.getTargetInfo(new ArrayList<>(targetIdSet), type);
            targetInfoDTOList.forEach(vo -> {
                vo.setTargetParentId(targetNatureMap.get(vo.getTargetId()));
                vo.setStandardDTOList(inspectTaskTargetItemQueryService.getObjStandardList(taskId, vo.getTargetId()));
            });
            resultList.add(targetInfoDTOList);
        });

        return resultList;
    }

    /**
     * @Author wql
     * @Description 获取任务关联的对象
     * @Date 9:34 2023/4/21
     **/
    @Override
    public List<TaskTargetRelationPO> getObjInfo(Long taskId) {
        return taskTargetRelationMapper.selectOrderBy(taskId);
    }

    @Override
    public List<InspectGeneratedTaskTargetVO> listGeneratedTargets(String targetCategory) {
        Assert.isParameterError(StringUtils.isBlank(targetCategory), "巡检对象类型不能为空!");
        InspectTargetCategoryEnum category = InspectTargetCategoryEnum.getTargetCategoryEnum(targetCategory);
        Assert.isParameterError(category == null, "巡检对象类型不合法!");

        List<TaskTargetRelationPO> relations = this.lambdaQuery()
                .eq(TaskTargetRelationPO::getTargetCategory, category)
                .isNotNull(TaskTargetRelationPO::getTargetId)
                .isNotNull(TaskTargetRelationPO::getLatAndLon)
                .ne(TaskTargetRelationPO::getLatAndLon, "")
                .select(TaskTargetRelationPO::getTargetId,
                        TaskTargetRelationPO::getTargetName,
                        TaskTargetRelationPO::getTargetCode,
                        TaskTargetRelationPO::getTargetCategory,
                        TaskTargetRelationPO::getLatAndLon,
                        TaskTargetRelationPO::getLocation)
                .orderByDesc(TaskTargetRelationPO::getCreateTime)
                .list();

        Map<Long, TaskTargetRelationPO> targetMap = new LinkedHashMap<>();
        Optional.ofNullable(relations).orElse(Collections.emptyList())
                .forEach(relation -> targetMap.putIfAbsent(relation.getTargetId(), relation));

        return targetMap.values()
                .stream()
                .map(this::buildGeneratedTargetVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InspectGeneratedTaskTargetVO> listLatestAbnormalTargets(String targetCategory) {
        if (StringUtils.isNotBlank(targetCategory)) {
            Assert.isParameterError(InspectTargetCategoryEnum.getTargetCategoryEnum(targetCategory) == null, "巡检对象类型不合法!");
        }
        List<InspectGeneratedTaskTargetVO> targets = taskTargetRelationMapper.selectLatestAbnormalTargets(targetCategory);
        Optional.ofNullable(targets).orElse(Collections.emptyList())
                .forEach(target -> target.setTypeName(getTargetTypeName(target.getType())));
        return Optional.ofNullable(targets).orElse(Collections.emptyList());
    }

    private InspectGeneratedTaskTargetVO buildGeneratedTargetVO(TaskTargetRelationPO relation) {
        InspectTargetCategoryEnum category = relation.getTargetCategory();
        return InspectGeneratedTaskTargetVO.builder()
                .id(relation.getTargetId())
                .name(relation.getTargetName())
                .code(relation.getTargetCode())
                .type(category == null ? null : category.name())
                .typeName(getTargetTypeName(category == null ? null : category.name()))
                .latAndLon(relation.getLatAndLon())
                .location(relation.getLocation())
                .build();
    }

    private String getTargetTypeName(String targetCategory) {
        InspectTargetCategoryEnum category = InspectTargetCategoryEnum.getTargetCategoryEnum(targetCategory);
        return category == null ? null : category.getDescription();
    }
}
