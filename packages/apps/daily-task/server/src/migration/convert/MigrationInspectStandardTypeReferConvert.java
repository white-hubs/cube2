package com.guhecloud.cube.biz.daily.task.migration.convert;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardTargetTypeRelationService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardTargetTypeRelationPO;
import com.guhecloud.cube.biz.daily.task.enums.TaskTargetTypeEnum;
import com.guhecloud.cube.biz.daily.task.migration.convert.base.MigrationBaseConvert;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectStandardTypeReferMapper;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectStandardTypeReferPO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * 巡检标准和对象类型关联转换
 */
@Order(6)
@Component
@RequiredArgsConstructor
public class MigrationInspectStandardTypeReferConvert extends MigrationBaseConvert<MigrationInspectStandardTypeReferPO, DailyStandardTargetTypeRelationPO> {
    private final MigrationInspectStandardTypeReferMapper mapper;
    private final DailyStandardTargetTypeRelationService service;

    /**
     * 转换
     *
     * @return 新数据
     */
    @Override
    public Pagination<DailyStandardTargetTypeRelationPO> getAndConvertDataPage(Long current) {
        var page = mapper.selectPage(new Page<>(current, 100), Wrappers.<MigrationInspectStandardTypeReferPO>lambdaQuery()
                                             .eq(MigrationInspectStandardTypeReferPO::getDeleted, false)
                                             .and(i -> i.isNull(MigrationInspectStandardTypeReferPO::getBusinessType).or()
                                                     .eq(MigrationInspectStandardTypeReferPO::getBusinessType, "INSPECTION").or()
                                                     .eq(MigrationInspectStandardTypeReferPO::getBusinessType, ""))
                                    );
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        var list = page.getRecords().stream().map(item -> {
            var po = DailyStandardTargetTypeRelationPO.builder()
                    .id(item.getId())
                    .standardId(item.getStandardId())
                    .type(TaskTargetTypeEnum.valueOf(item.getTargetCategory()))
                    .targetTypeId(item.getTargetType())
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
    public IService<DailyStandardTargetTypeRelationPO> getService() {
        return service;
    }
}
