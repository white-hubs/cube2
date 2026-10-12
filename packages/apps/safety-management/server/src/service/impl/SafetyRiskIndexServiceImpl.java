package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.SafetyRiskIndexMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.RiskIndexQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.RiskIndexDetailVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.RiskIndexVO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyRiskIndexPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyRiskIndexService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wql
 * @describe 安全风险指数
 * @date 2023/10/12 15:58
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyRiskIndexServiceImpl extends ServiceImpl<SafetyRiskIndexMapper, SafetyRiskIndexPO> implements SafetyRiskIndexService {
    private final MarketConfigService marketConfigService;

    @Override
    public List<RiskIndexVO> riskIndexList(RiskIndexQuery query) {
        String period = getCurrentPeriod();
        List<SafetyRiskIndexPO> list = this.lambdaQuery().eq(SafetyRiskIndexPO::getStatisticalPeriod, period)
                .eq(query.getMarketTenant() != null, SafetyRiskIndexPO::getMarketTenant, query.getMarketTenant())
                .orderByDesc(SafetyRiskIndexPO::getRiskIndex).list();
        return ObjectUtils.copying(list, RiskIndexVO.class);
    }

    @Override
    public List<RiskIndexVO> riskIndexPeriodList(RiskIndexQuery query) {
        List<SafetyRiskIndexPO> list = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(query.getStatisticalDate()), SafetyRiskIndexPO::getStatisticalDate, query.getStatisticalDate())
                .eq(query.getMarketTenant() != null, SafetyRiskIndexPO::getMarketTenant, query.getMarketTenant())
                .orderByDesc(SafetyRiskIndexPO::getRiskIndex).list();
        return ObjectUtils.copying(list, RiskIndexVO.class);
    }

    @Override
    public RiskIndexDetailVO riskIndexDetail(RiskIndexQuery query) {
        String period = getCurrentPeriod();
        List<SafetyRiskIndexPO> list = this.lambdaQuery().eq(SafetyRiskIndexPO::getStatisticalPeriod, period)
                .eq(SafetyRiskIndexPO::getMarketTenant, query.getMarketTenant())
                .orderByDesc(SafetyRiskIndexPO::getRiskIndex).list();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        SafetyRiskIndexPO safetyRiskIndexPO = list.get(0);
        if (StringUtils.isEmpty(safetyRiskIndexPO.getRiskIndexDetailJson())) {
            return null;
        }
        return JsonUtils.deserialize(safetyRiskIndexPO.getRiskIndexDetailJson(), RiskIndexDetailVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SafetyRiskIndexPO> riskIndexGenerate(RiskIndexQuery query) {
        List<SafetyRiskIndexPO> vos = new ArrayList<>();
        List<MarketConfigPO> markets = marketConfigService.getMarketConfigList(query.getMarketTenant());
        if (ObjectUtils.isNotEmpty(markets)) {
            String period = getCurrentPeriod();
            markets.forEach(market -> {
                if (ObjectUtils.isNull(market.getStatus()) || !market.getStatus()) {
                    //先删除后生成
                    this.remove(new LambdaQueryWrapper<SafetyRiskIndexPO>()
                                        .eq(SafetyRiskIndexPO::getStatisticalPeriod, period)
                                        .eq(SafetyRiskIndexPO::getMarketTenant, market.getMarketTenant())
                               );
                    vos.add(SafetyRiskIndexPO.builder()
                                    .statisticalDate(String.join(",", DateUtils.format(query.getPeriod()
                                                                                               .getBegin()), DateUtils.format(query
                                                                                                                                      .getPeriod()
                                                                                                                                      .getEnd())))
                                    .statisticalPeriod(period)
                                    .marketTenant(market.getMarketTenant())
                                    .marketName(market.getMarketName())
                                    .riskIndex(0)
                                    .riskIndexDetailJson(null)
                                    .build());
                }
            });
        }
        this.saveBatch(vos);
        return vos;
    }

    /**
     * @Author wql
     * @Description 获取当前周期
     * @Date 9:44 2023/10/17
     **/
    private String getCurrentPeriod() {
        var now = LocalDateTime.now();
        var startYear = DateUtils.beginOfYear(now);
        int year = now.getYear();
        int i = startYear.getDayOfWeek().getValue();
        long betweenDay = DateUtils.between(startYear, now, ChronoUnit.DAYS);
        long totalDay = betweenDay + i;
        long integerWeek = totalDay % 7;
        long temp = totalDay / 7;
        long week = integerWeek == 0 ? temp : temp + 1;
        return String.valueOf(year) + week;
    }
}
