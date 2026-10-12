package com.guhecloud.cube.biz.daily.task.configuration.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultLabelDTO;
import com.guhecloud.cube.biz.daily.task.configuration.dao.DailyStandardMapper;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyStandardByTypesParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyStandardInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyStandardPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyStandardTargetRelationParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyStandardUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyStandardUpdateStatusParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.ItemParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.TargetTypeRelationParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyStandardByTypeResponse;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyStandardDetailResponse;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyStandardPageResponse;
import com.guhecloud.cube.biz.daily.task.configuration.response.KeepTargetResponse;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemResultRelationService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyResultService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardItemRelationService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardTargetTypeRelationService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyTaskCategoryService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemResultRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyResultPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardItemResultRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardTargetTypeRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskCategoryPO;
import com.guhecloud.cube.biz.daily.task.enums.TaskTargetTypeEnum;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 执行标准
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyStandardServiceImpl extends ServiceImpl<DailyStandardMapper, DailyStandardPO> implements DailyStandardService {

    private final DailyStandardTargetTypeRelationService dailyStandardTargetTypeRelationService;
    private final DailyStandardItemRelationService dailyStandardItemRelationService;
    private final DailyItemService dailyItemService;
    private final DailyItemResultRelationService dailyItemResultRelationService;
    private final DailyResultService dailyResultService;
    private final DailyTaskCategoryService dailyTaskCategoryService;

    /**
     * 新增执行标准
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertDailyStandard(DailyStandardInsertParam param) {
        var dailyStandardPO = new DailyStandardPO();
        dailyStandardPO.setId(IdWorker.getId());
        dailyStandardPO.setName(param.getName());
        dailyStandardPO.setDescription(param.getDescription());
        dailyStandardPO.setCategoryIds(param.getCategoryIds());
        dailyStandardPO.setEnable(param.getEnable());
        dailyStandardPO.setDailyType(param.getDailyType());
        this.save(dailyStandardPO);

        Long standardId = dailyStandardPO.getId();

        // 保存关联对象
        saveTargetTypeRelations(param.getTargetTypeRelations(), standardId);
        Assert.isUnsupported(CollectionUtils.isEmpty(param.getItems()), "标准项不能为空");
        var newItems = param.getItems().stream().filter(item -> item.getItemId() == null).toList();

        saveNewItemResult(newItems);

        saveStandardItemResult(param.getItems(), standardId);
        return standardId;
    }

    /**
     * 删除执行标准
     *
     * @param id 主键ID
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDailyStandard(Long id) {
        var dailyStandardPO = this.getById(id);
        Assert.isNotFound(dailyStandardPO, "未查询到相关信息");
        return this.removeById(id);
    }

    /**
     * 修改执行标准
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDailyStandard(DailyStandardUpdateParam param) {
        var dailyStandardPO = this.getById(param.getId());
        Assert.isNotFound(dailyStandardPO, "未查询到相关信息");
        if (param.getName() != null) {
            dailyStandardPO.setName(param.getName());
        }
        if (param.getDescription() != null) {
            dailyStandardPO.setDescription(param.getDescription());
        }
        if (param.getCategoryIds() != null) {
            dailyStandardPO.setCategoryIds(param.getCategoryIds());
        }
        if (param.getEnable() != null) {
            dailyStandardPO.setEnable(param.getEnable());
        }
        if (param.getDailyType() != null) {
            dailyStandardPO.setDailyType(param.getDailyType());
        }
        this.updateById(dailyStandardPO);

        Long standardId = param.getId();

        // 删除旧的关联对象
        dailyStandardTargetTypeRelationService.remove(
                Wrappers.<DailyStandardTargetTypeRelationPO>lambdaQuery()
                        .eq(DailyStandardTargetTypeRelationPO::getStandardId, standardId)
                                                     );

        // 保存新的关联对象
        saveTargetTypeRelations(param.getTargetTypeRelations(), standardId);

        // 删除旧的标准项关联
        dailyStandardItemRelationService.remove(
                Wrappers.<DailyStandardItemResultRelationPO>lambdaQuery()
                        .eq(DailyStandardItemResultRelationPO::getStandardId, standardId)
                                               );

        // 处理新增的标准项
        var newItems = param.getItems().stream().filter(itemParam -> itemParam.getItemId() == null).toList();

        saveNewItemResult(newItems);

        saveStandardItemResult(param.getItems(), standardId);

        return true;
    }

    /**
     * 查询执行标准分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<DailyStandardPageResponse> queryDailyStandardPage(DailyStandardPageParam param) {
        // 如果提供了关联对象类型查询条件，先查询关联表获取符合条件的标准ID列表
        List<Long> standardIds = null;
        if (param.getTargetType() != null || param.getTargetTypeId() != null) {
            var targetTypeRelationQuery = dailyStandardTargetTypeRelationService.lambdaQuery();
            if (param.getTargetType() != null) {
                targetTypeRelationQuery.eq(DailyStandardTargetTypeRelationPO::getType, param.getTargetType());
            }
            if (param.getTargetTypeId() != null) {
                targetTypeRelationQuery.eq(DailyStandardTargetTypeRelationPO::getTargetTypeId, param.getTargetTypeId());
            }
            var targetTypeRelations = targetTypeRelationQuery.list();
            if (targetTypeRelations.isEmpty()) {
                // 如果没有匹配的关联关系，返回空分页
                return new Pagination<>();
            }
            standardIds = targetTypeRelations.stream()
                    .map(DailyStandardTargetTypeRelationPO::getStandardId)
                    .distinct()
                    .collect(Collectors.toList());
        }

        // 构建主表查询条件
        var queryWrapper = Wrappers.<DailyStandardPO>lambdaQuery()
                .like(StringUtils.isNoneBlank(param.getName()), DailyStandardPO::getName, param.getName())
                .eq(param.getEnable() != null, DailyStandardPO::getEnable, param.getEnable())
                .like(StringUtils.isNotBlank(param.getCategoryId()), DailyStandardPO::getCategoryIds, param.getCategoryId())
                .eq(StringUtils.isNoneBlank(param.getDailyType()), DailyStandardPO::getDailyType, param.getDailyType());

        // 如果有关联对象类型查询条件，添加标准ID过滤
        if (standardIds != null && !standardIds.isEmpty()) {
            queryWrapper.in(DailyStandardPO::getId, standardIds);
        }
        queryWrapper.orderByDesc(DailyStandardPO::getCreateTime);
        var page = this.page(PagingUtils.convert(param), queryWrapper);
        var pagination = PagingUtils.convert(page, DailyStandardPageResponse.class);
        if (pagination.getRecords().isEmpty()) {
            return pagination;
        }
        buildExtendInfo(pagination, page);

        return pagination;
    }

    private void buildExtendInfo(Pagination<DailyStandardPageResponse> pagination, Page<DailyStandardPO> page) {
        //查询标准关联对象
        var pageStandardIds = ListUtils.convertItem(pagination.getRecords(), DailyStandardPageResponse::getId);
        var targetTypeRelations = dailyStandardTargetTypeRelationService.lambdaQuery()
                .in(DailyStandardTargetTypeRelationPO::getStandardId, pageStandardIds).list();
        var targetTypeRelationMap = targetTypeRelations.stream()
                .collect(Collectors.groupingBy(DailyStandardTargetTypeRelationPO::getStandardId));
        // 查询分类名称信息
        if (!pagination.getRecords().isEmpty()) {
            var categoryIdSet = new HashSet<Long>();
            page.getRecords().forEach(po -> {
                if (StringUtils.isNotBlank(po.getCategoryIds())) {
                    Arrays.stream(po.getCategoryIds().split(","))
                            .filter(StringUtils::isNotBlank)
                            .map(String::trim)
                            .forEach(idStr -> {
                                try {
                                    categoryIdSet.add(Long.parseLong(idStr));
                                } catch (NumberFormatException e) {
                                    log.warn("无法解析分类ID: {}", idStr);
                                }
                            });
                }
            });

            var categoryMap = new HashMap<Long, String>();
            if (!categoryIdSet.isEmpty()) {
                var categoryList = dailyTaskCategoryService.lambdaQuery()
                        .in(DailyTaskCategoryPO::getId, categoryIdSet)
                        .list();
                Optional.ofNullable(categoryList).orElse(new ArrayList<>())
                        .forEach(category -> categoryMap.put(category.getId(), category.getName()));
            }

            for (int i = 0; i < pagination.getRecords().size() && i < page.getRecords().size(); i++) {
                var response = pagination.getRecords().get(i);
                var po = page.getRecords().get(i);
                if (response == null) {
                    break;
                }
                // 填充分类名称（多个分类名称用逗号分隔）
                if (po != null && StringUtils.isNotBlank(po.getCategoryIds())) {
                    var categoryNames = Arrays.stream(po.getCategoryIds().split(","))
                            .filter(StringUtils::isNotBlank)
                            .map(String::trim)
                            .map(idStr -> {
                                try {
                                    Long id = Long.parseLong(idStr);
                                    return categoryMap.getOrDefault(id, "");
                                } catch (NumberFormatException e) {
                                    return "";
                                }
                            })
                            .filter(StringUtils::isNotBlank)
                            .collect(Collectors.joining(","));
                    response.setCategoryNames(categoryNames);
                }
                // 填充任务类型
                if (po != null) {
                    response.setDailyType(po.getDailyType());
                }

                //填充关联对象
                var targetRelations = targetTypeRelationMap.getOrDefault(response.getId(), Collections.emptyList());
                var typeSet = targetRelations.stream().map(DailyStandardTargetTypeRelationPO::getType).collect(Collectors.toSet());
                response.setTargetTypeRelationStr(typeSet.stream().map(TaskTargetTypeEnum::getDescription)
                                                          .collect(Collectors.joining(",")));
            }
        }
    }

    /**
     * 根据ID查询执行标准详情
     *
     * @param id 主键ID
     *
     * @return 执行标准详情
     */
    @Override
    public DailyStandardDetailResponse getDailyStandardById(Long id) {
        var dailyStandardPO = this.getById(id);
        Assert.isNotFound(dailyStandardPO, "未查询到相关信息");

        var response = new DailyStandardDetailResponse();
        response.setId(dailyStandardPO.getId());
        response.setName(dailyStandardPO.getName());
        response.setDescription(dailyStandardPO.getDescription());
        response.setCategoryIds(dailyStandardPO.getCategoryIds());
        response.setEnable(dailyStandardPO.getEnable());
        response.setDailyType(dailyStandardPO.getDailyType());
        // 查询分类名称（多个分类名称用逗号分隔）
        if (StringUtils.isNotBlank(dailyStandardPO.getCategoryIds())) {
            var categoryIdList = Arrays.stream(dailyStandardPO.getCategoryIds().split(","))
                    .filter(StringUtils::isNotBlank)
                    .map(String::trim)
                    .toList();
            var categoryNames = new ArrayList<String>();
            for (String idStr : categoryIdList) {
                try {
                    Long categoryId = Long.parseLong(idStr);
                    var category = dailyTaskCategoryService.getById(categoryId);
                    if (category != null) {
                        categoryNames.add(category.getName());
                    }
                } catch (NumberFormatException e) {
                    log.warn("无法解析分类ID: {}", idStr);
                }
            }
            response.setCategoryNames(String.join(",", categoryNames));
        }

        // 查询关联对象列表
        var targetTypeRelations = dailyStandardTargetTypeRelationService.lambdaQuery()
                .eq(DailyStandardTargetTypeRelationPO::getStandardId, id)
                .list();
        if (CollectionUtils.isNotEmpty(targetTypeRelations)) {
            var targetTypeRelationList = targetTypeRelations.stream()
                    .map(relation -> {
                        var relationParam = new TargetTypeRelationParam();
                        relationParam.setType(relation.getType());
                        relationParam.setTargetTypeId(relation.getTargetTypeId());
                        return relationParam;
                    })
                    .collect(Collectors.toList());
            response.setTargetTypeRelations(targetTypeRelationList);
        }

        // 查询标准项ID列表和详细信息
        var itemRelations = dailyStandardItemRelationService.lambdaQuery()
                .eq(DailyStandardItemResultRelationPO::getStandardId, id)
                .list();
        if (CollectionUtils.isNotEmpty(itemRelations)) {
            var itemIds = itemRelations.stream()
                    .map(DailyStandardItemResultRelationPO::getItemId)
                    .collect(Collectors.toList());
            var items = dailyItemService.lambdaQuery()
                    .in(DailyItemPO::getId, itemIds)
                    .list();
            var itemMap = MapUtils.listToMap(items, DailyItemPO::getId, item -> item);
            var resultIds = itemRelations.stream()
                    .map(DailyStandardItemResultRelationPO::getResultId)
                    .filter(Objects::nonNull)
                    .toList();
            //查询结果信息列表
            var results = dailyResultService.lambdaQuery()
                    .in(DailyResultPO::getId, resultIds)
                    .list();
            var resultMap = MapUtils.listToMap(results, DailyResultPO::getId, result -> result);
            var standardItemInfos = itemRelations.stream().map(itemRelation -> {
                var itemInfo = new DailyStandardDetailResponse.ItemInfo();
                itemInfo.setItemId(itemRelation.getItemId());
                var item = itemMap.get(itemRelation.getItemId());
                var result = resultMap.get(itemRelation.getResultId());
                itemInfo.setItemName(item != null ? item.getName() : null);
                itemInfo.setItemContent(item != null ? item.getContent() : null);
                itemInfo.setOperationGuide(item != null ? item.getOperationGuide() : null);
                itemInfo.setTag(item != null ? item.getTag() : null);
                itemInfo.setResultId(itemRelation.getResultId());
                itemInfo.setResultName(result != null ? result.getName() : null);
                var labelList = result != null && StringUtils.isNotBlank(result.getResultLabelJson())
                                ? JsonUtils.deserializeToList(result.getResultLabelJson(), DailyResultLabelDTO.class)
                                : new ArrayList<DailyResultLabelDTO>();
                itemInfo.setResultLabelList(labelList);
                itemInfo.setDefaultResultItem(itemRelation.getDefaultResultItem());
                itemInfo.setMustTakePhotoResultItems(itemRelation.getMustTakePhotoResultItems());
                itemInfo.setMustRemarkResultItems(itemRelation.getMustRemarkResultItems());
                return itemInfo;
            }).toList();
            response.setItems(standardItemInfos);
        }

        return response;
    }

    /**
     * 修改执行标准状态
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDailyStandardStatus(DailyStandardUpdateStatusParam param) {
        var dailyStandardPO = this.getById(param.getId());
        Assert.isNotFound(dailyStandardPO, "未查询到相关信息");

        dailyStandardPO.setEnable(param.getEnable());
        return this.updateById(dailyStandardPO);
    }

    /**
     * 根据类型ID列表获取标准数据
     *
     * @param param 包含类型ID列表的查询参数对象
     *
     * @return 按类型ID分组的标准数据映射，key为类型ID，value为该类型下的标准数据列表
     */
    @Override
    public Map<String, List<DailyStandardByTypeResponse>> getStandardByTypeIds(DailyStandardByTypesParam param) {
        // 查询类型与标准的关联关系
        if (CollectionUtils.isEmpty(param.getTypeIds())) {
            return Collections.emptyMap();
        }
        var relationList = dailyStandardTargetTypeRelationService.lambdaQuery()
                .eq(param.getType() != null, DailyStandardTargetTypeRelationPO::getType, param.getType())
                .in(DailyStandardTargetTypeRelationPO::getTargetTypeId, new HashSet<>(param.getTypeIds()))
                .list();
        if (CollectionUtils.isEmpty(relationList)) {
            return Collections.emptyMap();
        }

        // 提取关联关系中的标准ID集合
        var standardIdSet = relationList.stream().map(DailyStandardTargetTypeRelationPO::getStandardId).collect(Collectors.toSet());

        // 根据标准ID集合查询标准数据列表
        var standardList = this.lambdaQuery()
                .in(DailyStandardPO::getId, standardIdSet)
                .eq(StringUtils.isNotBlank(param.getDailyType()), DailyStandardPO::getDailyType, param.getDailyType())
                .list();
        if (CollectionUtils.isEmpty(standardList)) {
            return Collections.emptyMap();
        }
        // 将标准数据列表转换为以ID为key的映射表
        var standardMap = standardList.stream()
                .collect(Collectors.toMap(DailyStandardPO::getId, s -> s));

        // 构建响应对象并按类型ID进行分组
        return relationList.stream().map(relation -> {
            var standard = standardMap.getOrDefault(relation.getStandardId(), new DailyStandardPO());
            return DailyStandardByTypeResponse
                    .builder().typeId(relation.getTargetTypeId()).id(standard.getId()).name(standard.getName())
                    .description(standard.getDescription())
                    .build();

        }).filter(s -> s.getId() != null).collect(Collectors.groupingBy(DailyStandardByTypeResponse::getTypeId));
    }

    /**
     * 根据对象列表查询哪些对象已关联标准
     *
     * @param param 对象及类型集合
     *
     * @return 对象ID列表
     */
    @Override
    public KeepTargetResponse getTargetIdsByTypeIds(DailyStandardTargetRelationParam param) {
        if (param == null || CollectionUtils.isEmpty(param.getTargets())) {
            return KeepTargetResponse.empty();
        }
        var targets = param.getTargets().stream()
                .filter(target -> target.getId() != null && target.getTypeId() != null)
                .toList();
        if (CollectionUtils.isEmpty(targets)) {
            return KeepTargetResponse.empty();
        }
        var typeIdSet = targets.stream().map(DailyStandardTargetRelationParam.TargetRelationItem::getTypeId).collect(Collectors.toSet());
        var relations = dailyStandardTargetTypeRelationService.lambdaQuery()
                .in(DailyStandardTargetTypeRelationPO::getTargetTypeId, typeIdSet)
                .list();
        if (CollectionUtils.isEmpty(relations)) {
            return new KeepTargetResponse((long) targets.size(), List.of());
        }
        var res = new KeepTargetResponse();
        var relatedTypeIds = relations.stream()
                .map(DailyStandardTargetTypeRelationPO::getTargetTypeId)
                .collect(Collectors.toSet());
        var keep = targets.stream()
                .filter(target -> relatedTypeIds.contains(target.getTypeId()))
                .distinct()
                .toList();
        var keepTargets = keep.stream()
                .map(paramRelation -> new KeepTargetResponse.TargetRelationItem(paramRelation.getId(), paramRelation.getTypeId(), paramRelation.getTagIds()))
                .toList();
        res.setTargets(keepTargets);
        res.setRemoveCount((long) (targets.size() - keep.size()));
        return res;
    }

    @Override
    public List<DailyStandardByTypeResponse> getDailyStandardByTaskTypeId(Long id) {
        var standardList = this.lambdaQuery()
                .like(DailyStandardPO::getCategoryIds, id)
                .eq(DailyStandardPO::getEnable, true)
                .list();
        if (CollectionUtils.isEmpty(standardList)) {
            return new ArrayList<>();
        }
        return standardList.stream().map(standard -> DailyStandardByTypeResponse
                .builder().id(standard.getId()).name(standard.getName())
                .description(standard.getDescription())
                .build()).collect(Collectors.toList());

    }

    /**
     * 将适配对象类型列表转换为逗号分隔的字符串
     *
     * @param targetTypes 适配对象类型列表
     *
     * @return 逗号分隔的字符串，如：AREA,DEVICE,PROPERTY
     */
    private String convertTargetTypesToString(List<String> targetTypes) {
        if (CollectionUtils.isEmpty(targetTypes)) {
            return null;
        }
        return String.join(",", targetTypes);
    }

    private void saveTargetTypeRelations(List<TargetTypeRelationParam> targetTypeRelationParams, Long standardId) {
        if (CollectionUtils.isNotEmpty(targetTypeRelationParams)) {
            var targetTypeRelations = targetTypeRelationParams.stream()
                    .map(relation -> DailyStandardTargetTypeRelationPO.builder()
                            .id(IdWorker.getId())
                            .standardId(standardId)
                            .type(relation.getType())
                            .targetTypeId(relation.getTargetTypeId())
                            .build())
                    .collect(Collectors.toList());
            dailyStandardTargetTypeRelationService.saveBatch(targetTypeRelations);
        }
    }

    private void saveNewItemResult(List<ItemParam> newItems) {
        // 处理新增的标准项
        if (CollectionUtils.isNotEmpty(newItems)) {
            //设置id
            newItems.forEach(item -> item.setItemId(IdWorker.getId()));
            // 保存新增的标准项
            var addItems = newItems.stream()
                    .map(newItem -> {
                        DailyItemPO dailyItemPO = new DailyItemPO();
                        dailyItemPO.setId(newItem.getItemId());
                        dailyItemPO.setName(newItem.getItemName());
                        dailyItemPO.setContent(newItem.getItemContent());
                        dailyItemPO.setOperationGuide(newItem.getOperationGuide());
                        dailyItemPO.setTag(newItem.getTag());
                        // 将多个适配对象类型转换为逗号分隔的字符串
                        dailyItemPO.setTargetTypes(convertTargetTypesToString(newItem.getTargetTypes()));
                        return dailyItemPO;
                    })
                    .collect(Collectors.toList());
            dailyItemService.saveBatch(addItems);
            // 创建标准项与标准结果的关联关系
            var itemResultRelations = new ArrayList<DailyItemResultRelationPO>();
            for (int i = 0; i < newItems.size(); i++) {
                var newItemParam = newItems.get(i);
                var dailyItemPO = addItems.get(i);
                // 如果提供了 resultId，创建关联关系
                if (newItemParam.getResultId() != null) {
                    DailyItemResultRelationPO itemResultRelationPO = DailyItemResultRelationPO.builder()
                            .id(IdWorker.getId())
                            .itemId(dailyItemPO.getId())
                            .resultId(newItemParam.getResultId())
                            .defaultResultItem(newItemParam.getDefaultResultItem())
                            .mustTakePhotoResultItems(newItemParam.getMustTakePhotoResultItems())
                            .mustRemarkResultItems(newItemParam.getMustRemarkResultItems())
                            .build();
                    itemResultRelations.add(itemResultRelationPO);
                }
            }
            if (CollectionUtils.isNotEmpty(itemResultRelations)) {
                dailyItemResultRelationService.saveBatch(itemResultRelations);
            }
        }
    }

    private void saveStandardItemResult(List<ItemParam> items, Long standardId) {
        // 保存新的标准项关联
        if (CollectionUtils.isNotEmpty(items)) {
            var itemRelations = items.stream()
                    .map(itemParam -> DailyStandardItemResultRelationPO.builder()
                            .id(IdWorker.getId())
                            .standardId(standardId)
                            .itemId(itemParam.getItemId())
                            .resultId(itemParam.getResultId())
                            .defaultResultItem(itemParam.getDefaultResultItem())
                            .mustTakePhotoResultItems(itemParam.getMustTakePhotoResultItems())
                            .mustRemarkResultItems(itemParam.getMustRemarkResultItems())
                            .build())
                    .collect(Collectors.toList());
            dailyStandardItemRelationService.saveBatch(itemRelations);
        }
    }

}
