package com.guhecloud.cube.biz.daily.task.migration.convert;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultLabelDTO;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyResultService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyResultPO;
import com.guhecloud.cube.biz.daily.task.migration.convert.base.MigrationBaseConvert;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectResultMapper;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectResultPO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 巡检结果转换
 */
@Order(3)
@Component
@RequiredArgsConstructor
public class MigrationInspectResultConvert extends MigrationBaseConvert<MigrationInspectResultPO, DailyResultPO> {
    private final MigrationInspectResultMapper mapper;
    private final DailyResultService service;

    /**
     * 转换
     *
     * @return 新数据
     */
    @Override
    public Pagination<DailyResultPO> getAndConvertDataPage(Long current) {
        // 先按code分组获取分页信息(不然可能会存在同一个结果的结果项没查完的问题)
        var page = mapper.selectPage(new Page<>(current, 100), Wrappers.<MigrationInspectResultPO>lambdaQuery()
                                             .select(MigrationInspectResultPO::getId, MigrationInspectResultPO::getCode)
                                             .eq(MigrationInspectResultPO::getDeleted, false)
                                             .and(i -> i.isNull(MigrationInspectResultPO::getBusinessType).or()
                                                     .eq(MigrationInspectResultPO::getBusinessType, "INSPECTION").or().eq(MigrationInspectResultPO::getBusinessType, ""))
                                             .groupBy(MigrationInspectResultPO::getCode)
                                    );
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        // 获取列表数据
        var list = mapper.selectList(Wrappers.<MigrationInspectResultPO>lambdaQuery()
                                             .in(MigrationInspectResultPO::getId, page.getRecords().stream()
                                                     .map(MigrationInspectResultPO::getId).collect(Collectors.toSet())));
        // 按code分组
        var grouped = list.stream()
                .collect(Collectors.groupingBy(MigrationInspectResultPO::getCode));
        var resultList = new ArrayList<DailyResultPO>();
        grouped.forEach((code, ls) -> resultList.add(this.convertGroup(ls)));
        return PagingUtils.convert(page, resultList);
    }

    /**
     * 获取服务类
     *
     * @return 服务类
     */
    @Override
    public IService<DailyResultPO> getService() {
        return service;
    }

    /**
     * 将迁移巡检结果组转换为日报结果对象
     *
     * @param group 迁移巡检结果列表，用于构建日报标签数据
     *
     * @return 转换后的日报结果对象，包含序列化的标签信息
     */
    private DailyResultPO convertGroup(List<MigrationInspectResultPO> group) {
        // 获取组内第一个巡检结果作为基础信息来源
        MigrationInspectResultPO first = group.getFirst();

        // 将组内每个巡检结果转换为日报标签DTO并序列化为JSON字符串列表
        List<DailyResultLabelDTO> jsonLabels = group.stream()
                .map(item -> DailyResultLabelDTO.builder()
                        .label(item.getItemName())
                        .value(Long.parseLong(item.getItemValue()))
                        .errorFlag(item.getErrorFlag())
                        .build())
                .toList();

        // 构建日报结果对象，包含基础信息和序列化的标签JSON数据
        var po = DailyResultPO.builder()
                .id(first.getId())
                .dailyType("DAILY_INSPECTION")
                .enable(true)
                .name(first.getName())
                .resultLabelJson(JsonUtils.serialize(jsonLabels))
                .build();
        this.setBasicTenant(first, po);
        return po;

    }

}
