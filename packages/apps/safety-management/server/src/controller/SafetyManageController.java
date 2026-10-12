package com.guhecloud.cube.srv.gen.safety.management.controller;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketConfigQueryParam;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AppSafetyWorkSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyMarketStatisticsCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyStatisticsCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyWorkStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 安全管理可视化
 *
 * @author wql
 * @describe 安全管理可视化
 * @date 2023/9/7 15:04
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyManageController {

    private final SafetyManageService safetyManageService;

    private final MarketConfigService marketConfigService;

    /**
     * 安全工作情况统计
     *
     * @Author wql
     * @Description 安全工作情况统计
     * @Date 19:04 2023/9/7
     **/
    @GetMapping("/safety/safetyWork")
    public SafetyWorkStatusVO getSafetyWorkStatus(BaseQuery query) {
        return safetyManageService.getSafetyWorkStatus(query);
    }

    /**
     * 安全卡片统计
     *
     * @Author wql
     * @Description 安全卡片统计
     * @Date 20:07 2023/9/7
     **/
    @GetMapping("/safety/safetyCard")
    public SafetyStatisticsCardVO getSafetyCardStatistics(BaseQuery query) {
        return safetyManageService.getSafetyCardStatistics(query);
    }

    /**
     * 市场安全卡片统计
     *
     * @Author wql
     * @Description 市场安全卡片统计
     * @Date 20:07 2023/9/7
     **/
    @GetMapping("/safety/marketSafetyCard")
    public SafetyMarketStatisticsCardVO getSafetyMarketCardStatistics(BaseQuery cardQuery) {
        return safetyManageService.getSafetyMarketCardStatistics(cardQuery);
    }

    /**
     * app安全工作情况统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/app/safety-work-Situation")
    public AppSafetyWorkSituationVO getAppSafetyWorkSituation(@Valid BaseQuery query) {
        return safetyManageService.getAppSafetyWorkSituation(query);
    }

    /**
     * 获取市场配置
     */
    @GetMapping("/safety/market-config/list")
    public List<MarketConfigPO> getMarketConfigList(MarketConfigQueryParam configQueryParam) {
        return marketConfigService.getList(configQueryParam);
    }
}
