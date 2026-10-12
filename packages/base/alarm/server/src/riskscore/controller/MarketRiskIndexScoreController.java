package com.guhecloud.cube.base.alarm.server.riskscore.controller;

import com.guhecloud.cube.srv.gen.riskscore.sdk.service.CalculateScoreQueryService;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.RiskIndexDetailVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketRiskIndexScoreController {

    private final CalculateScoreQueryService calculateScoreQueryService;

    /**
     * 风险指标分数
     *
     * @return
     */
    @GetMapping("/risk-index")
    public Integer getRiskIndexScore(Period period) {
        LocalDateTime beginOfWeek = DateUtils.beginOfWeek(DateUtils.lastWeek());
        if (Objects.nonNull(period)) {
            beginOfWeek = DateUtils.beginOfWeek(period.getBegin());
        }
        return calculateScoreQueryService.getRiskIndexScore(beginOfWeek, SessionContextHolder.getRequester()
                .getTenant());
    }

    /**
     * 风险指标详情
     *
     * @param period
     *
     * @return
     */
    @GetMapping("/risk-index/detail")
    public RiskIndexDetailVO getRiskIndexDetail(Period period) {
        LocalDateTime beginOfWeek = DateUtils.beginOfWeek(DateUtils.lastWeek());
        if (Objects.nonNull(period)) {
            beginOfWeek = DateUtils.beginOfWeek(period.getBegin());
        }
        return calculateScoreQueryService.getRiskIndexDetail(beginOfWeek, SessionContextHolder.getRequester().getTenant());
    }
}
