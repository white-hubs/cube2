package com.guhecloud.cube.biz.daily.task.migration.convert;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyCronService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyCronPO;
import com.guhecloud.cube.biz.daily.task.migration.convert.base.MigrationBaseConvert;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectCronMapper;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectCronPO;
import com.guhecloud.cube.biz.daily.task.plan.coverts.DailyCronUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * 巡检周期转换
 */
@Order(1)
@Component
@RequiredArgsConstructor
public class MigrationInspectCronConvert extends MigrationBaseConvert<MigrationInspectCronPO, DailyCronPO> {
    private final MigrationInspectCronMapper mapper;
    private final DailyCronService service;

    /**
     * 转换
     *
     * @return 新数据
     */
    @Override
    public Pagination<DailyCronPO> getAndConvertDataPage(Long current) {
        var page = mapper.selectPage(new Page<>(current, 100), Wrappers.<MigrationInspectCronPO>lambdaQuery()
                                             .eq(MigrationInspectCronPO::getDeleted, false)
                                             .and(i -> i.isNull(MigrationInspectCronPO::getBusinessType).or()
                                                     .eq(MigrationInspectCronPO::getBusinessType, "INSPECTION").or().eq(MigrationInspectCronPO::getBusinessType, ""))
                                    );
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        var newList = page.getRecords().stream().map(item -> {
            var po = DailyCronPO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .cron(StringUtils.isNotBlank(item.getCron()) ? DailyCronUtils.convertQuartzToSpring(item.getCron()) : item.getCron())
                    .description(item.getDescription())
                    .dailyType("DAILY_INSPECTION")
                    .build();
            this.setBasicTenant(item, po);
            return po;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, newList);
    }

    /**
     * 获取服务类
     *
     * @return 服务类
     */
    @Override
    public IService<DailyCronPO> getService() {
        return service;
    }
}
