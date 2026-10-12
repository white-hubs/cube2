package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.PropertyStatisticsMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountPropertyFilingNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.FilingDeviceQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.PropertyFilingNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.risksource.RiskSourceMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.KeyValueVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AppSafetyWorkSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketBaseVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketTenantBarVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.PileUpMapVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.FilingDeviceVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.FlammableDeviceFilingVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.MarketNumStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.PropertyByTypePageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.PropertyFilingAnalyseVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.PropertyFilingStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.RiskSourceCountStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.PropertyCategoryEnum;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.PropertyStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.po.PropertyFilingPO;
import com.guhecloud.cube.srv.gen.safety.management.service.LooseWorksStatisticService;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.cube.srv.gen.safety.management.service.PropertyStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.util.CommonUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 资产统计
 * @date 2023/9/8 17:08
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyStatisticsServiceImpl extends ServiceImpl<PropertyStatisticsMapper, PropertyFilingPO> implements PropertyStatisticsService {
    private final LooseWorksStatisticService looseWorksStatisticService;
    private final MarketConfigService marketConfigService;

    @Override
    public Pagination<FilingDeviceVO> getDeviceExpireList(FilingDeviceQuery expiredDevice) {
        Page<PropertyFilingPO> page = this.baseMapper.getPropertyFilingPage(PagingUtils.convert(expiredDevice), expiredDevice);
        List<FilingDeviceVO> list = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(po -> {
                list.add(FilingDeviceVO.builder()
                                 .deviceName(po.getPropertyName())
                                 .marketTenant(po.getMarketTenant())
                                 .marketName(po.getMarketName())
                                 .deviceType(PropertyCategoryEnum.getDescription(po.getPropertySubType()))
                                 .putInDate(po.getPutInTime())
                                 .expireDate(po.getExpirationTime())
                                 .state(PropertyStatusEnum.valueOf(po.getStatus()).getRemarks())
                                 .manager(po.getManagerName())
                                 .area(po.getAreaName())
                                 .deviceUser(po.getUsername())
                                 .num(po.getNum())
                                 .bimCode(po.getBimCode())
                                 .serialNumber(po.getSerialNumber())
                                 .attribute(po.getAttribute())
                                 .stallName(po.getStallName())
                                 .sitePointName(po.getSitePointName())
                                 .propertyName(po.getPropertyName())
                                 .receiveDepName(po.getReceiveDepName())
                                 .maintenanceDepName(po.getMaintenanceDepName())
                                 .address(po.getAddress())
                                 .electronMobileType(po.getElectronMobileType())
                                 .build());
            });
        }
        return PagingUtils.convert(page, list);
    }

    @Override
    public List<PropertyFilingAnalyseVO> getFilingAnalyse(BaseQuery query) {
        List<PropertyFilingPO> list = this.baseMapper.getPropertyFilingList(query);
        List<PropertyFilingAnalyseVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)) {
            BigDecimal total = BigDecimal.valueOf(countSumNum(list));
            Map<String, List<PropertyFilingPO>> listMap = list.stream().collect(Collectors.groupingBy(PropertyFilingPO::getPropertyType));
            Map<String, List<PropertyFilingPO>> subListMap = list.stream()
                    .collect(Collectors.groupingBy(PropertyFilingPO::getPropertySubType));
            Map<String, Integer> collect = groupCountProperty(subListMap);
            if (ObjectUtils.isNotEmpty(collect)) {
                for (String key : collect.keySet()) {
                    if (PropertyCategoryEnum.OXYGEN_CYLINDER.name().equals(key) || PropertyCategoryEnum.ELECTRO_MOBILE.name().equals(key)) {
                        Integer num = collect.get(key);
                        vos.add(PropertyFilingAnalyseVO.builder()
                                        .Name(PropertyCategoryEnum.getDescription(key))
                                        .num(num)
                                        .ratio(CommonUtil.countProportion(total, BigDecimal.valueOf(num)))
                                        .build());
                    }
                }
                List<PropertyFilingPO> propertyFilingPOList = listMap.get(PropertyCategoryEnum.FIRE_FIGHTING_EQUIPMENT.name());
                vos.add(PropertyFilingAnalyseVO.builder()
                                .Name(PropertyCategoryEnum.FIRE_FIGHTING_EQUIPMENT.getDesc())
                                .num(Optional.ofNullable(propertyFilingPOList).orElse(new ArrayList<>()).size())
                                .ratio(CommonUtil.countProportion(total, BigDecimal.valueOf(Optional.ofNullable(propertyFilingPOList)
                                                                                                    .orElse(new ArrayList<>()).size())))
                                .build());
            }
        }
        return vos;
    }

    @Override
    public Pagination<MarketNumStatisticsVO> getDeviceExpireStatistics(BaseQuery query) {
        List<MarketNumStatisticsVO> MarketNumStatisticsVO = this.baseMapper.getDeviceExpireStatistics(query,
                                                                                                      PropertyCategoryEnum.FIRE_FIGHTING_EQUIPMENT.name(), "1");
        return PagingUtils.convert(new Page<>(), MarketNumStatisticsVO);
    }

    private Map<String, Integer> groupCountProperty(Map<String, List<PropertyFilingPO>> listMap) {
        Map<String, Integer> collect = new HashMap<>();
        if (ObjectUtils.isNotEmpty(listMap)) {
            listMap.keySet().forEach(key -> {
                List<PropertyFilingPO> pos = listMap.get(key);
                if (ObjectUtils.isNotEmpty(pos)) {
                    collect.put(key, countSumNum(pos));
                }
            });
        }
        return collect;
    }

    @Override
    public FlammableDeviceFilingVO getFlammableDeviceStatistics(BaseQuery query) {
        query.setTypeCode(PropertyCategoryEnum.FLAMMABLE_EQUIPMENT.name());
        List<PropertyFilingPO> list = this.baseMapper.getPropertyFilingList(query);
        FlammableDeviceFilingVO vo = new FlammableDeviceFilingVO();
        if (ObjectUtils.isNotEmpty(list)) {
            Optional<Integer> reduce = list.stream().map(PropertyFilingPO::getNum).reduce(Integer::sum);
            int total = reduce.orElse(0);
            vo.setTotalNum(total);
            Map<String, List<PropertyFilingPO>> listMap = list.stream().collect(Collectors.groupingBy(PropertyFilingPO::getStatus));
            Map<String, Integer> collect = groupCountProperty(listMap);
            if (ObjectUtils.isNotEmpty(collect)) {
                List<FlammableDeviceFilingVO.FlammableDeviceStatusVO> status = new ArrayList<>();
                collect.keySet().forEach(key -> {
                    status.add(FlammableDeviceFilingVO.FlammableDeviceStatusVO.builder()
                                       .deviceState(PropertyStatusEnum.valueOf(key).getRemarks())
                                       .stateNum(collect.get(key))
                                       .ratio(CommonUtil.countProportion(BigDecimal.valueOf(total), BigDecimal.valueOf(collect.get(key))))
                                       .build());
                });
                vo.setStatusVOList(status);
            }
        }
        return vo;
    }

    @Override
    public List<KeyValueVO> getPropertyTypeList() {
        List<String> list = this.baseMapper.getPropertyTypeList();
        List<KeyValueVO> typeNames = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)) {
            list.forEach(key -> typeNames.add(KeyValueVO.builder().key(key).value(PropertyCategoryEnum.getDescription(key))
                                                      .build()));
        }
        return typeNames;
    }

    @Override
    public PropertyFilingStatisticsVO getPropertyFilingStatistics(BaseQuery query) {
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setMarketTenant(query.getMarketTenant());
        List<PropertyFilingPO> list = this.baseMapper.getPropertyFilingList(baseQuery);
        PropertyFilingStatisticsVO vo = new PropertyFilingStatisticsVO();
        if (ObjectUtils.isNotEmpty(list)) {
            AtomicReference<Integer> total = new AtomicReference<>(0);
            list.stream().forEach(po -> {
                if (PropertyCategoryEnum.FIRE_FIGHTING_EQUIPMENT.name().equals(po.getPropertyType())) {
                    total.updateAndGet(v -> v + 1);
                }
            });
            Map<String, List<PropertyFilingPO>> listMap = list.stream()
                    .collect(Collectors.groupingBy(PropertyFilingPO::getPropertySubType));
            Map<String, Integer> collect = groupCountProperty(listMap);
            if (ObjectUtils.isNotEmpty(collect)) {
                vo.setElectricVehicleNum(collect.getOrDefault(PropertyCategoryEnum.ELECTRO_MOBILE.name(), 0));
                vo.setFlammableEquipmentNum(collect.getOrDefault(PropertyCategoryEnum.OXYGEN_CYLINDER.name(), 0));
                vo.setFireFightingEquipmentNum(total.get());
            }
        }
        query.setExpired(true);
        vo.setFireFightingEquipmentExpiredNum(getFireFightingEquipmentExpiredNum(query));
        vo.setLooseWorksNum(looseWorksStatisticService.looseWorksNumStatistics(query));
        return vo;
    }

    /**
     * @Author wql
     * @Description 设备建档数统计
     * @Date 11:44 2023/9/14
     **/
    @Override
    public Integer getDeviceFilingNum(CountPropertyFilingNumQuery query) {
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setPeriod(query.getPeriod());
        baseQuery.setMarketTenant(query.getMarketTenant());
        baseQuery.setTypeCode(query.getDeviceType());
        List<PropertyFilingPO> pos = this.baseMapper.getPropertyFilingList(baseQuery);
        return ObjectUtils.isNotEmpty(pos) ? countSumNum(pos) : 0;
    }

    private Integer countSumNum(List<PropertyFilingPO> pos) {
        Optional<Integer> reduce = pos.stream().map(PropertyFilingPO::getNum).reduce(Integer::sum);
        return reduce.orElse(0);
    }

    @Override
    public Integer getFireFightingEquipmentExpiredNum(BaseQuery query) {
        query.setTypeCode(PropertyCategoryEnum.FIRE_FIGHTING_EQUIPMENT.name());
        List<PropertyFilingPO> pos = this.baseMapper.getPropertyFilingList(query);
        return countSumNum(pos);
    }

    @Override
    public Pagination<MarketNumStatisticsVO> getFlammableDeviceList(BaseQuery query) {
        List<MarketNumStatisticsVO> vos = this.baseMapper.getDeviceExpireStatistics(query, PropertyCategoryEnum.FLAMMABLE_EQUIPMENT.name(), null);
        return PagingUtils.convert(new Page<>(), vos);
    }

    @Override
    public Pagination<MarketNumStatisticsVO> getElectricVehicleDeviceList(BaseQuery query) {
        query.setSubTypeCode(PropertyCategoryEnum.ELECTRO_MOBILE.name());
        Page<MarketNumStatisticsVO> page = this.baseMapper.getDeviceExpireStatisticsPage(PagingUtils.convert(query), query, null, null);
        return PagingUtils.convert(page);
    }

    @Override
    public Pagination<MarketNumStatisticsVO> getPropertyFilingList(BaseQuery query) {
        List<MarketNumStatisticsVO> vos = this.baseMapper.getDeviceExpireStatistics(query, null, null);
        return PagingUtils.convert(new Page<>(), vos);
    }

    @Override
    public Integer countPropertyFilingNum(PropertyFilingNumQuery query) {
        List<PropertyFilingPO> pos = this.baseMapper.getPropertyFilingNumList(query);
        return countSumNum(pos);
    }

    @Override
    public Pagination<PropertyByTypePageVO> getPropertyTypePage(BaseQuery query) {
        FilingDeviceQuery filingDeviceQuery = new FilingDeviceQuery();
        filingDeviceQuery.setPageNumber(query.getPageNumber());
        filingDeviceQuery.setPageSize(query.getPageSize());
        filingDeviceQuery.setMarketTenant(query.getMarketTenant());
        filingDeviceQuery.setType(query.getTypeCode());
        filingDeviceQuery.setExpired(query.getExpired());
        filingDeviceQuery.setKeyword(query.getKeyword());
        Page<PropertyFilingPO> page = this.baseMapper.getPropertyFilingPage(PagingUtils.convert(filingDeviceQuery), filingDeviceQuery);
        return PagingUtils.convert(page, ConvertUtils.toList(PropertyByTypePageVO.class, page.getRecords()));
    }

    @Override
    public AppSafetyWorkSituationVO.WorkSituationVO getPropertyFilingRatioByMarket() {
        long marketCount = this.marketConfigService.count(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                  .eq(MarketConfigPO::getStatus, Boolean.TRUE));
        Integer filingMarketCount = this.baseMapper.selectTenantCount();
        return AppSafetyWorkSituationVO.WorkSituationVO.builder()
                .totalNum(filingMarketCount)
                .ratio(MathUtils.calculateProportionPercentage(filingMarketCount, marketCount))
                .build();
    }

    @Override
    public List<RiskSourceCountStatusVO> countStatusByType(RiskSourceMetricQuery query) {
        return this.baseMapper.countStatusByType(query, query.getRiskSourceTypes());
    }

    @Override
    public List<GroupCountVO> countByMarket(BaseStatisticsQuery query) {
        return this.baseMapper.countByMarket(query);
    }

    @Override
    public List<GroupCountVO> countByType(RiskSourceMetricQuery query) {
        return this.baseMapper.countByType(query);
    }

    /**
     * 柱状图
     *
     * @return
     */
    @Override
    public Pagination<StackedBarChartVO> getVehicleCountBar(RiskSourceMetricQuery param) {
        param.setRiskSourceTypes(Collections.singletonList(PropertyCategoryEnum.ELECTRO_MOBILE.name()));
        List<MarketTenantBarVO> countVOList = this.baseMapper.countTypeTenant(param);
        if (CollectionUtils.isEmpty(countVOList)) {
            return Pagination.empty();
        }
        List<MarketBaseVO> xData = new ArrayList<>();
        List<PileUpMapVO> yData = new ArrayList<>();
        List<Integer> vlaueList = new ArrayList<>();
        AtomicReference<Integer> total = new AtomicReference<>(BigDecimal.ZERO.intValue());
        countVOList.forEach(v -> {
            xData.add(MarketBaseVO.builder().marketName(v.getMarketName()).marketTenant(v.getMarketTenant()).build());
            vlaueList.add(v.getCount());
            total.updateAndGet(v1 -> v1 + v.getCount());
        });
        yData.add(PileUpMapVO.builder().value(vlaueList).build());
        return PagingUtils.convert(new Page<>(0, 0, total.get()), Collections.singletonList(StackedBarChartVO.builder().xdata(xData)
                                                                                                    .ydata(yData).build()));
    }

    /**
     * 饼图
     *
     * @return
     */
    @Override
    public List<AlarmEventTypeRatioVO> getVehicleCountPie(RiskSourceMetricQuery param) {
        List<AlarmEventTypeRatioVO> result = new ArrayList<>();
        param.setRiskSourceTypes(Collections.singletonList(PropertyCategoryEnum.ELECTRO_MOBILE.name()));
        List<MarketTenantBarVO> countVOList = this.baseMapper.countTypeTenant(param);
        if (CollectionUtils.isEmpty(countVOList)) {
            return result;
        }
        int total = countVOList.stream().filter(c -> c.getCount() != null).mapToInt(MarketTenantBarVO::getCount).sum();
        countVOList.forEach(g -> {
            AlarmEventTypeRatioVO vo = new AlarmEventTypeRatioVO();
            vo.setTotalNum(g.getCount());
            vo.setName(g.getMarketName());
            vo.setRatio(0 == total ? BigDecimal.valueOf(0.0) :
                        BigDecimal.valueOf(g.getCount())
                                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(100)));
            result.add(vo);
        });
        return result;
    }

    @Override
    public Long getElectrlcVehicleDeviceCount() {
        return this.baseMapper.selectCount(Wrappers.<PropertyFilingPO>lambdaQuery()
                                                   .eq(PropertyFilingPO::getPropertySubType, PropertyCategoryEnum.ELECTRO_MOBILE.name()));
    }

}
