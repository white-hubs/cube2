package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.SafetyInspectRecordItemMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.InspectionItemStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketBaseVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.PileUpMapVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.InspectionItemTagEnum;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyInspectRecordItemPO;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyInspectRecordItemService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyInspectRecordItemServiceImpl extends ServiceImpl<SafetyInspectRecordItemMapper, SafetyInspectRecordItemPO> implements SafetyInspectRecordItemService {

    /**
     * 巡检车辆违规统计
     *
     * @param query 条件
     *
     * @return 违规统计数据
     */
    @Override
    public Pagination<StackedBarChartVO> inspectVehicleViolationStat(InspectionItemStatisticsQuery query) {
        List<SafetyInspectRecordItemPO> itemPOList = this.getVehicleViolationItem(query);
        if (CollectionUtils.isEmpty(itemPOList)) {
            return Pagination.empty();
        }
        List<MarketBaseVO> xData = new ArrayList<>();
        Map<Integer, List<SafetyInspectRecordItemPO>> tenantMap = new TreeMap<>(itemPOList.stream()
                                                                                        .collect(Collectors.groupingBy(SafetyInspectRecordItemPO::getMarketTenant)));
        tenantMap.forEach((k, v) -> xData.add(MarketBaseVO.builder().marketName(v.get(BigDecimal.ZERO.intValue()).getMarketName())
                                                      .marketTenant(k).build()));

        Map<String, List<SafetyInspectRecordItemPO>> itemMap = itemPOList.stream()
                .collect(Collectors.groupingBy(SafetyInspectRecordItemPO::getItemTag));
        List<PileUpMapVO> yData = new ArrayList<>();
        InspectionItemTagEnum[] tagEnumList;
        if (CollectionUtils.isEmpty(query.getTagList())) {
            tagEnumList = InspectionItemTagEnum.values();
        } else {
            tagEnumList = query.getTagList().stream()
                    .filter(StringUtils::isNotBlank)
                    .map(InspectionItemTagEnum::valueOf)
                    .toArray(InspectionItemTagEnum[]::new);
        }
        for (InspectionItemTagEnum value : tagEnumList) {
            List<Integer> valueList = new ArrayList<>();
            Map<Integer, List<SafetyInspectRecordItemPO>> marketMap = new TreeMap<>();
            List<SafetyInspectRecordItemPO> tagList = itemMap.get(value.name());
            if (!CollectionUtils.isEmpty(tagList)) {
                marketMap = new TreeMap<>(tagList.stream().collect(Collectors.groupingBy(SafetyInspectRecordItemPO::getMarketTenant)));
            }
            Map<Integer, List<SafetyInspectRecordItemPO>> finalMarketMap = marketMap;
            tenantMap.forEach((k, v) -> {
                int count = CollectionUtils.isEmpty(finalMarketMap.get(k)) ? 0 : finalMarketMap.get(k).size();
                valueList.add(count);
            });
            yData.add(PileUpMapVO.builder().name(value.getDesc()).code(value.name()).value(valueList).build());
        }
        return PagingUtils.convert(new Page<>(), Collections.singletonList(StackedBarChartVO.builder().xdata(xData).ydata(yData).build()));
    }

    @Override
    public Integer getInspectRecordCount(InspectionItemStatisticsQuery query) {
        return this.baseMapper.selectInspectRecordCount(query);
    }

    private List<SafetyInspectRecordItemPO> getVehicleViolationItem(InspectionItemStatisticsQuery query) {
        List<SafetyInspectRecordItemPO> result = new ArrayList<>();
        Period period = query.getPeriod();
        boolean periodNotNull = period != null && period.getBegin() != null && period.getEnd() != null;
        List<SafetyInspectRecordItemPO> recordItemPOList = this.list(Wrappers.<SafetyInspectRecordItemPO>lambdaQuery()
                                                                             .eq(SafetyInspectRecordItemPO::getErrorValueFlag, true)
                                                                             .ge(periodNotNull, SafetyInspectRecordItemPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin))
                                                                             .le(periodNotNull, SafetyInspectRecordItemPO::getCreateTime, ObjectUtils.ifNull(period, Period::getEnd))
                                                                             .eq(query.getMarketTenant() !=
                                                                                 null, SafetyInspectRecordItemPO::getMarketTenant, query.getMarketTenant())
                                                                             .in(!CollectionUtils.isEmpty(query.getTagList()), SafetyInspectRecordItemPO::getItemTag, query.getTagList())
                                                                             .in(!CollectionUtils.isEmpty(query.getMarketTenantList()), SafetyInspectRecordItemPO::getMarketTenant, query.getMarketTenantList())
                                                                    );
        if (CollectionUtils.isEmpty(recordItemPOList)) {
            return result;
        }
        return recordItemPOList;
    }
}
