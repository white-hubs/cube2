package com.guhecloud.cube.biz.daily.task.configuration.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyItemResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultLabelDTO;
import com.guhecloud.cube.biz.daily.task.configuration.dao.DailyItemMapper;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyItemResultRelationInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyItemResultRelationPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyItemResultRelationUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyItemResultRelationDetailResponse;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyItemResultRelationPageResponse;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemResultRelationService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyResultService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardItemRelationService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemResultRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyResultPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardItemResultRelationPO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 执行项
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyItemServiceImpl extends ServiceImpl<DailyItemMapper, DailyItemPO> implements DailyItemService {
    private final DailyResultService dailyResultService;
    private final DailyItemResultRelationService dailyItemResultRelationService;
    private final DailyStandardItemRelationService dailyStandardItemRelationService;

    /**
     * 新增执行项的结果关联
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertDailyItemResultRelation(DailyItemResultRelationInsertParam param) {
        // 先保存执行项
        var dailyItemPO = new DailyItemPO();
        dailyItemPO.setId(IdWorker.getId());
        dailyItemPO.setName(param.getItemName());
        dailyItemPO.setContent(param.getItemContent());
        dailyItemPO.setOperationGuide(param.getOperationGuide());
        // 将多个适配对象类型转换为逗号分隔的字符串
        dailyItemPO.setTargetTypes(convertTargetTypesToString(param.getTargetTypes()));
        dailyItemPO.setTag(param.getTag());
        dailyItemPO.setDailyType(param.getDailyType());
        dailyItemPO.setTemp(param.getIsTemp());
        save(dailyItemPO);
        var itemId = dailyItemPO.getId();

        // 保存关系信息
        var dailyItemResultRelationPO = new DailyItemResultRelationPO();
        dailyItemResultRelationPO.setId(IdWorker.getId());
        dailyItemResultRelationPO.setItemId(itemId);
        dailyItemResultRelationPO.setResultId(param.getResultId());
        dailyItemResultRelationPO.setDefaultResultItem(param.getDefaultResultItem());
        dailyItemResultRelationPO.setMustTakePhotoResultItems(param.getMustTakePhotoResultItems());
        dailyItemResultRelationPO.setMustRemarkResultItems(param.getMustRemarkResultItems());
        dailyItemResultRelationService.save(dailyItemResultRelationPO);
        return dailyItemResultRelationPO.getId();
    }

    /**
     * 删除执行项的结果关联
     *
     * @param itemId 主键ID
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDailyItemResultRelation(Long itemId) {
        var item = getById(itemId);
        Assert.isNotFound(item, "未查询到相关信息");
        var itemResult = dailyItemResultRelationService.lambdaQuery().eq(DailyItemResultRelationPO::getItemId, itemId).last("LIMIT 1")
                .one();
        Assert.isNotFound(itemResult, "未查询到相关信息");

        // 检查是否有执行标准引用了这个执行项
        if (itemResult.getItemId() != null) {
            var standardItemRelationExist = dailyStandardItemRelationService.lambdaQuery()
                    .eq(DailyStandardItemResultRelationPO::getItemId, itemResult.getItemId())
                    .exists();
            Assert.isConflict(standardItemRelationExist, "该执行项已被标准项引用，无法删除");
        }
        //删除关系
        dailyItemResultRelationService.removeById(itemResult.getId());
        //删除标准项id
        return this.removeById(itemId);
    }

    /**
     * 修改执行项的结果关联
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDailyItemResultRelation(DailyItemResultRelationUpdateParam param) {
        var dailyItemPO = getById(param.getItemId());
        Assert.isNotFound(dailyItemPO, "未查询到相关信息");
        var dailyItemResultRelationPO = dailyItemResultRelationService.lambdaQuery()
                .eq(DailyItemResultRelationPO::getItemId, param.getItemId())
                .last("LIMIT 1").one();
        Assert.isNotFound(dailyItemResultRelationPO, "未查询到相关信息");

        // 如果传入执行项名称、内容、适配对象类型或任务类型，更新执行项
        if (param.getItemName() != null || param.getItemContent() != null || param.getOperationGuide() != null
            || !CollectionUtils.isEmpty(param.getTargetTypes()) ||
            param.getDailyType() != null) {
            if (dailyItemPO != null) {
                if (param.getItemName() != null) {
                    dailyItemPO.setName(param.getItemName());
                }
                if (param.getItemContent() != null) {
                    dailyItemPO.setContent(param.getItemContent());
                }
                if (param.getOperationGuide() != null) {
                    dailyItemPO.setOperationGuide(param.getOperationGuide());
                }
                if (!CollectionUtils.isEmpty(param.getTargetTypes())) {
                    dailyItemPO.setTargetTypes(convertTargetTypesToString(param.getTargetTypes()));
                }
                if (param.getTag() != null) {
                    dailyItemPO.setTag(param.getTag());
                }
                if (param.getDailyType() != null) {
                    dailyItemPO.setDailyType(param.getDailyType());
                }
                updateById(dailyItemPO);
            }
        }

        if (param.getResultId() != null) {
            dailyItemResultRelationPO.setResultId(param.getResultId());
        }
        if (param.getDefaultResultItem() != null) {
            dailyItemResultRelationPO.setDefaultResultItem(param.getDefaultResultItem());
        }
        if (param.getMustTakePhotoResultItems() != null) {
            dailyItemResultRelationPO.setMustTakePhotoResultItems(param.getMustTakePhotoResultItems());
        }
        if (param.getMustRemarkResultItems() != null) {
            dailyItemResultRelationPO.setMustRemarkResultItems(param.getMustRemarkResultItems());
        }

        return dailyItemResultRelationService.updateById(dailyItemResultRelationPO);
    }

    /**
     * 查询执行项的结果关联分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<DailyItemResultRelationPageResponse> queryDailyItemResultRelationPage(DailyItemResultRelationPageParam param) {
        // 先根据执行项名称、适配对象类型和任务类型过滤执行项，获取符合条件的执行项ID列表
        var queryWrapper = lambdaQuery()
                .like(StringUtils.isNotBlank(param.getItemName()), DailyItemPO::getName, param.getItemName())
                .eq(DailyItemPO::getTemp, false)
                .eq(StringUtils.isNotBlank(param.getDailyType()), DailyItemPO::getDailyType, param.getDailyType());

        // 支持多个适配对象类型查询（只要包含任一类型即匹配）
        if (!CollectionUtils.isEmpty(param.getTargetTypes())) {
            queryWrapper.and(wrapper -> {
                for (String targetType : param.getTargetTypes()) {
                    wrapper.or(w -> w.like(DailyItemPO::getTargetTypes, targetType));
                }
            });
        }
        var itemList = queryWrapper.list();
        var itemIdList = itemList.stream()
                .map(DailyItemPO::getId)
                .collect(Collectors.toList());
        if (itemIdList.isEmpty()) {
            // 如果没有匹配的执行项，返回空分页
            return new Pagination<>();
        }
        var page = dailyItemResultRelationService.lambdaQuery()
                .in(DailyItemResultRelationPO::getItemId, itemIdList)
                .page(PagingUtils.convert(param));
        var pagination = PagingUtils.convert(page, DailyItemResultRelationPageResponse.class);
        if (pagination.getRecords().isEmpty()) {
            return pagination;
        }
        var items = lambdaQuery()
                .in(DailyItemPO::getId, ListUtils.convertItem(pagination.getRecords(), DailyItemResultRelationPageResponse::getItemId))
                .list();
        var itemMap = MapUtils.listToMap(items, DailyItemPO::getId, po -> po);
        // 收集所有的 resultId，批量查询结果项信息
        var resultIdList = page.getRecords().stream()
                .map(DailyItemResultRelationPO::getResultId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        var resultMap = MapUtils.listToMap(
                dailyResultService.lambdaQuery().in(DailyResultPO::getId, resultIdList).list(),
                DailyResultPO::getId,
                po -> po
                                          );
        // 关联查询执行项信息和结果项信息
        for (int i = 0; i < pagination.getRecords().size() && i < page.getRecords().size(); i++) {
            var response = pagination.getRecords().get(i);
            var dailyItemResultRelationPO = page.getRecords().get(i);
            if (response != null && dailyItemResultRelationPO != null) {
                response.setItemId(dailyItemResultRelationPO.getItemId());

                // 关联查询执行项信息
                if (dailyItemResultRelationPO.getItemId() != null) {
                    var dailyItemPO = itemMap.get(dailyItemResultRelationPO.getItemId());
                    if (dailyItemPO != null) {
                        response.setItemName(dailyItemPO.getName());
                        response.setItemContent(dailyItemPO.getContent());
                        response.setOperationGuide(dailyItemPO.getOperationGuide());
                        response.setDailyType(dailyItemPO.getDailyType());
                        var targetTypes = dailyItemPO.getTargetTypes();
                        if (StringUtils.isNotBlank(targetTypes)) {
                            response.setTargetTypes(convertTargetTypesToEnumList(dailyItemPO.getTargetTypes()));
                            var targetTypeStr = Arrays.stream(targetTypes.split(","))
                                    .map(TaskTargetTypeEnum::valueOf)
                                    .map(TaskTargetTypeEnum::getDescription)
                                    .collect(Collectors.joining(","));
                            response.setTargetTypeStr(targetTypeStr);
                        }
                    }
                }

                // 关联查询结果项信息
                if (dailyItemResultRelationPO.getResultId() != null) {
                    var dailyResultPO = resultMap.get(dailyItemResultRelationPO.getResultId());
                    if (dailyResultPO != null) {
                        response.setResultId(dailyResultPO.getId());
                        response.setResultName(dailyResultPO.getName());
                        response.setDefaultResultItem(dailyItemResultRelationPO.getDefaultResultItem());
                        response.setMustTakePhotoResultItems(dailyItemResultRelationPO.getMustTakePhotoResultItems());
                        response.setMustRemarkResultItems(dailyItemResultRelationPO.getMustRemarkResultItems());
                        // 将 JSON 字符串转换为 List<DailyResultLabelDTO>
                        if (StringUtils.isNotBlank(dailyResultPO.getResultLabelJson())) {
                            try {
                                var resultLabelList = JsonUtils.deserialize(
                                        dailyResultPO.getResultLabelJson(),
                                        new TypeReference<List<DailyResultLabelDTO>>() {}
                                                                           );
                                response.setResultLabelList(resultLabelList);
                                response.setResultLabelStr(resultLabelList == null ? "" : resultLabelList.stream()
                                        .map(DailyResultLabelDTO::getLabel).collect(Collectors.joining(",")));
                            } catch (Exception e) {
                                log.warn("反序列化 resultLabelJson 失败: {}", dailyResultPO.getResultLabelJson(), e);
                            }
                        }
                    }
                }
            }
        }
        return pagination;
    }

    @Override
    public DailyItemResultRelationDetailResponse getDailyItemResultRelation(Long itemId) {
        var dailyItemResultRelationPO = dailyItemResultRelationService.lambdaQuery()
                .eq(DailyItemResultRelationPO::getItemId, itemId)
                .last("LIMIT 1")
                .one();

        Assert.isNotFound(dailyItemResultRelationPO, "未查询到相关信息");

        // 查询执行项信息
        DailyItemPO dailyItemPO = null;
        if (dailyItemResultRelationPO.getItemId() != null) {
            dailyItemPO = getById(dailyItemResultRelationPO.getItemId());
            Assert.isNotFound(dailyItemPO, "未查询到执行项信息");
        }

        var response = DailyItemResultRelationDetailResponse.builder()
                .id(dailyItemResultRelationPO.getId())
                .itemId(dailyItemResultRelationPO.getItemId())
                .itemName(dailyItemPO != null ? dailyItemPO.getName() : null)
                .itemContent(dailyItemPO != null ? dailyItemPO.getContent() : null)
                .operationGuide(dailyItemPO != null ? dailyItemPO.getOperationGuide() : null)
                .tag(dailyItemPO != null ? dailyItemPO.getTag() : null)
                .targetTypes(dailyItemPO != null ? convertTargetTypesToEnumList(dailyItemPO.getTargetTypes()) : null)
                .dailyType(dailyItemPO != null ? dailyItemPO.getDailyType() : null)
                .resultId(dailyItemResultRelationPO.getResultId())
                .defaultResultItem(dailyItemResultRelationPO.getDefaultResultItem())
                .mustTakePhotoResultItems(dailyItemResultRelationPO.getMustTakePhotoResultItems())
                .mustRemarkResultItems(dailyItemResultRelationPO.getMustRemarkResultItems())
                .createTime(dailyItemResultRelationPO.getCreateTime())
                .updateTime(dailyItemResultRelationPO.getUpdateTime())
                .build();

        // 查询结果项信息
        if (dailyItemResultRelationPO.getResultId() != null) {
            var dailyResultPO = dailyResultService.getById(dailyItemResultRelationPO.getResultId());
            if (dailyResultPO != null) {
                response.setResultName(dailyResultPO.getName());
                // 将 JSON 字符串转换为 List<DailyResultLabelDTO>
                if (StringUtils.isNotBlank(dailyResultPO.getResultLabelJson())) {
                    try {
                        var resultLabelList = JsonUtils.deserialize(
                                dailyResultPO.getResultLabelJson(),
                                new TypeReference<List<DailyResultLabelDTO>>() {}
                                                                   );
                        response.setResultLabelList(resultLabelList);
                    } catch (Exception e) {
                        log.warn("反序列化 resultLabelJson 失败: {}", dailyResultPO.getResultLabelJson(), e);
                    }
                }
            }
        }

        return response;
    }

    @Override
    public Map<Long, DailyItemResultDTO> getItemInfoList(Set<Long> itemIds) {
        /* 获取项名称映射 */
        var itemPOList = this.listByIds(itemIds);
        if (CollectionUtils.isEmpty(itemPOList)) {
            return new HashMap<>();
        }

        var dailyItemResultRelationPOList = dailyItemResultRelationService.lambdaQuery()
                .in(DailyItemResultRelationPO::getItemId, itemIds)
                .list();
        Map<Long, DailyResultPO> resultMap;
        Map<Long, List<DailyItemResultRelationPO>> itemGroups = new HashMap<>();
        if (CollectionUtils.isNotEmpty(dailyItemResultRelationPOList)) {
            itemGroups = dailyItemResultRelationPOList.stream()
                    .collect(Collectors.groupingBy(DailyItemResultRelationPO::getItemId));
            resultMap = Optional.ofNullable(dailyResultService
                                                    .listByIds(dailyItemResultRelationPOList.stream()
                                                                       .map(DailyItemResultRelationPO::getResultId)
                                                                       .collect(Collectors.toSet()))).orElse(new ArrayList<>()).stream()
                    .collect(Collectors.toMap(DailyResultPO::getId, r -> r));
        } else {
            resultMap = new HashMap<>();
        }
        Map<Long, DailyItemResultDTO> resultDTOMap = new HashMap<>();
        for (DailyItemPO item : itemPOList) {
            // 构建结果DTO列表
            var resultRelationPOS = itemGroups.get(item.getId());
            List<DailyResultDTO> resultDTOs = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(resultRelationPOS)) {
                resultDTOs = resultRelationPOS.stream()
                        .map(sir -> {
                            DailyResultPO po = resultMap.getOrDefault(sir.getResultId(), new DailyResultPO());
                            return DailyResultDTO.builder().id(sir.getResultId()).name(po.getName())
                                    .defaultResultItem(sir.getDefaultResultItem())
                                    .mustTakePhotoResultItems(sir.getMustTakePhotoResultItems())
                                    .mustRemarkResultItems(sir.getMustRemarkResultItems()).resultLabelJson(po.getResultLabelJson())
                                    .build();
                        })
                        .filter(Objects::nonNull).distinct().collect(Collectors.toList());
            }
            resultDTOMap.put(item.getId(), DailyItemResultDTO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .content(item.getContent())
                    .operationGuide(item.getOperationGuide())
                    .resultList(resultDTOs).build());

        }
        return resultDTOMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<DailyItemResultRelationInsertParam> noExistItemList) {
        if (CollectionUtils.isEmpty(noExistItemList)) {
            return;
        }
        List<DailyItemPO> dailyItemList = new ArrayList<>();
        List<DailyItemResultRelationPO> dailyItemResultRelationPOList = new ArrayList<>();
        for (var param : noExistItemList) {
            // 先保存执行项
            var dailyItemPO = new DailyItemPO();
            dailyItemPO.setId(param.getId());
            dailyItemPO.setName(param.getItemName());
            dailyItemPO.setContent(param.getItemContent());
            dailyItemPO.setOperationGuide(param.getOperationGuide());
            // 将多个适配对象类型转换为逗号分隔的字符串
            dailyItemPO.setTargetTypes(convertTargetTypesToString(param.getTargetTypes()));
            dailyItemPO.setTag(param.getTag());
            dailyItemPO.setDailyType(param.getDailyType());
            dailyItemPO.setTemp(param.getIsTemp());
            dailyItemList.add(dailyItemPO);
            // 保存关系信息
            var itemId = dailyItemPO.getId();
            var dailyItemResultRelationPO = new DailyItemResultRelationPO();
            dailyItemResultRelationPO.setId(IdWorker.getId());
            dailyItemResultRelationPO.setItemId(itemId);
            dailyItemResultRelationPO.setResultId(param.getResultId());
            dailyItemResultRelationPO.setDefaultResultItem(param.getDefaultResultItem());
            dailyItemResultRelationPO.setMustTakePhotoResultItems(param.getMustTakePhotoResultItems());
            dailyItemResultRelationPO.setMustRemarkResultItems(param.getMustRemarkResultItems());
            dailyItemResultRelationPOList.add(dailyItemResultRelationPO);
        }
        this.saveBatch(dailyItemList);
        dailyItemResultRelationService.saveBatch(dailyItemResultRelationPOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTempItem(List<Long> taskItemIdList) {
        if (CollectionUtils.isEmpty(taskItemIdList)) {
            return;
        }
        this.remove(new LambdaQueryWrapper<DailyItemPO>().in(DailyItemPO::getId, taskItemIdList).eq(DailyItemPO::getTemp, true));
    }

    /**
     * 将适配对象类型列表转换为逗号分隔的字符串
     *
     * @param targetTypes 适配对象类型列表
     *
     * @return 逗号分隔的字符串，如：AREA,DEVICE,PROPERTY
     */
    private String convertTargetTypesToString(List<TaskTargetTypeEnum> targetTypes) {
        if (CollectionUtils.isEmpty(targetTypes)) {
            return null;
        }
        return targetTypes.stream().map(TaskTargetTypeEnum::name).collect(Collectors.joining(","));
    }

    private List<TaskTargetTypeEnum> convertTargetTypesToEnumList(String targetTypes) {
        if (StringUtils.isBlank(targetTypes)) {
            return null;
        }
        return Arrays.stream(targetTypes.split(","))
                .map(TaskTargetTypeEnum::valueOf)
                .collect(Collectors.toList());
    }
}
