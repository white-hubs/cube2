package com.guhecloud.cube.srv.gen.safety.management.controller.riskIndex;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.RiskIndexQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.RiskIndexDetailVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.RiskIndexVO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyRiskIndexPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyRiskIndexService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 安全风险指数
 *
 * @author wql
 * @describe 安全风险指数
 * @date 2023/10/12 19:57
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyRiskIndexController {
    private final SafetyRiskIndexService safetyRiskIndexService;
    private final MarketConfigService marketConfigService;

    /**
     * 生成安全风险指数
     *
     * @Author wql
     * @Description 生成安全风险指数
     * @Date 9:47 2023/10/16
     **/
    @GetMapping("/riskIndex/generate")
    public List<SafetyRiskIndexPO> riskIndexGenerate(RiskIndexQuery query) {
        return safetyRiskIndexService.riskIndexGenerate(query);
    }

    /**
     * 查询安全风险指数
     *
     * @Author wql
     * @Description 查询安全风险指数
     * @Date 9:25 2023/10/17
     **/
    @GetMapping("/riskIndex/list")
    public List<RiskIndexVO> riskIndexList(RiskIndexQuery query) {
        if (query.getPeriod() != null) {
            query.setStatisticalDate(
                    DateUtils.format(query.getPeriod().getBegin()) + "," + DateUtils.format(query.getPeriod().getEnd()));
        }
        List<RiskIndexVO> riskIndexVOS = safetyRiskIndexService.riskIndexPeriodList(query);
        if (CollectionUtils.isEmpty(riskIndexVOS)) {
            return riskIndexVOS;
        }
        Set<Integer> tenants = riskIndexVOS.stream().map(RiskIndexVO::getMarketTenant).collect(Collectors.toSet());
        List<MarketConfigPO> marketConfigPOS = this.marketConfigService.list(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                     .in(MarketConfigPO::getMarketTenant, tenants));
        riskIndexVOS.forEach(riskIndexVO -> {
            Optional<MarketConfigPO> marketConfigOptional = marketConfigPOS.stream()
                    .filter(marketConfigPO -> marketConfigPO.getMarketTenant().intValue() == riskIndexVO.getMarketTenant().intValue())
                    .findFirst();
            marketConfigOptional.ifPresent(marketConfigPO ->
                                                   riskIndexVO.setMarketName(marketConfigPO.getMarketName())
                                          );
        });
        return riskIndexVOS;
    }

    /**
     * 查询市场安全风险详情
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/riskIndex/detail")
    public RiskIndexDetailVO riskIndexDetail(@Valid RiskIndexQuery query) {
        return safetyRiskIndexService.riskIndexDetail(query);
    }
}
