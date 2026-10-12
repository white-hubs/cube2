package com.guhecloud.cube.base.alarm.server.riskscore.init;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.base.alarm.server.riskscore.po.SafetyRiskScorePO;
import com.guhecloud.cube.base.alarm.server.riskscore.service.MarketSafetyRiskScoreService;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.RiskScoreCache;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.dto.SafetyRiskScoreDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EventBusListen(order = 1)
public class SafetyRiskScoreInit {

    @Autowired
    private RiskScoreCache riskScoreCache;

    @Autowired
    private MarketSafetyRiskScoreService marketSafetyRiskScoreService;

    @Subscribe
    @AllowConcurrentEvents
    public void loadRiskScoreConfigCache(SysReadEvent event) throws Exception {
        try {
            List<SafetyRiskScorePO> safetyRiskScorePOS = this.marketSafetyRiskScoreService.list();
            if (CollectionUtils.isEmpty(safetyRiskScorePOS)) {
                return;
            }
            List<SafetyRiskScoreDTO> safetyRiskScoreDTOS = ObjectUtils.copying(safetyRiskScorePOS, SafetyRiskScoreDTO.class);
            this.riskScoreCache.setRiskScoreConfigs(safetyRiskScoreDTOS);
        } catch (Exception e) {
            log.error("初始化分值区间配置到Redis失败,失败异常原因:{}", e);
        }
    }
}
