package com.guhecloud.cube.biz.daily.task.migration.convert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemResultRelationService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemResultRelationPO;
import com.guhecloud.cube.biz.daily.task.migration.convert.base.MigrationBaseConvert;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectItemMapper;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectItemPO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 巡检项转换
 * 注意需要保证这个优先于'巡检标准项关联转换器'执行,
 * 即MigrationInspectItemConvert的@Order中的序号要小于MigrationInspectStandardConvert的@Order中的序号
 */
@Order(4)
@Component
@RequiredArgsConstructor
public class MigrationInspectItemConvert extends MigrationBaseConvert<MigrationInspectItemPO, DailyItemPO> {
    private final MigrationInspectItemMapper mapper;
    private final DailyItemService service;
    private final DailyItemResultRelationService dailyItemResultRelationService;

    /**
     * 转换
     *
     * @return 新数据
     */
    @Override
    public Pagination<DailyItemPO> getAndConvertDataPage(Long current) {
        var page = mapper.selectPage(new Page<>(current, 100), Wrappers.<MigrationInspectItemPO>lambdaQuery()
                                             .eq(MigrationInspectItemPO::getDeleted, false)
                                             .and(i -> i.isNull(MigrationInspectItemPO::getBusinessType).or()
                                                     .eq(MigrationInspectItemPO::getBusinessType, "INSPECTION").or().eq(MigrationInspectItemPO::getBusinessType, ""))
                                    );
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        var refList = new ArrayList<DailyItemResultRelationPO>();
        var list = page.getRecords().stream().map(item -> {
            refList.add(DailyItemResultRelationPO.builder().id(IdWorker.getId()).itemId(item.getId()).resultId(item.getResultId())
                                .defaultResultItem(item.getResultItemValue()).mustRemarkResultItems(item.getRemarkResult())
                                .mustTakePhotoResultItems(item.getImageResult()).build());
            var po = DailyItemPO.builder()
                    .id(item.getId())
                    .name(item.getAlias())
                    .content(item.getName())
                    .tag(item.getItemTag())
                    .targetTypes(item.getReferType())
                    .dailyType("DAILY_INSPECTION")
                    .build();
            this.setBasicTenant(item, po);
            return po;
        }).collect(Collectors.toList());
        dailyItemResultRelationService.saveBatch(refList, 100);
        return PagingUtils.convert(page, list);
    }

    /**
     * 获取服务类
     *
     * @return 服务类
     */
    @Override
    public IService<DailyItemPO> getService() {
        return service;
    }
}
