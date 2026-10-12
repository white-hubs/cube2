package com.guhecloud.cube.biz.daily.task.migration.convert;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyTaskCategoryService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskCategoryPO;
import com.guhecloud.cube.biz.daily.task.migration.convert.base.MigrationBaseConvert;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectTypeMapper;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectTypePO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * 巡检类型转换
 */
@Order(2)
@Component
@RequiredArgsConstructor
public class MigrationInspectTypeConvert extends MigrationBaseConvert<MigrationInspectTypePO, DailyTaskCategoryPO> {
    private final MigrationInspectTypeMapper mapper;
    private final DailyTaskCategoryService service;

    /**
     * 转换
     *
     * @return 新数据
     */
    @Override
    public Pagination<DailyTaskCategoryPO> getAndConvertDataPage(Long current) {
        var page = mapper.selectPage(new Page<>(current, 100), Wrappers.<MigrationInspectTypePO>lambdaQuery()
                                             .eq(MigrationInspectTypePO::getDeleted, false)
                                             .and(i -> i.isNull(MigrationInspectTypePO::getBusinessType).or()
                                                     .eq(MigrationInspectTypePO::getBusinessType, "INSPECTION").or().eq(MigrationInspectTypePO::getBusinessType, ""))
                                    );
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        var list = page.getRecords().stream().map(item -> {
            var po = DailyTaskCategoryPO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .dailyType("DAILY_INSPECTION")
                    .build();
            this.setBasicTenant(item, po);
            return po;
        }).collect(Collectors.toList());

        return PagingUtils.convert(page, list);
    }

    /**
     * 获取服务类
     *
     * @return 服务类
     */
    @Override
    public IService<DailyTaskCategoryPO> getService() {
        return service;
    }
}
