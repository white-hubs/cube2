package com.guhecloud.cube.biz.daily.task.domain.impl;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailyItemResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyStandardResultDTO;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyResultService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardItemRelationService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardService;
import com.guhecloud.cube.biz.daily.task.domain.DailyStandardResultService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyResultPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardItemResultRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 执行内容(执行 标准、项、结果、结果项)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyStandardResultServiceImpl implements DailyStandardResultService {

    private final DailyStandardService dailyStandardService;
    private final DailyStandardItemRelationService dailyStandardItemRelationService;
    private final DailyItemService dailyItemService;
    private final DailyResultService dailyResultService;

    /**
     * 获取执行标准完整对象集合
     *
     * @param standardIdSet 标准ID集合，用于查询相关联的项和结果数据
     *
     * @return 返回以标准ID为键，DailyStandardResultDTO为值的映射表。每个DTO包含该标准下所有相关的项及其对应的结果信息。
     */
    @Override
    public Map<Long, DailyStandardResultDTO> handlerStandardContent(Set<Long> standardIdSet) {
        if (CollectionUtils.isEmpty(standardIdSet)) {
            return new HashMap<>();
        }
        var standardDTOMap = new HashMap<Long, DailyStandardResultDTO>();

        // 查询标准与项、结果之间的关联关系列表
        var standardResultList = dailyStandardItemRelationService.lambdaQuery()
                .in(DailyStandardItemResultRelationPO::getStandardId, standardIdSet).list();
        if (CollectionUtils.isEmpty(standardResultList)) {
            return standardDTOMap;
        }

        // 提取所有的项ID和结果ID，便于批量查询
        var itemIdSet = new HashSet<Long>();
        var resultIdSet = new HashSet<Long>();
        standardResultList.forEach(s -> {
            itemIdSet.add(s.getItemId());
            resultIdSet.add(s.getResultId());
        });

        /* 获取标准名称映射 */
        var standardList = dailyStandardService.listByIds(standardIdSet);
        var standardIdNameMap = Optional.ofNullable(standardList).orElse(new ArrayList<>()).stream()
                .collect(Collectors.toMap(DailyStandardPO::getId, DailyStandardPO::getName));

        /* 获取项名称映射 */
        var itemList = dailyItemService.listByIds(itemIdSet);
        var itemIdMap = Optional.ofNullable(itemList).orElse(new ArrayList<>()).stream()
                .collect(Collectors.toMap(DailyItemPO::getId, Function.identity()));

        /* 获取结果详细信息映射 */
        var resultList = dailyResultService.listByIds(resultIdSet);
        Map<Long, DailyResultPO> resultMap = Optional.ofNullable(resultList).orElse(new ArrayList<>()).stream()
                .collect(Collectors.toMap(DailyResultPO::getId, r -> r));

        /* 按照标准ID分组并组装完整的标准结果结构 按 standardId 分组 → 按 itemId 分组 → 构建 DTO */
        return standardResultList.stream()
                .collect(Collectors.groupingBy(
                        DailyStandardItemResultRelationPO::getStandardId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                relations -> this.buildStandardDTO(relations, standardIdNameMap, itemIdMap, resultMap)
                                                    )
                                              ))
                .entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 构建标准DTO对象
     *
     * @param relations         标准项结果关系列表
     * @param standardIdNameMap 标准ID到名称的映射
     * @param itemIdMap         项目ID到名称的映射
     * @param resultMap         结果ID到结果PO的映射
     *
     * @return 构建完成的标准结果DTO对象
     */
    private DailyStandardResultDTO buildStandardDTO(List<DailyStandardItemResultRelationPO> relations, Map<Long, String> standardIdNameMap,
                                                    Map<Long, DailyItemPO> itemIdMap, Map<Long, DailyResultPO> resultMap
                                                   ) {

        // 获取标准ID
        Long standardId = relations.getFirst().getStandardId();

        // 按项目ID对关系进行分组
        Map<Long, List<DailyStandardItemResultRelationPO>> itemGroups = relations.stream()
                .collect(Collectors.groupingBy(DailyStandardItemResultRelationPO::getItemId));

        // 构建项目结果DTO列表
        List<DailyItemResultDTO> itemList = itemGroups.entrySet().stream()
                .map(entry -> {
                    Long itemId = entry.getKey();
                    // 构建结果DTO列表
                    List<DailyResultDTO> resultDTOs = entry.getValue().stream()
                            .map(sir -> {
                                DailyResultPO po = resultMap.getOrDefault(sir.getResultId(), new DailyResultPO());
                                return DailyResultDTO.builder().id(sir.getResultId()).name(po.getName())
                                        .defaultResultItem(sir.getDefaultResultItem())
                                        .mustTakePhotoResultItems(sir.getMustTakePhotoResultItems())
                                        .mustRemarkResultItems(sir.getMustRemarkResultItems()).resultLabelJson(po.getResultLabelJson())
                                        .build();
                            })
                            .filter(Objects::nonNull).distinct().collect(Collectors.toList());
                    DailyItemPO itemPO = itemIdMap.getOrDefault(itemId, new DailyItemPO());
                    return DailyItemResultDTO.builder().id(itemId)
                            .name(itemPO.getName())
                            .content(itemPO.getContent())
                            .operationGuide(itemPO.getOperationGuide())
                            .resultList(resultDTOs).build();
                })
                .collect(Collectors.toList());

        // 构建并返回标准结果DTO
        return DailyStandardResultDTO.builder().id(standardId).name(standardIdNameMap.get(standardId)).itemList(itemList).build();
    }

}
