package com.guhecloud.cube.biz.daily.task.migration.convert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemResultRelationService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardItemRelationService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemResultRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardItemResultRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardPO;
import com.guhecloud.cube.biz.daily.task.migration.convert.base.MigrationBaseConvert;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectStandardItemMapper;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectStandardMapper;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectStandardItemPO;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectStandardPO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 巡检标准转换(包含标准关联的项)
 * 注意需要保证这个在'巡检项转换器'后执行,
 * 即MigrationInspectItemConvert的@Order中的序号要小于MigrationInspectStandardConvert的@Order中的序号
 */
@Order(5)
@Component
@RequiredArgsConstructor
public class MigrationInspectStandardConvert extends MigrationBaseConvert<MigrationInspectStandardPO, DailyStandardPO> {
    private final MigrationInspectStandardMapper mapper;
    private final MigrationInspectStandardItemMapper standardItemMapper;
    private final DailyStandardService service;
    private final DailyStandardItemRelationService standardItemService;
    private final DailyItemService itemService;
    private final DailyItemResultRelationService itemResultService;

    /**
     * 转换
     *
     * @return 新数据
     */
    @Override
    public Pagination<DailyStandardPO> getAndConvertDataPage(Long current) {
        var page = mapper.selectPage(new Page<>(current, 100), Wrappers.<MigrationInspectStandardPO>lambdaQuery()
                                             .eq(MigrationInspectStandardPO::getDeleted, false)
                                             .and(i -> i.isNull(MigrationInspectStandardPO::getBusinessType).or()
                                                     .eq(MigrationInspectStandardPO::getBusinessType, "INSPECTION").or()
                                                     .eq(MigrationInspectStandardPO::getBusinessType, ""))
                                    );
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        var standardIdList = new ArrayList<Long>();
        var standardMap = new HashMap<Long, DailyStandardPO>();
        var list = page.getRecords().stream().map(item -> {
            standardIdList.add(item.getId());
            var po = DailyStandardPO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .enable(item.getEnable())
                    .description(item.getRemark())
                    .categoryIds("0")
                    .dailyType("DAILY_INSPECTION")
                    .build();
            this.setBasicTenant(item, po);
            standardMap.put(item.getId(), po);
            return po;
        }).collect(Collectors.toList());
        // 已存在的执行项map
        var itemResultMap = this.getExistsItemResultMap();

        var oldStandardItemList = standardItemMapper.selectList(Wrappers.<MigrationInspectStandardItemPO>lambdaQuery()
                                                                        .in(MigrationInspectStandardItemPO::getStandardId, standardIdList)
                                                               );
        var itemList = new ArrayList<DailyItemPO>();
        var refList = new ArrayList<DailyItemResultRelationPO>();
        var siMap = oldStandardItemList.stream().collect(Collectors.groupingBy(i -> i.getStandardId() + i.getName()));
        var standardItemList = new ArrayList<DailyStandardItemResultRelationPO>();
        siMap.forEach((k, v) -> {
            var item = v.getFirst();
            var standard = standardMap.getOrDefault(item.getStandardId(), new DailyStandardPO());
            var itemId = itemResultMap.get(item.getName() + item.getResultId());
            if (itemId == null) {
                itemId = IdWorker.getId();
                var dailyItem = DailyItemPO.builder().id(itemId).name(item.getName()).content(item.getName())
                        .dailyType("DAILY_INSPECTION")
                        .build();
                dailyItem.setTenant(standard.getTenant());
                dailyItem.setDeleted(false);
                dailyItem.setCreateBy(standard.getCreateBy());
                dailyItem.setUpdateBy(standard.getUpdateBy());
                dailyItem.setCreateTime(standard.getCreateTime());
                dailyItem.setUpdateTime(standard.getUpdateTime());
                itemList.add(dailyItem);

                var itemRef = DailyItemResultRelationPO.builder().itemId(itemId)
                        .resultId(item.getResultId())
                        .mustTakePhotoResultItems(item.getImageResult()).defaultResultItem(item.getResultItemValue())
                        .mustRemarkResultItems(item.getRemarkResult()).build();
                itemRef.setDeleted(false);
                itemRef.setCreateBy(standard.getCreateBy());
                itemRef.setUpdateBy(standard.getUpdateBy());
                itemRef.setCreateTime(standard.getCreateTime());
                itemRef.setUpdateTime(standard.getUpdateTime());
                refList.add(itemRef);
                itemResultMap.put(item.getName() + item.getResultId(), itemId);
            }
            var po = DailyStandardItemResultRelationPO.builder()
                    .id(item.getId())
                    .standardId(item.getStandardId())
                    .itemId(itemId)
                    .defaultResultItem(item.getResultItemValue())
                    .mustRemarkResultItems(item.getRemarkResult())
                    .mustTakePhotoResultItems(item.getImageResult())
                    .resultId(item.getResultId())
                    .build();
            po.setDeleted(false);
            po.setCreateBy(standard.getCreateBy());
            po.setUpdateBy(standard.getUpdateBy());
            po.setCreateTime(standard.getCreateTime());
            po.setUpdateTime(standard.getUpdateTime());
            standardItemList.add(po);
        });
        if (CollectionUtils.isNotEmpty(standardItemList)) {
            standardItemService.saveBatch(standardItemList, 100);
        }
        if (CollectionUtils.isNotEmpty(itemList)) {
            itemService.saveBatch(itemList, 100);
        }
        if (CollectionUtils.isNotEmpty(refList)) {
            itemResultService.saveBatch(refList, 100);
        }
        return PagingUtils.convert(page, list);
    }

    /**
     * 获取服务类
     *
     * @return 服务类
     */
    @Override
    public IService<DailyStandardPO> getService() {
        return service;
    }

    /**
     * 获取已存在的执行项和结果关联关系
     *
     * @return 已存在的执行项和结果关联关系
     */
    private Map<String, Long> getExistsItemResultMap() {
        var map = new HashMap<String, Long>();
        var oldItemList = itemService.lambdaQuery().select(DailyItemPO::getId, DailyItemPO::getName).list();
        // 执行项和结果关联关系
        var oldItemResultList = itemResultService.lambdaQuery()
                .select(DailyItemResultRelationPO::getItemId, DailyItemResultRelationPO::getResultId).list();
        var itemResultMap = Optional.ofNullable(oldItemResultList).orElse(CollectionUtils.emptyList()).stream()
                .collect(Collectors.groupingBy(DailyItemResultRelationPO::getItemId));
        oldItemList.forEach(item -> {
            var itemResultList = itemResultMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(itemResultList)) {
                map.put(item.getName() + itemResultList.getFirst().getResultId(), item.getId());
            }
        });
        return map;
    }
}
